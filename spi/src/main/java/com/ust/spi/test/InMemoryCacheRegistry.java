package com.ust.spi.test;

import com.ust.spi.Cache;
import com.ust.spi.CacheRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The registry which contains in-memory cashes.
 */
public final class InMemoryCacheRegistry implements CacheRegistry {
    private Map<String, Cache> caches = new ConcurrentHashMap<>();

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
