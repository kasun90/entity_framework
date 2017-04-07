/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.core;

import java.util.Collection;

/**
 *
 * @author nuwan
 */
public interface ServiceRegistry {
    Collection<ServiceInstance> getServices(String serviceName);
    void registerService(ServiceInstance instance);
    ServiceInstance findService(String serviceName,Long key);
    ServiceInstance findLocalService(String serviceName);
    ServiceInstance first(String serviceName);
    long getTopologyChecksum(String serviceName);
    void removeService(String serviceName,long serviceID);
}