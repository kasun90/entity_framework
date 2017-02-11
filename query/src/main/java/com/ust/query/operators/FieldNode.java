package com.ust.query.operators;

public class FieldNode implements ExpNode{
    private String fieldName;

    public FieldNode(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String toString() {
        return "["+fieldName+"]";
    }
   
    
}
