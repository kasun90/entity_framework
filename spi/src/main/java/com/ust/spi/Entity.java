package com.ust.spi;

import com.ust.spi.ex.EntityException;
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

  public abstract String getID();

  public void applyEvent(Event event) {
    Method method = getApplyMethod(event);
      event.setEntityID(getID());
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
    return Arrays.stream(methods).filter(method -> method.getName().equals("apply")).filter(method -> method.getParameterTypes().length == 1)
        .collect(Collectors.toMap(method -> method.getParameterTypes()[0], Function.identity()));
  }

}
