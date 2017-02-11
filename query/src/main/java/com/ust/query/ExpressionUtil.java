package com.ust.query;

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
import java.util.List;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexInputRef;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlKind;

public class ExpressionUtil {

    private final List<String> fieldNames;

    public ExpressionUtil(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public static QueryExp create(RexNode node, List<String> fieldNames) {
        ExpressionUtil util = new ExpressionUtil(fieldNames);
        BinaryOpNode root = util.translateOr(node);
        if (root != null) {
            return new QueryExp(root);
        } else {
            return null;
        }
    }

    private BinaryOpNode translateOr(RexNode node0) {
        BinaryOpNode root = null;
        for (RexNode node : RelOptUtil.disjunctions(node0)) {
            BinaryOpNode current = translateAnd(node);
            if (root == null) {
                root = current;
            } else {
                root = new OrOpr(root, current);
            }
        }
        return root;
    }

    private BinaryOpNode translateAnd(RexNode node0) {
        BinaryOpNode root = null;
        for (RexNode node : RelOptUtil.conjunctions(node0)) {
            BinaryOpNode current = translateMatch(node);
            if (root == null) {
                root = current;
            } else {
                root = new AndOpr(root, current);
            }
        }
        return root;
    }

    private BinaryOpNode translateMatch(RexNode node) {
        switch (node.getKind()) {
            case EQUALS: {
                return translateBinary(new EqOpr(), (RexCall) node);
            }
            case LESS_THAN:
                return translateBinary(new LtOpr(), (RexCall) node);
            case LESS_THAN_OR_EQUAL:
                return translateBinary(new LteOpr(), (RexCall) node);
            case NOT_EQUALS:
                return translateBinary(new NeqOpr(), (RexCall) node);
            case GREATER_THAN:
                return translateBinary(new GtOpr(), (RexCall) node);
            case GREATER_THAN_OR_EQUAL:
                return translateBinary(new GteOpr(), (RexCall) node);
            case OR: {
                return translateOr(node);
            }
            case AND: {
                return translateAnd(node);
            }
            default:
                throw new AssertionError("cannot translate " + node.getKind() + " - " + node);
        }
    }

    private BinaryOpNode translateBinary(BinaryOpNode node, RexCall call) {
        RexNode left = call.operands.get(0);
        RexNode right = call.operands.get(1);

        if (right.getKind() != SqlKind.LITERAL) {
            if (right.getKind() == SqlKind.CAST) {
                right = ((RexCall) right).operands.get(0);
            }
            if (right.getKind() != SqlKind.LITERAL) {
                return null;
            }
        }

        final RexLiteral rightLiteral = (RexLiteral) right;

        switch (left.getKind()) {
            case INPUT_REF: {
                final RexInputRef left1 = (RexInputRef) left;
                String name = fieldNames.get(left1.getIndex());
                node.set(new FieldNode(name), new LiteralNode(rightLiteral.getValue2()));
                return node;
            }
            case CAST: {
                final RexInputRef left1 = (RexInputRef) ((RexCall) left).operands.get(0);
                String name = fieldNames.get(left1.getIndex());
                node.set(new FieldNode(name), new LiteralNode(rightLiteral.getValue2()));
                return node;
            }
            default: {
                return null;
            }
        }
    }
}
