package com.ust.entitygenerator;

import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.Set;

/**
 * This keeps event information of the schema.
 */
final class EventInfo {
    private final String name;
    private final String javaDoc;
    private final Set<String> fields = new HashSet<>();
    private TypeSpec.Builder builder = null;
    private TypeSpec.Builder tester = null;

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
