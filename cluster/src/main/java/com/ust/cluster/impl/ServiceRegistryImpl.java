package com.ust.cluster.impl;

import com.ust.cluster.core.ServiceInstance;
import com.ust.cluster.core.ServiceRegistry;
import com.ust.cluster.util.UHashing;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.openhft.hashing.LongHashFunction;

public class ServiceRegistryImpl implements ServiceRegistry {

    private final Map<String, List<ServiceInstance>> map = new ConcurrentHashMap<>();
    private final Map<String, Long> topologyChecksum = new ConcurrentHashMap<>();
    private final Map<String, ServiceInstance> mapLocalInstances = new ConcurrentHashMap<>();
    private final Comparator<ServiceInstance> comparator;

    public ServiceRegistryImpl() {
        this.comparator = (ServiceInstance left, ServiceInstance right) -> Long.compare(left.getId(), right.getId());
    }

    @Override
    public Collection<ServiceInstance> getServices(String serviceName) {
        List services = map.get(serviceName);
        if (services == null) {
            services = Collections.EMPTY_LIST;
        }
        return services;
    }

    @Override
    public void registerService(ServiceInstance instance) {
        CopyOnWriteArrayList<ServiceInstance> services = (CopyOnWriteArrayList<ServiceInstance>) map.computeIfAbsent(instance.getName(), this::createServiceList);
        synchronized (services) {
            boolean ok = services.addIfAbsent(instance);
            if (ok) {
                Collections.sort(services, comparator);
                if (instance.isLocal()) {
                    mapLocalInstances.put(instance.getName(), instance);
                }
            }
            topologyChecksum.put(instance.getName(), calculateChecksum(services));
        }

    }

    @Override
    public void removeService(String serviceName, long serviceID) {
        CopyOnWriteArrayList<ServiceInstance> services = (CopyOnWriteArrayList<ServiceInstance>) map.get(serviceName);
        if (services != null) {
            Optional<ServiceInstance> optional = services.stream().filter(service -> service.getId() == serviceID).findAny();
            if (optional.isPresent()) {
                services.remove(optional.get());
                Collections.sort(services, comparator);
            }
            topologyChecksum.put(serviceName, calculateChecksum(services));
        }
    }

    //service instances are sorted by service id hash by default. 
    private List<ServiceInstance> createServiceList(@SuppressWarnings("unused") String serviceName) {
        return new CopyOnWriteArrayList<>();
    }

    @Override
    public ServiceInstance findService(String serviceName, Long key) {
        List<ServiceInstance> services = (List<ServiceInstance>) getServices(serviceName);
        if (services.isEmpty()) {
            return null;
        }
        int index = UHashing.consistentHash(key, services.size());
        return services.get(index);
    }

    @Override
    public ServiceInstance first(String serviceName) {
        return findService(serviceName, Long.MIN_VALUE);
    }

    @Override
    public long getTopologyChecksum(String serviceName) {
        return topologyChecksum.getOrDefault(serviceName, 0L);
    }

    private long calculateChecksum(Collection<ServiceInstance> services) {
        if (services.isEmpty()) {
            return 0;
        }
        Iterator<ServiceInstance> ite = services.iterator();
        long hash = LongHashFunction.xx_r39().hashLong(ite.next().getId());
        while (ite.hasNext()) {
            hash = LongHashFunction.xx_r39(hash).hashLong(ite.next().getId());
        }
        return hash;
    }

    @Override
    public ServiceInstance findLocalService(String serviceName) {
        return mapLocalInstances.get(serviceName);
    }
}
