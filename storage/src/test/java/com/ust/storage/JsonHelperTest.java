package com.ust.storage;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.ust.storage.query.JsonHelper;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("PMD")
public class JsonHelperTest {

    @Test
    public void test_eq() {
        Assert.assertEquals(true, JsonHelper.eq(null, null));
        Assert.assertEquals(false, JsonHelper.eq(null, "test"));
        Assert.assertEquals(false, JsonHelper.eq(new JsonPrimitive(true), null));
        Assert.assertEquals(false, JsonHelper.eq(JsonNull.INSTANCE, "test"));
        Assert.assertEquals(true, JsonHelper.eq(given_json("test", "value1"), given_json("test", "value1")));
    }

    @Test
    public void test_compare() {
        Assert.assertEquals(0, JsonHelper.compare(null, null));
        Assert.assertEquals(-1, JsonHelper.compare(null, new JsonPrimitive("hello")));
        Assert.assertEquals(1, JsonHelper.compare(new JsonPrimitive("hello"), null));
        Assert.assertEquals(0, JsonHelper.compare(JsonNull.INSTANCE, JsonNull.INSTANCE));
        Assert.assertEquals(-1, JsonHelper.compare(JsonNull.INSTANCE, new JsonPrimitive("hello")));
        Assert.assertEquals(1, JsonHelper.compare(new JsonPrimitive("hello"),JsonNull.INSTANCE));
        Assert.assertEquals(0, JsonHelper.compare(given_json("test", "value1"), given_json("test", "value1")));
    }

    private JsonObject given_json(String field, String value) {
        JsonObject json = new JsonObject();
        json.addProperty(field, value);
        return json;
    }
}
