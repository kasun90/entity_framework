package com.ust.spi;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * This responsible for retrieving {@link Entity} from entity server and also act like a temporary caching layer for the
 * commands.
 * @param <E> the {@link Entity} type
 */
public class EntityRepository<E extends Entity> implements EntityViewRepository<E> {

    private final Map<String, E> mapEntityCache = new ConcurrentHashMap<>();

    /**
     * Gets the {@link Entity} by keys.
     * @param keys keys to get the {@link Entity}
     * @return the {@link Entity} represented by the keys
     */
    @Override
    public E getEntity(String... keys) {
        String key = Arrays.stream(keys).collect(Collectors.joining(":"));
        return mapEntityCache.get(key);
    }

    /**
     * Sage the changes to the {@link Entity} server.
     * @param entity the {@link Entity} to be saved
     */
    public void saveEntity(E entity) {
        mapEntityCache.put(entity.getId(), entity);
    }
}
