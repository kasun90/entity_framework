/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.core;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author nuwan
 * @param <T>
 */
public class CompletionFutureEx<T> extends CompletableFuture<T> {

    private String correlationID;
    
    public CompletionFutureEx() {
        correlationID  = generateId();
    }

    public String getCorrelationID() {
        return correlationID;
    }
    
    private String generateId() {
        return new UUID(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong()).toString();
    }
    
}
