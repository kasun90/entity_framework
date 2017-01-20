package com.ust.spi;

public abstract class Event {

  private String entityID;

 
  protected void setEntityID(String entityID) {
    this.entityID = entityID;
  }
  
  public String getEntityID() {
    return this.entityID;
  }
}
