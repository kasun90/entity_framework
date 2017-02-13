package com.ust.query.operators;

import com.google.gson.JsonObject;

public class OrOpr extends BinaryOpNode {
    
    public OrOpr(BinaryOpNode left, BinaryOpNode right) {
        set(left, right);
    }
    
    @Override
    public boolean eval(JsonObject json) {
        boolean rVal = right.getAs(BinaryOpNode.class).eval(json);
        boolean lVal = left.getAs(BinaryOpNode.class).eval(json);
        return rVal || lVal;
    }
    
     @Override
    public String toString() {
        return "( "+left.toString() + " || " + right.toString() + " )";
    }
}
