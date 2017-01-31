package com.ust.spi;

/**
 * The {@code Event} represent the state changes of the {@link Entity}. Any {@code Event} belongs to an {@link Entity}
 * and represent the life cycle of that {@link Entity}
 */
public abstract class Event {

    private String entityId;

    /**
     * Sets the id of the {@link Entity} which has the ownership of this {@link Event}.
     *
     * @param entityId the id of the {@link Entity}
     */
    protected void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    /**
     * Gets the id of the {@link Entity} which has the ownership of this {@link Event}.
     *
     * @return the id of the {@link Entity}
     */
    public String getEntityId() {
        return this.entityId;
    }
}
