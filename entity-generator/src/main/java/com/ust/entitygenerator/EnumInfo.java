package com.ust.entitygenerator;

import com.squareup.javapoet.TypeSpec;

import java.util.LinkedList;
import java.util.List;

/**
 * This keeps the enumeration information of the schema.
 */
final class EnumInfo {

    private final String name;
    private String javaDoc;
    private final List<String> items = new LinkedList<>();
    TypeSpec.Builder builder;

    public EnumInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getItems() {
        return items;
    }

    public String getJavaDoc() {
        return javaDoc;
    }

    public void setJavaDoc(String javaDoc) {
        this.javaDoc = javaDoc;
    }

    public TypeSpec.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(TypeSpec.Builder builder) {
        this.builder = builder;
    }
}
