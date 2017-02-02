package com.ust.storage.view;

public class EventView {
    String event;
    String changeBy;
    long txTime;
    long businessDate;

    public EventView(String event, String changeBy, long txTime, long businessDate) {
        this.event = event;
        this.changeBy = changeBy;
        this.txTime = txTime;
        this.businessDate = businessDate;
    }

    public String getEvent() {
        return event;
    }

    public long getTxTime() {
        return txTime;
    }

    public long getBusinessDate() {
        return businessDate;
    }

    public String getChangeBy() {
        return changeBy;
    }

    @Override
    public String toString() {
        return "EventView{" + "event=" + event + ", changeBy=" + changeBy + ", txTime=" + txTime + ", businessDate=" + businessDate + '}';
    }
    
    
}
