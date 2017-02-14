package com.ust.entitygenerator;

import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This keeps the class information of the schema.
 */
final class ClassInfo {
    private String className;
    private String javaDoc;
    private final List<String> parentType = new LinkedList<>();
    private final List<FieldInfo> fields = new LinkedList<>();
    private final Map<String, FieldInfo> fieldsByName = new HashMap<>();
    private final List<EventApplyInfo> eventApplyInfo = new LinkedList<>();
    boolean mapEntity = false;
    private ClassInfo itemClass = null;

    TypeSpec.Builder builder;
    TypeSpec.Builder tester;

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

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getParentType() {
        return parentType;
    }

    public void addParentType(String parentType) {
        this.parentType.add(parentType);
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

    public TypeSpec.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(TypeSpec.Builder builder) {
        this.builder = builder;
    }

    public TypeSpec.Builder getTester() {
        return tester;
    }

    public void setTester(TypeSpec.Builder tester) {
        this.tester = tester;
    }
}
