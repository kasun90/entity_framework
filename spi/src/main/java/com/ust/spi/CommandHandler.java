package com.ust.spi;

/**
 * The {@code CommandHandler} responsible for performing action to particular {@link Command}.
 * @param <C> the {@link Command} is the request to be executed
 * @param <R> the result type returns from the execution
 */
public interface CommandHandler<C extends Command<R>, R> {

    /**
     * Executes the given {@link Command}.
     * @param cmd the {@link Command} to be executed
     * @return the result of the execution
     */
    R execute(C cmd);
}
