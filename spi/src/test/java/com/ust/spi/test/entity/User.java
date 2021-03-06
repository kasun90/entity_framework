package com.ust.spi.test.entity;

import com.ust.spi.Entity;
import com.ust.spi.ex.EntityException;
import com.ust.spi.test.event.EntityExceptionCreationEvent;
import com.ust.spi.test.event.PasswordChanged;
import com.ust.spi.test.event.UserCreated;

@SuppressWarnings("unused")
public class User extends Entity {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getId() {
        return username;
    }

    private void apply(UserCreated userCreated) {
        this.username = userCreated.getUsername();
        this.password = userCreated.getPassword();
    }

    private void apply(PasswordChanged passwordChanged) {
        this.password = passwordChanged.getPassword();
    }

    private void apply(EntityExceptionCreationEvent event) {
        throw new EntityException("Error");
    }
}
