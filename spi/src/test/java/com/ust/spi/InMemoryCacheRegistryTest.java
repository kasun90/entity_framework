package com.ust.spi;

import com.ust.spi.test.InMemoryCache;
import com.ust.spi.test.InMemoryCacheRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InMemoryCacheRegistryTest {
    @Test
    public void getCache() throws Exception {
        InMemoryCacheRegistry registry = new InMemoryCacheRegistry();
        InMemoryCache cache = new InMemoryCache();
        cache.put("rest", "VAR");
        registry.putCache("test", cache);

        assertEquals("VAR", registry.getCache("test").get("rest"));
    }

    @Test
    public void removeCache() throws Exception {
        InMemoryCacheRegistry registry = new InMemoryCacheRegistry();
        InMemoryCache cache = new InMemoryCache();
        cache.put("rest", "VAR");
        registry.putCache("test", cache);

        assertEquals("VAR", registry.getCache("test").get("rest"));

        registry.removeCache("test");
        assertNull(registry.getCache("test").get("rest"));
    }

}