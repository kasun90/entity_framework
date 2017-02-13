package com.ust.query.operators;

public enum Opertor {
    EQ("="),
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<=");

    String op;

    private Opertor(String op) {
        this.op = op;
    }
    
}
