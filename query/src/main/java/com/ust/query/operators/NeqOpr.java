package com.ust.query.operators;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class NeqOpr  extends BinaryOpNode{

    @Override
    public boolean eval(JsonObject json) {
        FieldNode fieldNode = left.getAs(FieldNode.class);
        JsonElement el = json.get(fieldNode.getFieldName());
        if(el == null)
            return false;
        return !(el.getAsString().equals(right.getAs(LiteralNode.class).getAsString()));
    }
    
     @Override
    public String toString() {
        return left.toString() + " <> " + right.toString();
    }
}
