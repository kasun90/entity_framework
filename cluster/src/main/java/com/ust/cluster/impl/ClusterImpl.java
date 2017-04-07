/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.impl;

import com.ust.cluster.core.Cluster;
import com.ust.cluster.core.MembershipProtocol;
import io.scalecube.transport.Transport;
import io.scalecube.transport.TransportConfig;

/**
 *
 * @author nuwan
 */
public class ClusterImpl implements Cluster{

    private final MembershipProtocol membershipProto;
    private final Transport server;
    public ClusterImpl(MembershipProtocol membershipProto) {
        this.membershipProto = membershipProto;
         TransportConfig config = TransportConfig.builder()
        .port(6000)
        .portAutoIncrement(true)
        .enableEpoll(true)
        .listenAddress("127.0.0.1")
        .build();
        server = Transport.bindAwait(config);
    }
   
    @Override
    public MembershipProtocol getMembershipProtocol() {
        return membershipProto;
    }

    @Override
    public Transport getTransport() {
       return server;
    }
    
}
