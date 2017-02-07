package com.ust.spi.test.handler;

import com.ust.spi.EntityCommandHandler;
import com.ust.spi.test.UserResponse;
import com.ust.spi.test.command.PasswordResetRequest;
import com.ust.spi.test.entity.User;

public class TestExceptionHandler extends EntityCommandHandler<PasswordResetRequest, UserResponse, User> {
    public TestExceptionHandler() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @Override
    public UserResponse execute(PasswordResetRequest cmd) {
        return null;
    }
}
