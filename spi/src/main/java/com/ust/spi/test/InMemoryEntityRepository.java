package com.ust.spi.test;

import com.ust.spi.Entity;
import com.ust.spi.EntityRepository;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * This responsible for retrieving {@link Entity} from entity server and also act like a temporary caching layer for the
 * commands.
 *
 * @param <E> the {@link Entity} type
 */
public final class InMemoryEntityRepository<E extends Entity> extends EntityRepository<E> {
    private final Map<String, E> mapEntityCache = new ConcurrentHashMap<>();

    @Override
    public E getEntity(String... keys) {
        String key = Arrays.stream(keys).collect(Collectors.joining(":"));
        return mapEntityCache.get(key);
    }

    @Override
    public void saveEntity(E entity) {
        mapEntityCache.put(entity.getId(), entity);
    }
}
