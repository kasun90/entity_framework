package com.ust.cluster.impl;

import com.google.common.base.Preconditions;
import com.ust.cluster.core.ServiceHeaders;
import com.ust.cluster.core.ServiceInstance;
import io.scalecube.transport.Message;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class SimpleLocalServiceInstance implements ServiceInstance {

    private final String serviceName;
    private final long serviceID;
    private final Map<String, Method> methods;
    private final Object serviceObject;

    public SimpleLocalServiceInstance(String serviceName, Object serviceObject, long serviceID
        , Map<String, Method> methods) {
        this.serviceName = serviceName;
        this.serviceObject = serviceObject;
        this.serviceID = serviceID;
        this.methods = methods;
    }

    @Override
    public String getName() {
        return serviceName;
    }

    @Override
    public long getId() {
        return serviceID;
    }

    @Override
    public Object invoke(Message req) {
        String methodName = req.header(ServiceHeaders.METHOD_NAME);
        Preconditions.checkArgument(methodName != null);
        try {
            Object result;
            Method method = methods.get(methodName);
            if (method.getParameters().length == 0) {
                result = method.invoke(serviceObject);
            } else {
                result = method.invoke(serviceObject, new Object[] {req.data()});
            }

            return result;
        } catch (Throwable thr) {
            return thr;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.serviceName);
        hash = 59 * hash + (int) (this.serviceID ^ (this.serviceID >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleLocalServiceInstance other = (SimpleLocalServiceInstance) obj;
        if (this.serviceID != other.serviceID) {
            return false;
        }
        return Objects.equals(this.serviceName, other.serviceName);
    }

    
    @Override
    public void shutdown() {
    }

    @Override
    public boolean isLocal() {
        return true;
    }

}
