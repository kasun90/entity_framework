package com.ust.spi;

import java.util.Objects;
import java.util.function.Function;

/**
 * This provides an interface to access data caching layer for reading and writing.
 */
public interface MutableCache extends Cache {

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.
     *
     * @param key   the key to refer the value
     * @param value the value to be stored
     * @param <T>   the value type
     */
    <T> void put(String key, T value);

    /**
     * Removes the mapping for a key from this cache if it is present and returns it.
     *
     * @param key the key to remove the value
     * @param <T> the return value type
     * @return the removed value
     */
    <T> T remove(String key);

    /**
     * Removes all of the mappings from this cache.
     * The cache will be empty after this call returns.
     */
    void clear();

    /**
     * Returns {@code true} if this cache contains a mapping for the specified key.
     *
     * @param key the key to be search
     * @return availability of that key
     */
    boolean containsKey(String key);

    /**
     * If the specified key is not already associated with a value (or is mapped
     * to {@code null}), attempts to compute its value using the given mapping
     * function and enters it into this cache unless {@code null}.
     *
     * @param key             key with which the specified value is to be associated
     * @param mappingFunction the function to compute a value
     * @param <T>             the value type
     * @return the current (existing or computed) value associated with the specified key,
     *         or null if the computed value is null
     */
    default <T> T computeIfAbsent(String key, Function<String, ? extends T> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        T object;
        if ((object = get(key)) == null) {
            T newValue;
            if ((newValue = mappingFunction.apply(key)) != null) {
                put(key, newValue);
                return newValue;
            }
        }

        return object;
    }
}
