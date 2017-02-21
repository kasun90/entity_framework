package com.ust.spi.test.handler;

import com.ust.spi.EntityEventHandler;
import com.ust.spi.test.entity.User;
import com.ust.spi.test.event.UserCreated;

public class TestEventCreateExceptionHandler extends EntityEventHandler<UserCreated, User> {
    private UserCreated event;

    public TestEventCreateExceptionHandler(UserCreated event) {
        this.event = event;
    }

    @Override
    public void onEvent(UserCreated event) {
        this.event = event;
    }

    public UserCreated getEvent() {
        return event;
    }
}
