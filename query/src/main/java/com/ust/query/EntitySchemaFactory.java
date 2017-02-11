package com.ust.query;

import com.ust.query.ex.EntitySchemaException;
import java.util.Map;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

public class EntitySchemaFactory implements SchemaFactory {

    @Override
    public Schema create(SchemaPlus sp, String string, Map<String, Object> map) {
        String loader = (String) map.get("loader");
        try {
            EntityDefinitionLoader loaderInst = (EntityDefinitionLoader) Class.forName(loader).newInstance();
            return new EntitySchema(loaderInst.getEntityDefinitions());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new EntitySchemaException(ex);
        }
    }

}
