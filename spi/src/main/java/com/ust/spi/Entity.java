package com.ust.spi;

import com.ust.spi.ex.EntityException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@code Entity} provides common information about, and access to a single business entity.
 * @param <E> the type of parent entity
 */
public abstract class Entity<E extends Entity> {

    protected long version;
    protected final List<Event> events;
    protected static final Map<Class<?>, Map<Class<?>, Method>> map = new ConcurrentHashMap<>();

    public long getVersion() {
        return version;
    }

    public List<Event> getEvents() {
        return events;
    }

    public int getEventsCount() {
        return events.size();
    }

    public Entity() {
        this.version = 0;
        events = new LinkedList<>();
    }

    public abstract String getId();

    /**
     * Apply the given {@link Event} to the entity. The entity id of the {@link Event} will be overwritten with the id
     * of the {@code Entity}
     * @param event the {@link Event} to be applied
     */
    public void applyEvent(Event event) {
        Method method = getApplyMethod(event);
        event.setEntityId(getId());
        try {
            method.invoke(this, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EntityException(e);
        }

        events.add(event);
    }

    private Method getApplyMethod(Event event) {
        Map<Class<?>, Method> classMethodMap = map.computeIfAbsent(this.getClass(), aClass -> buildApplyMap());
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

    private Map<Class<?>, Method> buildApplyMap() {
        Method[] methods = this.getClass().getDeclaredMethods();
        return Arrays.stream(methods).filter(method -> method.getName().equals("apply"))
                .filter(method -> method.getParameterTypes().length == 1)
                .collect(Collectors.toMap(method -> method.getParameterTypes()[0], Function.identity()));
    }

}
