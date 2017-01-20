package com.ust.spi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @param <E>
 */
public abstract class Entity<E extends Entity> {

  private long version;
  private List<Event> events;
  private static final Map<Class<?>, Map<Class<?>, Method>> map = new ConcurrentHashMap<>();

  public long getVersion() {
    return version;
  }

  public List<Event> getEvents() {
    return events;
  }

  public Entity() {
    this.version = 0;
    events = new LinkedList<>();
  }

  public abstract String getID();

  public void applyEvent(Event event) {
    events.add(event);
    Method method = getApplyMethod(event);
    try {
      method.invoke(this, event);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
    event.setEntityID(getID());
  }

  private Method getApplyMethod(Event event) {
    Map<Class<?>, Method> classMethodMap = map.computeIfAbsent(this.getClass(), aClass -> buildApplyMap());
    Method method = classMethodMap.get(event.getClass());
    if (method == null) {
      throw new RuntimeException("No matching event apply method found. [Entity="
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
        .collect(Collectors.toMap(method -> method.getParameterTypes()[0], Function.identity()));
  }

}