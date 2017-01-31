package com.ust.spi;

/**
 * The {@code Command} holds the details required to execute an {@link Entity} related operation. The command always
 * changes the state of the {@link Entity}
 *
 * @param <R> the related {@link Entity} type
 */
public interface Command<R> {

    /**
     * Gets the key of the {@link Command}.
     *
     * @return the key
     */
    String getKey();
}
