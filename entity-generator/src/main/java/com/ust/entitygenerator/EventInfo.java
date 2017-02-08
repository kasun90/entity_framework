package com.ust.entitygenerator;

import java.util.HashSet;
import java.util.Set;

public final class EventInfo {
    private final String name;
    private final String javaDoc;
    private Set<String> fields = new HashSet<>();

    public EventInfo(String name, String javaDoc) {
        this.name = name;
        this.javaDoc = javaDoc;
    }

    public String getJavaDoc() {
        return javaDoc;
    }

    public String getName() {
        return name;
    }

    public Set<String> getFields() {
        return fields;
    }
}
