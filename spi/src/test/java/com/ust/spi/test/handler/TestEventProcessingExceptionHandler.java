package com.ust.spi.test.handler;

import com.ust.spi.EntityEventHandler;
import com.ust.spi.ex.EventException;
import com.ust.spi.test.entity.User;
import com.ust.spi.test.event.UserCreated;

public class TestEventProcessingExceptionHandler extends EntityEventHandler<UserCreated, User> {
    @Override
    public void onEvent(UserCreated event) {
        throw new EventException("test");
    }
}
