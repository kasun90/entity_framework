package com.ust.spi.test.entity;

import com.ust.spi.MapEntity;
import com.ust.spi.test.event.UserCreated;

@SuppressWarnings("unused")
public class TestItemCreateExceptionMapEntity extends MapEntity<EntityItemCreateExceptionItem, User> {

    public TestItemCreateExceptionMapEntity() {
        super(EntityItemCreateExceptionItem.class);
    }

    @Override
    public String getId() {
        return "";
    }

    private void apply(UserCreated event) {

    }
}

