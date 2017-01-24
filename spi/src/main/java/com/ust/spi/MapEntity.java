package com.ust.spi;

import com.ust.spi.ex.EntityException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class MapEntity<I, E extends Entity> extends Entity<E> {
    private static final Map<Class<?>, Map<Class<?>, Method>> maps = new ConcurrentHashMap<>();

    private Map<String, I> items = new HashMap<>();

    public Map<String, I> getItems() {
        return items;
    }

    public void applyEvent(String key, Event event) {
        event.setEntityID(getID());
        Method method = getApplyMethod(event);
        try {
            method.invoke(this, key, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EntityException(e);
        }
        events.add(event);
    }

    private Method getApplyMethod(Event event) {
        Map<Class<?>, Method> classMethodMap = maps.computeIfAbsent(this.getClass(), aClass -> buildApplyMaps());
        Method method = classMethodMap.get(event.getClass());
        if (method == null) {
            throw new EntityException("No matching event apply method found. [Entity="
                    + this.getClass().getName() + ", Event=" + event.getClass().getName() + "]");
        }
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method;
    }

    private Map<Class<?>, Method> buildApplyMaps() {
        Method[] methods = this.getClass().getDeclaredMethods();
        return Arrays.stream(methods).filter(method -> method.getName().equals("apply")).filter(method -> method.getParameterTypes().length == 2).
                collect(Collectors.toMap(method -> method.getParameterTypes()[1], Function.identity()));
    }


}
