/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.core;

import io.scalecube.transport.Message;

/**
 *
 * @author nuwan
 */
public abstract class ServiceRouter {
    protected final ServiceRegistry registry;

    public ServiceRouter(ServiceRegistry registry) {
        this.registry = registry;
    }
    
    public abstract ServiceInstance route(Message message);
}
