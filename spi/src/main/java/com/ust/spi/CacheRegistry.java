package com.ust.spi;

/**
 * The {@link CacheRegistry} provides an interface to keep and manage caches.
 */
public interface CacheRegistry {
    /**
     * Returns the cache to which the specified key is mapped,
     * or {@code null} if this registry contains no mapping for the key.
     *
     * @param key the key of the cache
     * @return the cache referred by the key
     */
    Cache getCache(String key);

    /**
     * Associates the specified cache with the specified key in this registry.
     *
     * @param key   the key to refer the value
     * @param cache the value to be stored
     */
    void putCache(String key, Cache cache);

    /**
     * Removes the mapping for a key from this cache if it is present and returns it.
     *
     * @param key the key to remove the value
     */
    void removeCache(String key);

}
