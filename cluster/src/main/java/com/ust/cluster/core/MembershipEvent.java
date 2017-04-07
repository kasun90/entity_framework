/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.core;

/**
 *
 * @author nuwan
 */
public class MembershipEvent {

    public enum EventType {
        ADDED, UPDATED, REMOVED
    }

    private final EventType eventType;
    private final Member member;

    public MembershipEvent(EventType eventType, Member member) {
        this.eventType = eventType;
        this.member = member;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public String toString() {
        return "MembershipEvent{" + "eventType=" + eventType + ", member=" + member + '}';
    }

    
}
