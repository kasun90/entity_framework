package com.ust.spi.test.entity;

import com.ust.spi.ex.EntityException;
import com.ust.spi.test.event.PasswordChanged;

@SuppressWarnings("PMD")
public class EntityApplyExceptionItem {
    private void apply(PasswordChanged event) {
        throw new EntityException("test");
    }
}