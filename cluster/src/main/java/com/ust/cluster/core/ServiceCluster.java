/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.core;

import com.ust.cluster.impl.SimpleLocalServiceInstance;
import com.ust.cluster.impl.RemoteServiceInstance;
import com.ust.cluster.util.ServiceUtil;
import io.scalecube.transport.Address;
import io.scalecube.transport.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import net.openhft.hashing.LongHashFunction;

/**
 *
 * @author nuwan
 */
public class ServiceCluster {

    private final Cluster cluster;
    private final ServiceRegistry serviceRegistry;
    private final ServiceProxy serviceProxy;
    private final CompletionFutureHandler futureHandler;
    private final CountDownLatch latch = new CountDownLatch(1);

    public ServiceCluster(Cluster cluster, ServiceRegistry registry) {
        this.cluster = cluster;
        this.serviceRegistry = registry;
        this.serviceProxy = new ServiceProxy(serviceRegistry);
        this.futureHandler = new CompletionFutureHandler();
        listenServices();
        listenRequest();
        listenResponse();
    }

    public void registerServiceInstance(SimpleLocalServiceInstance serviceInstance,Map<String,String> attr) {
        serviceRegistry.registerService(serviceInstance);
        AttributeMap attrMap = AttributeMap.builder().attr(Member.ATTR_IP, cluster.getTransport().address().host()).attr(Member.ATTR_PORT,
                cluster.getTransport().address().port())
                .attr(ServiceHeaders.SERVICE_NAME, serviceInstance.getName()).attrMap(attr).build();
        cluster.getMembershipProtocol().addMember("services", "" + serviceInstance.getId(), attrMap.map());
    }

    public void registerService(Object serviceInstance,Map<String,String> attr) {
        SimpleLocalServiceInstance container = new SimpleLocalServiceInstance(ServiceUtil.getServiceInterface(serviceInstance).getName(),
                serviceInstance, LongHashFunction.xx_r39().hashChars(UUID.randomUUID().toString()),
                ServiceUtil.extractMethods(serviceInstance));
        registerServiceInstance(container,attr);
    }
    
    public void registerService(Object serviceInstance) {
        registerService(serviceInstance, new HashMap<>());
    }

    public ServiceProxy proxy() {
        return serviceProxy;
    }

    public ServiceRouterFactory routerFactory() {
        return serviceProxy.getRouterFactory();
    }

    private void listenServices() {
        this.cluster.getMembershipProtocol().listen("services", this::onServiceNotification);
    }

    private void onServiceNotification(MembershipEvent event) {
        switch (event.getEventType()) {
            case ADDED: {
                serviceRegistry.registerService(createRemoteService(event.getMember()));
                break;
            }
            case UPDATED: {
                break;
            }
            case REMOVED: {
                serviceRegistry.removeService(event.getMember().getMeta(ServiceHeaders.SERVICE_NAME).toString(),
                        event.getMember().getId());
                break;
            }
        }
    }

    private RemoteServiceInstance createRemoteService(Member member) {
        return new RemoteServiceInstance(member.getMeta(ServiceHeaders.SERVICE_NAME).toString(), member.getId(), cluster.getTransport(),
                Address.create(member.getIp(), member.getPort()), futureHandler);
    }

    private void listenRequest() {
        cluster.getTransport().listen().subscribe(message -> handleRequest(message));
    }

    private void listenResponse() {
        cluster.getTransport().listen().subscribe(message -> handleResponse(message));
    }

    private void handleRequest(Message message) {
        if (message.header(ServiceHeaders.SERVICE_REQUEST) != null) {
            ServiceInstance serviceInstance = serviceRegistry.findLocalService(message.header(ServiceHeaders.SERVICE_REQUEST));

            DispatchingFuture result = DispatchingFuture.from(cluster, message);
            try {
                if (serviceInstance != null) {
                    result.complete(serviceInstance.invoke(message));
                } else {
                    result.completeExceptionally(new IllegalStateException("Service instance is missing: " + message.qualifier()));
                }
            } catch (Exception ex) {
                result.completeExceptionally(ex);
            }
        }
    }

    private void handleResponse(Message message) {
        if (message.header(ServiceHeaders.SERVICE_RESPONSE) != null) {
            CompletionFutureEx<?> future = futureHandler.get(message.correlationId());
            if (future != null) {
                future.complete(message.data());
            }
        }
    }

    public void start() throws InterruptedException {
        latch.await();
    }

    public void shutdown() {
        latch.countDown();
    }
}
