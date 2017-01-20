package com.ust.spi;

import java.lang.reflect.Field;

public class Injector {

  public static <T extends EntityCommandHandler> T createInstance(Class<? extends EntityCommandHandler> cls, EntityRepository repo) throws Exception{
    Field field = cls.getSuperclass().getDeclaredField("entityRepo");
    field.setAccessible(true);
    EntityCommandHandler handler = cls.newInstance();
    field.set(handler, repo);
    return (T) handler;
  }
}
