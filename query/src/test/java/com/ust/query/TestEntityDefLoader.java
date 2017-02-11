package com.ust.query;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestEntityDefLoader implements EntityDefinitionLoader {

    List<EntityDefinition> defList = new LinkedList<>();

    public TestEntityDefLoader() {
        Map<String, Class<?>> fields = new HashMap<>();
        fields.put("name", String.class);
        fields.put("age", Integer.class);
         fields.put("city", String.class);
        defList.add(new EntityDefinition("Student", fields));
    }

    @Override
    public List<EntityDefinition> getEntityDefinitions() {
        return defList;
    }

}
