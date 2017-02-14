package com.ust.entitygenerator;

/**
 * This keeps the event application information of the schema entity.
 */
final class EventApplyInfo {
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
