package com.ust.query.operators;

import com.google.gson.JsonObject;

public class QueryExp implements ExpNode{
    private BinaryOpNode root;

    public QueryExp(BinaryOpNode root) {
        this.root = root;
    }
   
    
    public boolean eval(JsonObject json)
    {
        return root.eval(json);
    }
    
     @Override
    public String toString() {
        return "[ "+root.toString()+" ]";
    }
}
