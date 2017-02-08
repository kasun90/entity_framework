package com.ust.entitygenerator;

import java.util.LinkedList;
import java.util.List;

public class EnumInfo {

    private final String name;
    private String javaDoc;
    private final List<String> items = new LinkedList<>();

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
}
