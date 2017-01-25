package com.ust.spi.test.handler;

import com.ust.spi.EntityCommandHandler;
import com.ust.spi.ex.CommandException;
import com.ust.spi.test.UserResponse;
import com.ust.spi.test.command.PasswordResetRequest;
import com.ust.spi.test.entity.User;

public class CommandExceptionHandler extends EntityCommandHandler<PasswordResetRequest, UserResponse, User> {

    @Override
    public UserResponse execute(PasswordResetRequest cmd) {
        throw new CommandException(new CommandException("Ex"));
    }

}
