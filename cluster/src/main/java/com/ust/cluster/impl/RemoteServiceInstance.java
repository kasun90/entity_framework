/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.impl;

import com.ust.cluster.core.CompletionFutureEx;
import com.ust.cluster.core.CompletionFutureHandler;
import com.ust.cluster.core.ServiceHeaders;
import com.ust.cluster.core.ServiceInstance;
import io.scalecube.transport.Address;
import io.scalecube.transport.Message;
import io.scalecube.transport.Transport;

/**
 *
 * @author nuwan
 */
public class RemoteServiceInstance implements ServiceInstance {

    private final String serviceName;
    private final long serviceID;
    private final Address address;
    private final Transport transport;
    private final CompletionFutureHandler futureHandler;

    public RemoteServiceInstance(String serviceName, long serviceID, Transport transport, Address address, CompletionFutureHandler futureHandler) {
        this.serviceName = serviceName;
        this.serviceID = serviceID;
        this.transport = transport;
        this.address = address;
        this.futureHandler = futureHandler;
    }

    @Override
    public String getName() {
        return this.serviceName;
    }

    @Override
    public long getId() {
        return this.serviceID;
    }

    @Override
    public Object invoke(Message message) {
        CompletionFutureEx future = (CompletionFutureEx) futureHandler.create();
        Message req = Message.with(message).correlationId(future.getCorrelationID()).
            header(ServiceHeaders.SERVICE_REQUEST, serviceName).
            header(ServiceHeaders.REMOTE_ADDR,transport.address().toString()).
            build();
        transport.send(address, req);
        return future;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean isLocal() {
        return false;
    }

}
