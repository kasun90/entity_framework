package com.ust.query.operators;

import java.io.Serializable;

public interface ExpNode extends Serializable {

    default <T> T getAs(Class<T> cls) {
        return (T) this;
    }
}
