// Copyright 2001 werken digital. All rights reserved.

package org.dom4j.xpath.impl;

import org.dom4j.Node;
import org.saxpath.Axis;
import org.saxpath.Operator;

public class DefaultXPathFactory implements org.jaxen.expr.XPathFactory {
    
    public org.jaxen.expr.XPath createXPath(org.jaxen.expr.Expr rootExpr) {
        return new org.jaxen.expr.DefaultXPath( rootExpr );
    }
    
    public org.jaxen.expr.PathExpr createPathExpr( org.jaxen.expr.FilterExpr filterExpr, org.jaxen.expr.LocationPath locationPath) {
        return new FilterExpr( filterExpr, locationPath );
    }
    
    public org.jaxen.expr.LocationPath createRelativeLocationPath() {
        return new LocationPath(false);
    }
    
    public org.jaxen.expr.LocationPath createAbsoluteLocationPath() {
        return new LocationPath(true);
    }
    
    public org.jaxen.expr.BinaryExpr createOrExpr(org.jaxen.expr.Expr lhs, org.jaxen.expr.Expr rhs) {
        return new BinaryExpr( Op.OR, lhs, rhs );
    }
    
    public org.jaxen.expr.BinaryExpr createAndExpr(org.jaxen.expr.Expr lhs, org.jaxen.expr.Expr rhs) {
        return new BinaryExpr( Op.AND, lhs, rhs );
    }
    
    
    public org.jaxen.expr.BinaryExpr createEqualityExpr(org.jaxen.expr.Expr lhs, org.jaxen.expr.Expr rhs, Operator.EqualityOperator operator) {
        if (operator.getEnumValue() == Operator.EqualityOperator.Enum.NOT_EQUALS ) {
            return new BinaryExpr( Op.NOT_EQUAL, lhs, rhs );
        }
        else {
            return new BinaryExpr( Op.EQUAL, lhs, rhs );
        }
    }
    
    
    public org.jaxen.expr.BinaryExpr createRelationalExpr(org.jaxen.expr.Expr lhs, org.jaxen.expr.Expr rhs, Operator.RelationalOperator operator) {        
        if (operator.getEnumValue() == Operator.RelationalOperator.Enum.GREATER_THAN ) {
            return new BinaryExpr( Op.GT, lhs, rhs );
        }
        else
        if (operator.getEnumValue() == Operator.RelationalOperator.Enum.GREATER_THAN_EQUALS ) {
            return new BinaryExpr( Op.GT_EQUAL, lhs, rhs );
        }
        else
        if (operator.getEnumValue() == Operator.RelationalOperator.Enum.LESS_THAN ) {
            return new BinaryExpr( Op.LT, lhs, rhs );
        }
        else
        if (operator.getEnumValue() == Operator.RelationalOperator.Enum.LESS_THAN_EQUALS ) {
            return new BinaryExpr( Op.LT_EQUAL, lhs, rhs );
        }
        return null;
    }
    
    public org.jaxen.expr.BinaryExpr createAdditiveExpr(org.jaxen.expr.Expr lhs, org.jaxen.expr.Expr rhs, Operator.AdditiveOperator operator) {
        if (operator.getEnumValue() == Operator.AdditiveOperator.Enum.ADD ) {
            return new BinaryExpr( Op.PLUS, lhs, rhs );
        }
        else
        if (operator.getEnumValue() == Operator.AdditiveOperator.Enum.ADD ) {
            return new BinaryExpr( Op.MINUS, lhs, rhs );
        }
        return null;
    }
    
    public org.jaxen.expr.BinaryExpr createMultiplicativeExpr(org.jaxen.expr.Expr lhs, org.jaxen.expr.Expr rhs, Operator.MultiplicativeOperator operator) {
        if (operator.getEnumValue() == Operator.MultiplicativeOperator.Enum.DIV ) {
            return new BinaryExpr( Op.DIV, lhs, rhs );
        }
        else
        if (operator.getEnumValue() == Operator.MultiplicativeOperator.Enum.MOD ) {
            return new BinaryExpr( Op.MOD, lhs, rhs );
        }
        else
        if (operator.getEnumValue() == Operator.MultiplicativeOperator.Enum.MULTIPLY ) {
            return new BinaryExpr( Op.MULTIPLY, lhs, rhs );
        }
        return null;
    }
    
    public org.jaxen.expr.UnaryExpr createUnaryExpr(org.jaxen.expr.Expr expr, Operator.UnaryOperator operator) {
/*        
        switch (operator.getEnumValue() ) {
            case Operator.AdditiveOperator.DIV:
                return new BinaryExpr( Op.MINUS, null, expr );
        }
        return null;
*/
        throw new RuntimeException( "UnaryExpr not supported yet" );
    }
    
    public org.jaxen.expr.UnionExpr createUnionExpr(org.jaxen.expr.Expr lhs, org.jaxen.expr.Expr rhs) {
        return new UnionExpr( (Expr) lhs, (Expr) rhs );
    }
    
    public org.jaxen.expr.FilterExpr createFilterExpr(org.jaxen.expr.Expr expr) {
        return new FilterExpr( (Expr) expr );
    }
    
    public org.jaxen.expr.FunctionCallExpr createFunctionCallExpr(String functionName) {
        return new FunctionExpr( functionName );
    }
    
    public org.jaxen.expr.NumberExpr createNumberExpr(int number) {
        return new NumberExpr( new Integer(number) );
    }
    
    public org.jaxen.expr.NumberExpr createNumberExpr(double number) {
        return new NumberExpr( new Double(number) );
    }
    
    public org.jaxen.expr.LiteralExpr createLiteralExpr(String literal) {
        return new StringExpr( literal );
    }
    
    public org.jaxen.expr.VariableReferenceExpr createVariableReferenceExpr(String variable) {
        return new VariableExpr( variable );
    }
    
    public org.jaxen.expr.Step createNameStep(Axis axis, String prefix, String localName) {
        return new NameTestStep( axis, prefix, localName );
    }
    
    public org.jaxen.expr.Step createTextNodeStep(Axis axis) {
        //return new TextNodeStep( axis );
        return new NodeTypeStep( Node.TEXT_NODE, axis );
    }
    
    public org.jaxen.expr.Step createCommentNodeStep(Axis axis) {
        //return new CommentNodeStep( axis );
        return new NodeTypeStep( Node.COMMENT_NODE, axis );
    }
    
    public org.jaxen.expr.Step createAllNodeStep(Axis axis) {
        //return new AllNodeStep( axis );
        return new NodeTypeStep( Node.ANY_NODE, axis );
    }
    
    public org.jaxen.expr.Step createProcessingInstructionNodeStep(Axis axis, String piName) {
        return new PIStep( axis, piName );
    }
    
    public org.jaxen.expr.Predicate createPredicate(org.jaxen.expr.Expr predicateExpr) {
        return new Predicate( (Expr) predicateExpr );
    }
    
}
