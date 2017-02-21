package com.ust.spi.test.entity;

import com.ust.spi.MapEntity;
import com.ust.spi.test.event.UserCreated;

@SuppressWarnings("unused")
public class TestApplyExceptionMapEntity extends MapEntity<EntityApplyExceptionItem, User> {

    public TestApplyExceptionMapEntity() {
        super(EntityApplyExceptionItem.class);
    }

    @Override
    public String getId() {
        return "";
    }

    private void apply(UserCreated event) {

    }
}

