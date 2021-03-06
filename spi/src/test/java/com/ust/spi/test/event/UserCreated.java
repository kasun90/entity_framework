package com.ust.spi.test.event;

import com.ust.spi.Event;

public class UserCreated extends Event {

    private final String username;
    private final String password;

    public UserCreated(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
