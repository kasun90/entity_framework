package com.ust.spi;

public abstract class Event {

    private String entityId;

    protected void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityId() {
        return this.entityId;
    }
}
