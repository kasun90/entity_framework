package com.ust.query.operators;

import com.google.gson.JsonObject;

public abstract class BinaryOpNode implements ExpNode {

    ExpNode left;
    ExpNode right;

    public abstract boolean eval(JsonObject json);

    public final BinaryOpNode set(ExpNode left, ExpNode right) {
        this.left = left;
        this.right = right;
        return this;
    }
}
