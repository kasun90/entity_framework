package com.ust.spi.utils;

import com.google.gson.Gson;

/**
 * This is a common utility class of the service provider interface module.
 */
public final class SpiUtils {
    private static final ThreadLocal<Gson> context = new ThreadLocal<>();

    private SpiUtils() {

    }

    private static Gson getGson() {
        Gson gson = context.get();
        if (gson == null) {
            gson = new Gson();
            context.set(gson);
        }
        return gson;
    }

    /**
     * Convert {@link Object} to {@link String}.
     *
     * @param object the object to be converted
     * @return the string representation.
     */
    public static String getToString(Object object) {
        return getGson().toJson(object);
    }
}
