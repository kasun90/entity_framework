package com.ust.spi.test.entity;

import com.ust.spi.MapEntity;
import com.ust.spi.ex.EntityException;
import com.ust.spi.test.event.EntityExceptionCreationEvent;
import com.ust.spi.test.event.PasswordChanged;
import com.ust.spi.test.event.UserCreated;

@SuppressWarnings("unused")
public class TestMapEntity extends MapEntity<String, User> {
    @Override
    public String getId() {
        return "";
    }

    private void apply(String key, PasswordChanged event) {
        getItems().put("0", "0");
    }

    private void apply(String key, EntityExceptionCreationEvent event) {
        throw new EntityException("");
    }

    private void apply(UserCreated event) {

    }
}
