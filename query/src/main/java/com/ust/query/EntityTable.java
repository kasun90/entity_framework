package com.ust.query;

import com.google.common.collect.ImmutableList;
import com.ust.query.operators.QueryExp;
import java.util.LinkedList;
import java.util.List;
import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.schema.ProjectableFilterableTable;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.Statistic;
import org.apache.calcite.schema.Statistics;

public class EntityTable implements ProjectableFilterableTable {

    private EntityDefinition def;
    private RelDataType dataTypes;
    
    final ImmutableList<Object[]> rows = ImmutableList.of(
        new Object[]{"paint", 10, "galle"},
        new Object[]{"paper", 5, "nugegoda"},
        new Object[]{"brush", 6, "colombo"},
        new Object[]{"brush", 7, "colombo"}
    );

    public EntityTable(EntityDefinition def) {
        this.def = def;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory rdtf) {
        RelDataTypeFactory.FieldInfoBuilder builder = rdtf.builder();
        def.getFields().stream().forEach(field -> builder.add(field, rdtf.createJavaType(def.getFieldType(field))));
        dataTypes = builder.build();
        return dataTypes;
    }

    private List<String> getFields() {
        List<String> fieldNames = new LinkedList<>();
        for (RelDataTypeField fieldType : dataTypes.getFieldList()) {
            fieldNames.add(fieldType.getKey());
        }
        return fieldNames;
    }

    @Override
    public Enumerable<Object[]> scan(DataContext root, List<RexNode> filters, int[] projects) {
        if (!filters.isEmpty()) {
            assert (filters.size() == 1);
            QueryExp exp = ExpressionUtil.create(filters.get(0), getFields());
            System.out.println("exp "+exp);
        }

        return Linq4j.asEnumerable(rows);
    }

    @Override
    public Statistic getStatistic() {
        return Statistics.UNKNOWN;
    }

    @Override
    public Schema.TableType getJdbcTableType() {
        return Schema.TableType.TABLE;
    }

    public EntityDefinition getDef() {
        return def;
    }

}
