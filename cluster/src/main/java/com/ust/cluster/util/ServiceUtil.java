/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.util;

import com.ust.cluster.annotation.ServiceEndpoint;
import com.ust.cluster.ex.ServiceException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author nuwan
 */
public class ServiceUtil {

    public static Map<String, Method> extractMethods(Object serviceObject) {
        Method[] methods = serviceObject.getClass().getDeclaredMethods();
        return Arrays.stream(methods).collect(Collectors.toMap(method -> method.getName(), Function.identity()));
    }

    public static Class<?> getServiceInterface(Object serviceObject) {
        Class<?> clazz = serviceObject.getClass();
        Class<?> interfaces[] = clazz.getInterfaces();
        Class<?> serviceEndpoint = Arrays.asList(interfaces).stream().filter(interfaze -> interfaze.isAnnotationPresent(ServiceEndpoint.class)).findFirst().orElse(null);
        while (serviceEndpoint == null) {
            clazz = clazz.getSuperclass();
            if (clazz.equals(Object.class)) {
                throw new ServiceException("instance not a service endpoint");
            }
            interfaces = clazz.getInterfaces();
            serviceEndpoint = Arrays.asList(interfaces).stream().filter(interfaze -> interfaze.isAnnotationPresent(ServiceEndpoint.class)).findFirst().orElse(null);
        }
        return serviceEndpoint;
    }
}
