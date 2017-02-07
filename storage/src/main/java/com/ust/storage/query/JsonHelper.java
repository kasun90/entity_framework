package com.ust.storage.query;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonHelper {

    public static Stream<JsonObject> stream(Iterator<JsonObject> jsonIterator) {
        Iterable<JsonObject> iterable = () -> jsonIterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }
    
    public static  boolean eq(JsonElement el, Object value) {
        if (el == null && value == null) {
            return true;
        }
        if (el == null || value == null) {
            return false;
        }

        if (el.isJsonNull()) {
            return false;
        } else if (el.isJsonPrimitive()) {
            return el.getAsString().equals(String.valueOf(value));
        } else if (el.isJsonObject() && (value instanceof JsonObject)) {
                return el.getAsJsonObject().toString().equals(((JsonObject) value).toString());
        }
        return false;
    }
    
     public static int compare(JsonElement left, JsonElement right) {
        if (left == null && right == null) {
            return 0;
        }
        if (left == null) {
            return -1;
        }
        if (right == null) {
            return 1;
        }

        if (left.isJsonNull() && right.isJsonNull()) {
            return 0;
        }

        if (left.isJsonNull()) {
            return -1;
        }
        if (right.isJsonNull()) {
            return 1;
        }

        if (left.isJsonPrimitive() && right.isJsonPrimitive()) {
            if (left.getAsJsonPrimitive().isNumber() && right.getAsJsonPrimitive().isNumber()) {
                return new BigDecimal(left.getAsString()).compareTo(new BigDecimal(right.getAsString()));
            } else {
                return left.getAsString().compareTo(right.getAsString());
            }
        } else {
            return  left.toString().compareTo(right.toString());
        }
    }
}
