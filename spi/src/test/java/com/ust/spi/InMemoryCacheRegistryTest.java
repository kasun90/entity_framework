package com.ust.spi;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryCacheRegistryTest {
    @Test
    public void getCache() throws Exception {
        InMemoryCacheRegistry registry = new InMemoryCacheRegistry();
        InMemoryCache cache = new InMemoryCache();
        cache.put("rest", "VAR");
        CacheRegistry.getInstance().putCache("test", cache);

        Assert.assertEquals("VAR", CacheRegistry.getInstance().getCache("test").get("rest"));
    }

    @Test
    public void removeCache() throws Exception {
        InMemoryCacheRegistry registry = new InMemoryCacheRegistry();
        InMemoryCache cache = new InMemoryCache();
        cache.put("rest", "VAR");
        CacheRegistry.getInstance().putCache("test", cache);

        Assert.assertEquals("VAR", CacheRegistry.getInstance().getCache("test").get("rest"));

        CacheRegistry.getInstance().removeCache("test");
        Assert.assertNull(CacheRegistry.getInstance().getCache("test").get("rest"));
    }

}