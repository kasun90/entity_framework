/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.core;

import com.google.common.reflect.Reflection;
import com.ust.cluster.ex.ServiceException;
import io.scalecube.transport.Message;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;

/**
 *
 * @author nuwan
 */
public class ServiceProxy {

    private final ServiceRegistry serviceRegistry;
    private final ServiceRouterFactory routerFactory;

    public ServiceProxy(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        routerFactory = new ServiceRouterFactory(serviceRegistry);
    }

    public ServiceRouterFactory getRouterFactory() {
        return routerFactory;
    }

    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    public <T> T api(Class<T> serviceInterface, Class<? extends ServiceRouter> routerType, Duration duration) {
        return Reflection.newProxy(serviceInterface, (Object proxy, Method method, Object[] args) -> {
            try {
                Object arg = args[0];
                String routeKey = null;
                if ((arg instanceof RoutableMessage)) {
                    routeKey = ((RoutableMessage) arg).getKey();
                }
                Message message = Message.withData(arg)
                    .header(ServiceHeaders.SERVICE_NAME, serviceInterface.getName())
                    .header(ServiceHeaders.METHOD_NAME, method.getName())
                    .header(ServiceHeaders.ROUTE_KEY, routeKey).build();
                
                ServiceRouter router = routerFactory.router(routerType);
                ServiceInstance service = router.route(message);
                completeExceptionally(() -> service == null, new ServiceException("no service instance found"));
                CompletableFuture future = (CompletableFuture) service.invoke(message);
                return future;
            } catch (Throwable throwable) {
                return completeExceptionally(throwable);
                
            }
        });
    }

    private CompletableFuture completeExceptionally(Throwable thr) {
        CompletableFuture future = new CompletableFuture();
        future.completeExceptionally(thr);
        return future;
    }

    private void completeExceptionally(BooleanSupplier supplier, Throwable thr) throws Throwable {
        if (supplier.getAsBoolean()) {
            throw thr;
        }
    }
}
