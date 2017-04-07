/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.core;

import io.scalecube.transport.Transport;

/**
 *
 * @author nuwan
 */
public interface Cluster {
    MembershipProtocol getMembershipProtocol();
    Transport getTransport();
}
