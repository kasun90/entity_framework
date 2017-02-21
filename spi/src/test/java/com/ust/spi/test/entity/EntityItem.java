package com.ust.spi.test.entity;

import com.ust.spi.test.event.PasswordChanged;

@SuppressWarnings("PMD")
public class EntityItem {

    private String name;
    private String password;

    private void apply(PasswordChanged event) {
        name = event.getEntityId();
        password = event.getPassword();
    }
}