package com.ust.storage.view;

import java.util.LinkedList;
import java.util.List;

public class EntityView {

    private int version;
    private int compatibility;
    private String entityType;
    private String id;
    private String entity;
    private List<String> events;
    private long eventSeq;

    public EntityView(int version, int compatibility, String entityType, String id, String entity, List<String> events) {
        this.version = version;
        this.compatibility = compatibility;
        this.entityType = entityType;
        this.id = id;
        this.entity = entity;
        this.events = events;
        this.eventSeq = 0;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public List<String> getEvents() {
        return events;
    }

    public List<String> detachEvents() {
        List<String> list = new LinkedList<>(events);
        events.clear();
        return list;
    }

    public int getCompatibility() {
        return compatibility;
    }

    public void setEventSeq(long eventSeq) {
        this.eventSeq = eventSeq;
    }

    public long getEventSeq() {
        return eventSeq;
    }

    @Override
    public String toString() {
        return "EntityView{" + "version=" + version + ", compatibility=" + compatibility + ", entityType=" + entityType + ", id=" + id + ", entity=" + entity + ", events=" + events + ", eventSeq=" + eventSeq + '}';
    }

    
}
