package com.ust.cluster.core;

import java.util.Collection;
import java.util.Map;

public interface MembershipProtocol {

    Member addMember(String group,String memberName, Map<String, String> tags);

    void updateMember(Member member,Map<String,String> tags);
    
    void removeMember(Member member);
    
    Collection<Member> getMembers(String group);
    
    void listen(String group,MemberListener listener);
    
}
