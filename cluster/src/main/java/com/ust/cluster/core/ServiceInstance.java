/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.core;

import io.scalecube.transport.Message;

/**
 *
 * @author nuwan
 */
public interface ServiceInstance {
    String getName();
    long getId();
    Object invoke(Message req);
    void shutdown();
    boolean isLocal();
}
