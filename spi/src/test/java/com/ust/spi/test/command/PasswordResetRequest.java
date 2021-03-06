package com.ust.spi.test.command;

import com.ust.spi.Command;
import com.ust.spi.test.UserResponse;

public class PasswordResetRequest implements Command<UserResponse> {

    private final String username;
    private final String password;

    public PasswordResetRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getKey() {
        return username;
    }

}
