package com.ust.spi.test;

import com.ust.spi.test.event.PasswordChanged;

public class EntityItem {

    String name;
    String password;

    private void apply(PasswordChanged event) {
        name = event.getEntityId();
        password = event.getPassword();
    }
}