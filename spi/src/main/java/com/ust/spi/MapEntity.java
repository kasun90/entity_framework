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
import java.util.stream.Stream;

/**
 * A {@code MapEntity} is same as the {@link Entity}. But it is dynamic in nature where you can give key value pair as
 * an item.
 *
 * @param <I> the value type of the map
 * @param <E> the type of parent entity
 */
public abstract class MapEntity<I, E extends Entity> extends Entity<E> {
    private static final Map<Class<?>, Map<Class<?>, Method>> maps = new ConcurrentHashMap<>();

    private final Map<String, I> items = new HashMap<>();
    private final Class<I> itemClazz;

    public MapEntity(Class<I> itemClazz) {
        this.itemClazz = itemClazz;
    }


    /**
     * Gets the items map in this entity.
     *
     * @return the map of items
     */
    public Map<String, I> getItems() {
        return items;
    }

    /**
     * Returns a sequential {@code Stream} with this collection as its source.
     *
     * @return a sequential {@code Stream} over the elements in this collection
     */
    public Stream<I> stream() {
        return items.values().stream();
    }

    /**
     * Apply the given {@link Event} to a item in the map of the entity. The entity id of the {@link Event} will be
     * overwritten with the id
     *
     * @param key   the key of the item contains in the map
     * @param event the {@link Event} to be applied
     */
    public void applyEvent(String key, Event event) {
        event.setEntityId(getId());
        I item = items.computeIfAbsent(key, k -> createInstance());
        Method method = getApplyMethod(event);
        try {
            method.invoke(item, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EntityException(e);
        }
        events.add(event);
    }

    private I createInstance() {
        try {
            return itemClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new EntityException(ex);
        }
    }

    private Method getApplyMethod(Event event) {
        Map<Class<?>, Method> classMethodMap = maps.computeIfAbsent(itemClazz, aClass -> buildApplyMaps(itemClazz));
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

    private Map<Class<?>, Method> buildApplyMaps(Class<I> itemClazz) {
        Method[] methods = itemClazz.getDeclaredMethods();
        return Arrays.stream(methods).filter(method -> method.getName().equals("apply"))
                .filter(method -> method.getParameterTypes().length == 1)
                .collect(Collectors.toMap(method -> method.getParameterTypes()[0], Function.identity()));
    }


}
