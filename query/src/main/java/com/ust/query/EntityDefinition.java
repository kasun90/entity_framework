package com.ust.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityDefinition {

    private final String entityType;
    private Map<String, Class<?>> fieldMap = new HashMap<>();

    public EntityDefinition(String entityType, Map<String, Class<?>> fields) {
        this.entityType = entityType;
        this.fieldMap = fields;
    }

    public String getEntityType() {
        return entityType;
    }

    public Set<String> getFields() {
        return fieldMap.keySet();
    }

    public Class getFieldType(String name) {
        return fieldMap.get(name);
    }
}
