/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.spi;

import java.lang.reflect.Field;

/**
 *
 * @author nuwan
 */
public class Injector {

  public static <T extends EntityCommandHandler> T createInstance(Class<? extends EntityCommandHandler> cls, EntityRepository repo) throws Exception{
    Field field = cls.getSuperclass().getDeclaredField("entityRepo");
    field.setAccessible(true);
    EntityCommandHandler handler = cls.newInstance();
    field.set(handler, repo);
    return (T) handler;
  }
}
