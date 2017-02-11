package com.ust.query;

import com.google.gson.JsonObject;
import com.ust.query.operators.AndOpr;
import com.ust.query.operators.BinaryOpNode;
import com.ust.query.operators.EqOpr;
import com.ust.query.operators.FieldNode;
import com.ust.query.operators.GtOpr;
import com.ust.query.operators.GteOpr;
import com.ust.query.operators.LiteralNode;
import com.ust.query.operators.LtOpr;
import com.ust.query.operators.LteOpr;
import com.ust.query.operators.NeqOpr;
import com.ust.query.operators.OrOpr;
import com.ust.query.operators.QueryExp;
import org.junit.Assert;
import org.junit.Test;

public class QueryExpTest {

    private JsonObject json;

    private QueryExpTest give_json() {
        json = new JsonObject();
        return this;
    }

    @Test
    public void exp_simple_conditional_test() {
        conditional_check("name", "=", "nuwan", true);
        conditional_check("name", "<>", "sanjeewa", true);
        conditional_check("age", ">", 29, true);
        conditional_check("age", ">=", 30, true);
        conditional_check("age", ">=", 20, true);
        conditional_check("age", "<", 31, true);
        conditional_check("age", "<=", 30, true);
        conditional_check("age", "<=", 35, true);
    }

    @Test
    public void exp_bad_field_test() {
        conditional_check("name1", "=", "nuwan", false);
        conditional_check("name1", "<>", "sanjeewa", false);
        conditional_check("age1", ">", 29, false);
        conditional_check("age1", ">=", 30, false);
        conditional_check("age1", "<", 31, false);
        conditional_check("age1", "<=", 30, false);
    }

    @Test
    public void exp_non_primitive_field_test() {
        conditional_check("city", "=", "galle", true);
        conditional_check("city", "<>", "sanjeewa", true);
        conditional_check("city", ">", "ambilipitiya", true);
        conditional_check("city", ">=", "galle", true);
        conditional_check("city", ">=", "ambilipitiya", true);
        conditional_check("city", "<", "homagama", true);
        conditional_check("city", "<=", "galle", true);
        conditional_check("city", "<=", "homagama", true);
    }

    @Test
    public void and_logical_check_test() {
        give_json().with("name", "nuwan").with("age", 30).with("city", "galle");
        QueryExp exp = new QueryExp(and(exp("name", "=", "nuwan"), exp("age", "=", 30)));
        Assert.assertTrue(exp.eval(json));
    }

    @Test
    public void or_logical_check_test() {
        give_json().with("name", "nuwan").with("age", 30).with("city", "galle");
        QueryExp exp = new QueryExp(or(exp("name", "=", "nuwan"), exp("age", "=", 12)));
        Assert.assertTrue(exp.eval(json));
    }

    private void conditional_check(String field, String op, Object value, boolean expect) {
        give_json().with("name", "nuwan").with("age", 30).with("city", "galle");
        QueryExp exp = new QueryExp(exp(field, op, value));
        Assert.assertEquals(expect, exp.eval(json));
    }

    private QueryExpTest with(String field, String value) {
        json.addProperty(field, value);
        return this;
    }

    private QueryExpTest with(String field, Number value) {
        json.addProperty(field, value);
        return this;
    }

    private QueryExpTest with(String field, Boolean value) {
        json.addProperty(field, value);
        return this;
    }

    private QueryExpTest with(String field, Character value) {
        json.addProperty(field, value);
        return this;
    }

    private BinaryOpNode and(BinaryOpNode left, BinaryOpNode right) {
        return new AndOpr(left, right);
    }

    private BinaryOpNode or(BinaryOpNode left, BinaryOpNode right) {
        return new OrOpr(left, right);
    }

    private BinaryOpNode exp(String field, String op, Object value) {
        BinaryOpNode node;
        switch (op) {
            case "=": {
                return new EqOpr().set(new FieldNode(field), new LiteralNode(value));
            }
            case "<>": {
                return new NeqOpr().set(new FieldNode(field), new LiteralNode(value));
            }
            case "<": {
                return new LtOpr().set(new FieldNode(field), new LiteralNode(value));
            }
            case ">": {
                return new GtOpr().set(new FieldNode(field), new LiteralNode(value));
            }
            case "<=": {
                return new LteOpr().set(new FieldNode(field), new LiteralNode(value));
            }
            case ">=": {
                return new GteOpr().set(new FieldNode(field), new LiteralNode(value));
            }
            default:
                return null;
        }

    }
}
