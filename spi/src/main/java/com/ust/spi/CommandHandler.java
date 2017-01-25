package com.ust.spi;


public interface CommandHandler<C extends Command<R>, R> {

    R execute(C cmd);
}
