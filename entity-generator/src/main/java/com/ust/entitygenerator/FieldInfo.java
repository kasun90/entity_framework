package com.ust.entitygenerator;

public final class FieldInfo {
    private final String fieldName;
    private final String dataType;
    private final String javaDoc;
    private final ClassInfo entity;

    public FieldInfo(String fieldName, String dataType, String javaDoc, ClassInfo entity) {
        this.fieldName = fieldName;
        this.dataType = dataType;
        this.javaDoc = javaDoc;
        this.entity = entity;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public String getJavaDoc() {
        return javaDoc;
    }

    public String getGetterJavaDoc() {
        return "Gets " + javaDoc + ".\n\n@return " + fieldName + " the " + getSimpleName(entity.getClassName()) + ".\n";
    }

    private String getSimpleName(String name) {
        return name.replaceAll("([^_A-Z])([A-Z])", "$1 $2").toLowerCase();
    }
}
