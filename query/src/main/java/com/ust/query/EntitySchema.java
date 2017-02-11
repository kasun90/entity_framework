package com.ust.query;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

public class EntitySchema extends AbstractSchema {

    private List<EntityDefinition> defList;

    public EntitySchema(List defList) {
        this.defList = defList;
    }

    @Override
    protected Map<String, Table> getTableMap() {
        final ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();
        Map<String, Table> tables = defList.stream().map(def -> new EntityTable(def))
            .collect(Collectors.toMap(entity -> entity.getDef().getEntityType(), Function.identity()));
        builder.putAll(tables);
        return builder.build();

    }
}
