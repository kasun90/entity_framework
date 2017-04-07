/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.core;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author nuwan
 */
public class CompletionFutureHandler {

    private final Map<String, CompletionFutureEx<?>> mapFutures;
    public CompletionFutureHandler() {
        mapFutures = new ConcurrentHashMap<>();
    }

    public CompletableFuture<?> create() {
        CompletionFutureEx future = new CompletionFutureEx();
        CompletionFutureEx exist = mapFutures.putIfAbsent(future.getCorrelationID(), future);
        while (exist != null) {
            future = new CompletionFutureEx();
            exist = mapFutures.putIfAbsent(future.getCorrelationID(), future);
        }
        return future;
    }

    public CompletionFutureEx<?>  get(String correlationId)
    {
        return mapFutures.get(correlationId);
    }
}
