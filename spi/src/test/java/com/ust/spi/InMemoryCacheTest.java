package com.ust.spi;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryCacheTest {
    @Test
    public void get() throws Exception {
        InMemoryCache cache = new InMemoryCache();
        cache.put("test", "Best");
        cache.put("duck", 10);

        Assert.assertEquals("Best", cache.get("test"));
        Assert.assertEquals(new Integer(10), cache.get("duck"));
    }

    @Test
    public void remove() throws Exception {
        InMemoryCache cache = new InMemoryCache();
        cache.put("test", "Best");
        cache.put("duck", 10);

        Assert.assertTrue(cache.containsKey("test"));
        Assert.assertEquals(new Integer(10), cache.get("duck"));
        cache.remove("test");
        Assert.assertFalse(cache.containsKey("test"));
    }

    @Test
    public void clear() throws Exception {
        InMemoryCache cache = new InMemoryCache();
        cache.put("test", "Best");
        cache.put("duck", 10);

        Assert.assertTrue(cache.containsKey("test"));
        Assert.assertTrue(cache.containsKey("duck"));
        cache.clear();
        Assert.assertFalse(cache.containsKey("test"));
        Assert.assertFalse(cache.containsKey("duck"));
    }

    @Test
    public void containsKey() throws Exception {
        InMemoryCache cache = new InMemoryCache();
        cache.put("test", "Best");
        cache.put("duck", 10);

        Assert.assertTrue(cache.containsKey("test"));
        Assert.assertFalse(cache.containsKey("best"));
    }

    @Test
    public void computeIfAbsent() throws Exception {
        InMemoryCache cache = new InMemoryCache();
        cache.put("test", "Best");
        cache.put("duck", 10);

        Assert.assertTrue(cache.containsKey("test"));
        Assert.assertFalse(cache.containsKey("rest"));

        Assert.assertEquals("GO", cache.computeIfAbsent("rest", s -> "GO"));
        Assert.assertTrue(cache.containsKey("rest"));

        Assert.assertEquals("GO", cache.computeIfAbsent("rest", s -> "BO"));
    }
}