package com.ust.entitygenerator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class ClassInfo {
    private String packageName = "";
    private String className;
    private String javaDoc;
    private List<String> parentType = new LinkedList<>();
    private List<FieldInfo> fields = new LinkedList<>();
    private Map<String, FieldInfo> fieldsByName = new HashMap<>();
    private List<EventApplyInfo> eventApplyInfo = new LinkedList<>();
    boolean mapEntity = false;
    private ClassInfo itemClass = null;

    public ClassInfo() {

    }

    public boolean isMapEntity() {
        return mapEntity;
    }

    public ClassInfo getItemClass() {
        return itemClass;
    }

    public void setItemClass(ClassInfo itemClass) {
        this.itemClass = itemClass;
    }

    public void setMapEntity(boolean mapEntity) {
        this.mapEntity = mapEntity;
    }

    public String getJavaDoc() {
        return javaDoc;
    }

    public void setJavaDoc(String javaDoc) {
        this.javaDoc = javaDoc;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getParentType() {
        return parentType;
    }

    public void addParentType(String parentType) {
        this.parentType .add(parentType);
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public void addField(FieldInfo fieldInfo) {
        fields.add(fieldInfo);
        fieldsByName.put(fieldInfo.getFieldName(), fieldInfo);
    }

    public FieldInfo getFieldInfo(String name) {
        return fieldsByName.get(name);
    }

    public List<EventApplyInfo> getEventApplyInfo() {
        return eventApplyInfo;
    }
}