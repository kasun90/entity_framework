package com.ust.cluster.impl;

import com.google.common.base.Preconditions;
import com.ust.cluster.core.ServiceHeaders;
import com.ust.cluster.core.ServiceInstance;
import com.ust.cluster.core.ServiceRegistry;
import com.ust.cluster.core.ServiceRouter;
import io.scalecube.transport.Message;
import net.openhft.hashing.LongHashFunction;

public class ConsistentHashRingRouter extends ServiceRouter {

    public ConsistentHashRingRouter(ServiceRegistry registry) {
        super(registry);
    }

    @Override
    public ServiceInstance route(Message message) {
        String serviceName = message.header(ServiceHeaders.SERVICE_NAME);
        String routeKey = message.header(ServiceHeaders.ROUTE_KEY);
        
        Preconditions.checkArgument(serviceName != null);
        //if route key not found it will routed to first service node in the ring
        if(routeKey == null)
            return registry.first(serviceName);
        
        return registry.findService(serviceName,LongHashFunction.xx_r39().hashChars(routeKey));
    }

}
