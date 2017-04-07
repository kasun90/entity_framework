/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.core;

import com.ust.cluster.ex.ServiceException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author nuwan
 */
public class ServiceRouterFactory {

    private final Map<Class<? extends ServiceRouter>, ServiceRouter> mapRouters = new ConcurrentHashMap<>();
    private final ServiceRegistry serviceRegistry;

    public ServiceRouterFactory(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public ServiceRouter router(Class<? extends ServiceRouter> routerType) {
        return mapRouters.computeIfAbsent(routerType, this::create);
    }

    private ServiceRouter create(Class<? extends ServiceRouter> routerType) {
        try {
            return routerType.getConstructor(ServiceRegistry.class).newInstance(serviceRegistry);
        } catch (NoSuchMethodException | SecurityException | InstantiationException |
            IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new ServiceException(ex);
        }
    }
}
