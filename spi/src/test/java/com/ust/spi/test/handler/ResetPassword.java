package com.ust.spi.test.handler;

import com.ust.spi.EntityCommandHandler;
import com.ust.spi.test.UserResponse;
import com.ust.spi.test.command.PasswordResetRequest;
import com.ust.spi.test.entity.User;
import com.ust.spi.test.event.PasswordChanged;

public class ResetPassword extends EntityCommandHandler<PasswordResetRequest, UserResponse, User> {

    @Override
    public UserResponse execute(PasswordResetRequest cmd) {
        User user = getRepository().getEntity(cmd.getUsername());
        if (user == null) {
            return new UserResponse("user not found");
        }
        user.applyEvent(new PasswordChanged(cmd.getPassword()));
        info("test");

        return new UserResponse("");
    }

}
