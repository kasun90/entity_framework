package com.ust.spi.test.event;

import com.ust.spi.Event;

public class PasswordChanged extends Event {

    private final String password;

    public PasswordChanged(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
