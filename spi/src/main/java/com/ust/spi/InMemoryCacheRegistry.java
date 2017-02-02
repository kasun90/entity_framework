package com.ust.spi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The registry which contains in-memory cashes.
 */
public final class InMemoryCacheRegistry implements CacheRegistry {
    Map<String, Cache> caches = new ConcurrentHashMap<>();

    public InMemoryCacheRegistry() {
        CacheRegistry.instanceData.setInstance(this);
    }

    @Override
    public Cache getCache(String id) {
        return caches.computeIfAbsent(id, k -> new InMemoryCache());
    }

    @Override
    public void putCache(String id, Cache cache) {
        caches.put(id, cache);
    }

    @Override
    public void removeCache(String id) {
        caches.remove(id);
    }
}
