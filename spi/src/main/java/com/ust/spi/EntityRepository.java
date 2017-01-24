package com.ust.spi;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EntityRepository<E extends Entity> implements EntityViewRepository<E> {

  private final Map<String, E> mapEntityCache = new ConcurrentHashMap<>();

  @Override
  public E getEntity(String... keys) throws Exception {
    String key = Arrays.stream(keys).collect(Collectors.joining(":"));
    return mapEntityCache.get(key);
  }

  public void saveEntity(E entity) throws Exception {
    mapEntityCache.put(entity.getID(), entity);
  }
}
