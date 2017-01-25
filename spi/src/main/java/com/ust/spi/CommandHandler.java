package com.ust.spi;

/**
 * The {@code CommandHandler} responsible for performing action to particular {@link Command}.
 * @param <C> the {@link Command} is the request to be executed
 * @param <R> the result type returns from the execution
 */
public interface CommandHandler<C extends Command<R>, R> {

    R execute(C cmd);
}
