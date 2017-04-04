package com.ust.spi;

/**
 * This provides an interface to access data caching layer for reading only.
 */
public interface Cache {

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this cache contains no mapping for the key.
     *
     * @param key the key of the value
     * @param <T> the return value type
     * @return the value referred by the key
     */
    <T> T get(String key);
}
