package com.ust.spi.test;

import com.ust.spi.Cache;
import com.ust.spi.MutableCache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link Cache} which keeps data in-memory.
 */
public final class InMemoryCache implements MutableCache {
    private Map<String, Object> data = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        return (T) data.get(key);
    }

    @Override
    public <T> void put(String key, T value) {
        data.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T remove(String key) {
        return (T) data.remove(key);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }
}
