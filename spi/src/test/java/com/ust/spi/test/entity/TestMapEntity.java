package com.ust.spi.test.entity;

import com.ust.spi.MapEntity;
import com.ust.spi.test.event.UserCreated;

@SuppressWarnings("unused")
public class TestMapEntity extends MapEntity<EntityItem, User> {

    public TestMapEntity() {
        super(EntityItem.class);
    }

    @Override
    public String getId() {
        return "";
    }

    private void apply(UserCreated event) {

    }
}

