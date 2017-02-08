package com.ust.entitygenerator;

public final class EventApplyInfo {
    private final String eventName;
    private final String javaDoc;

    public EventApplyInfo(String eventName, String javaDoc) {
        this.eventName = eventName;
        this.javaDoc = javaDoc;
    }

    public String getEventName() {
        return eventName;
    }

    public String getJavaDoc() {
        return javaDoc;
    }
}
