package com.ust.query.operators;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.math.BigDecimal;

public class LteOpr extends BinaryOpNode {

    @Override
    public boolean eval(JsonObject json) {
        FieldNode fieldNode = left.getAs(FieldNode.class);
        JsonElement el = json.get(fieldNode.getFieldName());
        if (el == null) {
            return false;
        }
        if (el.isJsonPrimitive() && el.getAsJsonPrimitive().isNumber()) {
            BigDecimal jsonVal = el.getAsBigDecimal();
            return jsonVal.compareTo(new BigDecimal(right.getAs(LiteralNode.class).getAsString())) <= 0;
        } else {
            return el.getAsString().compareTo(right.getAs(LiteralNode.class).getAsString()) <= 0;
        }
    }

     @Override
    public String toString() {
        return left.toString() + " <= " + right.toString();
    }
}
