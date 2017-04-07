package com.ust.cluster.core;

import java.util.HashMap;
import java.util.Map;

public class AttributeMap{

    private final Map<String, String> map;

    public AttributeMap(Map<String, String> map) {
        this.map = map;
    }

    public Map<String,String> map()
    {
        return map;
    }
    
    public static Builder builder()
    {
        return new Builder();
    }
    
    public static final class Builder {

        private final Map<String, String> map;

        public Builder() {
            map = new HashMap<>();
        }

        public Builder attr(String attribute, Object value) {
            map.put(attribute, String.valueOf(value));
            return this;
        }
        
        public Builder attrMap(Map<String,String> attr)
        {
            map.putAll(attr);
            return this;
        }
        
        public AttributeMap build()
        {
            return new AttributeMap(map);
        }
    }
}
