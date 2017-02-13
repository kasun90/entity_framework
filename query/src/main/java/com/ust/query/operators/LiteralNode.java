package com.ust.query.operators;

public class LiteralNode implements ExpNode{
    private final Object value;
    public LiteralNode(Object value)
    {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
    
    public String  getAsString()
    {
        return String.valueOf(value);
    }

    @Override
    public String toString() {
        return getAsString();
    }
    
    
    
}
