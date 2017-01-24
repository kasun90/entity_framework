package com.ust.spi;

public abstract class Event {

  private String entityID;

  protected void setEntityID(String id) {
    this.entityID = id;
  }

  public String getEntityID() {
    return this.entityID;
  }
}
