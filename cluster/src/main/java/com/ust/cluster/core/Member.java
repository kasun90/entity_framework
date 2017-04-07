package com.ust.cluster.core;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Objects;

public class Member implements Comparable<Member>{

    public static final String ATTR_ID = "id";
    public static final String ATTR_IP = "ip";
    public static final String ATTR_PORT = "port";

    private final String group;
    private final String name;
    private final Map<String, String> meta;

    public Member(String group, String name, Map<String, String> meta) {
        Preconditions.checkNotNull(group);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(meta.get(ATTR_ID));
        this.group = group;
        this.name = name;
        this.meta = meta;
    }

    public long getId()
    {
        String val = meta.get(ATTR_ID);
        return Long.valueOf(val);
    }
    
    public Object getMeta(String name) {
        return meta.get(name);
    }

    public Map<String,String> getMeta()
    {
        return meta;
    }
    
    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getIp()
    {
         return (String) meta.get(ATTR_IP);
    }
    
    public int getPort()
    {
        return Integer.valueOf(meta.get(ATTR_PORT));
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.group);
        hash = 59 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Member other = (Member) obj;
        if (!Objects.equals(this.group, other.group)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Member o) {
       int ret = this.group.compareTo(o.group);
       if(ret == 0)
           ret = this.name.compareTo(o.name);
       return ret;
    }

    @Override
    public String toString() {
        return "Member{" + "group=" + group + ", name=" + name + ", meta=" + meta + '}';
    }

    
}
