package com.ust.query;

import com.ust.query.operators.QueryExp;
import java.util.Arrays;
import java.util.List;
import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExpressionUtilTest {

    private List<String> fields;
    private final RelDataTypeFactory typeFactory = new JavaTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
    RexBuilder rexBuilder;
    List<RelDataType> types;

    @Before
    public void setUp() {
        types = Arrays.asList(
            typeFactory.createJavaType(String.class),
            typeFactory.createJavaType(Integer.class),
            typeFactory.createJavaType(String.class)
        );
        fields = Arrays.asList("name", "age", "city");
        rexBuilder = new RexBuilder(typeFactory);
    }

    @Test
    public void single_field_eq_test() {
        RexNode node = rexCall("name", SqlStdOperatorTable.EQUALS, "nuwan");
        QueryExp exp = ExpressionUtil.create(node, fields);
        eq("[ [name] = nuwan ]",exp);
    }
    
    @Test
    public void single_field_neq_test() {
        RexNode node = rexCall("name", SqlStdOperatorTable.NOT_EQUALS, "nuwan");
        QueryExp exp = ExpressionUtil.create(node, fields);
        eq("[ [name] <> nuwan ]",exp);
    }

    @Test
    public void single_field_gt_test() {
        RexNode node = rexCall("name", SqlStdOperatorTable.GREATER_THAN, "nuwan");
        QueryExp exp = ExpressionUtil.create(node, fields);
        eq("[ [name] > nuwan ]",exp);
    }

    @Test
    public void single_field_gte_test() {
        RexNode node = rexCall("name", SqlStdOperatorTable.GREATER_THAN_OR_EQUAL, "nuwan");
        QueryExp exp = ExpressionUtil.create(node, fields);
        eq("[ [name] >= nuwan ]",exp);
    }

    @Test
    public void single_field_lt_test() {
        RexNode node = rexCall("name", SqlStdOperatorTable.LESS_THAN, "nuwan");
        QueryExp exp = ExpressionUtil.create(node, fields);
        eq("[ [name] < nuwan ]",exp);
    }

    @Test
    public void single_field_lte_test() {
        RexNode node = rexCall("name", SqlStdOperatorTable.LESS_THAN_OR_EQUAL, "nuwan");
        QueryExp exp = ExpressionUtil.create(node, fields);
        eq("[ [name] <= nuwan ]",exp);
    }

    @Test
    public void and_logical_operator_test() {
        RexNode node1 = rexCall("name", SqlStdOperatorTable.EQUALS, "nuwan");
        RexNode node2 = rexCall("age", SqlStdOperatorTable.LESS_THAN_OR_EQUAL, 30);
        RexNode node3 = rexCall(SqlStdOperatorTable.AND, node1,node2);
        QueryExp exp = ExpressionUtil.create(node3, fields);
        eq("[ ( [name] = nuwan && [age] <= 30 ) ]",exp);
    }
    
    @Test
    public void and_long_logical_operator_test() {
        RexNode node1 = rexCall("name", SqlStdOperatorTable.EQUALS, "nuwan");
        RexNode node2 = rexCall("age", SqlStdOperatorTable.LESS_THAN_OR_EQUAL, 30);
        RexNode node3 = rexCall("city", SqlStdOperatorTable.EQUALS, "galle");
        RexNode node4 = rexCall(SqlStdOperatorTable.AND, node1,node2,node3);
        QueryExp exp = ExpressionUtil.create(node4, fields);
        eq("[ ( ( [name] = nuwan && [age] <= 30 ) && [city] = galle ) ]",exp);
    }
    
    @Test
    public void or_logical_operator_test() {
        RexNode node1 = rexCall("name", SqlStdOperatorTable.EQUALS, "nuwan");
        RexNode node2 = rexCall("age", SqlStdOperatorTable.LESS_THAN_OR_EQUAL, 30);
        RexNode node3 = rexCall(SqlStdOperatorTable.OR, node1,node2);
        QueryExp exp = ExpressionUtil.create(node3, fields);
        eq("[ ( [name] = nuwan || [age] <= 30 ) ]",exp);
    }

    @Test
    public void or_long_logical_operator_test() {
        RexNode node1 = rexCall("name", SqlStdOperatorTable.EQUALS, "nuwan");
        RexNode node2 = rexCall("age", SqlStdOperatorTable.LESS_THAN_OR_EQUAL, 30);
        RexNode node3 = rexCall("city", SqlStdOperatorTable.EQUALS, "galle");
        RexNode node4 = rexCall(SqlStdOperatorTable.OR, node1,node2,node3);
        QueryExp exp = ExpressionUtil.create(node4, fields);
        eq("[ ( ( [name] = nuwan || [age] <= 30 ) || [city] = galle ) ]",exp);
    }

    
    @Test
    public void or_and_logical_operator_mix_test1() {
        RexNode node1 = rexCall("name", SqlStdOperatorTable.EQUALS, "nuwan");
        RexNode node2 = rexCall("age", SqlStdOperatorTable.LESS_THAN_OR_EQUAL, 30);
        RexNode node3 = rexCall("city", SqlStdOperatorTable.EQUALS, "galle");
        RexNode node4 = rexCall(SqlStdOperatorTable.OR, node1,node2);
        RexNode node5 = rexCall(SqlStdOperatorTable.AND, node4,node3);
        QueryExp exp = ExpressionUtil.create(node5, fields);
        eq("[ ( ( [name] = nuwan || [age] <= 30 ) && [city] = galle ) ]",exp);
    }

    @Test
    public void or_and_logical_operator_mix_test2() {
        RexNode node1 = rexCall("name", SqlStdOperatorTable.EQUALS, "nuwan");
        RexNode node2 = rexCall("age", SqlStdOperatorTable.LESS_THAN_OR_EQUAL, 30);
        RexNode node3 = rexCall("city", SqlStdOperatorTable.EQUALS, "galle");
        RexNode node4 = rexCall(SqlStdOperatorTable.AND, node2,node3);
        RexNode node5 = rexCall(SqlStdOperatorTable.OR, node1,node4);
        QueryExp exp = ExpressionUtil.create(node5, fields);
        eq("[ ( [name] = nuwan || ( [age] <= 30 && [city] = galle ) ) ]",exp);
    }
    
    public RexNode rexCall(String field, SqlOperator op, Object value) {
        int index = fields.indexOf(field);
        RexNode fieldNode = rexBuilder.makeInputRef(types.get(index), index);
        RexNode literal = rexBuilder.makeLiteral(value, types.get(index), false);
        return rexBuilder.makeCall(op, fieldNode, literal);
    }

    public RexNode rexCall(SqlOperator op, RexNode... rexNodes) {
        return rexBuilder.makeCall(op, rexNodes);
    }

    private void eq(String check,QueryExp exp) {
        System.out.println("exp : " + exp.toString());
        Assert.assertEquals(check, exp.toString());
    }
}
