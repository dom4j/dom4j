// Copyright 2001 werken digital. All rights reserved.

package org.dom4j.xpath.impl;

import org.dom4j.Node;
import org.saxpath.Axis;
import org.saxpath.Operator;

public class DefaultXPathFactory implements org.jaxpath.expr.XPathFactory {
    
    public org.jaxpath.expr.XPath createXPath(org.jaxpath.expr.Expr rootExpr) {
        return new org.jaxpath.expr.DefaultXPath( rootExpr );
    }
    
    public org.jaxpath.expr.PathExpr createPathExpr( org.jaxpath.expr.FilterExpr filterExpr, org.jaxpath.expr.LocationPath locationPath) {
        return new FilterExpr( filterExpr, locationPath );
    }
    
    public org.jaxpath.expr.LocationPath createRelativeLocationPath() {
        return new LocationPath(false);
    }
    
    public org.jaxpath.expr.LocationPath createAbsoluteLocationPath() {
        return new LocationPath(true);
    }
    
    public org.jaxpath.expr.BinaryExpr createOrExpr(org.jaxpath.expr.Expr lhs, org.jaxpath.expr.Expr rhs) {
        return new BinaryExpr( Op.OR, lhs, rhs );
    }
    
    public org.jaxpath.expr.BinaryExpr createAndExpr(org.jaxpath.expr.Expr lhs, org.jaxpath.expr.Expr rhs) {
        return new BinaryExpr( Op.AND, lhs, rhs );
    }
    
    
    public org.jaxpath.expr.BinaryExpr createEqualityExpr(org.jaxpath.expr.Expr lhs, org.jaxpath.expr.Expr rhs, Operator.EqualityOperator operator) {
        if (operator.getEnumValue() == Operator.EqualityOperator.Enum.NOT_EQUALS ) {
            return new BinaryExpr( Op.NOT_EQUAL, lhs, rhs );
        }
        else {
            return new BinaryExpr( Op.EQUAL, lhs, rhs );
        }
    }
    
    
    public org.jaxpath.expr.BinaryExpr createRelationalExpr(org.jaxpath.expr.Expr lhs, org.jaxpath.expr.Expr rhs, Operator.RelationalOperator operator) {        
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
    
    public org.jaxpath.expr.BinaryExpr createAdditiveExpr(org.jaxpath.expr.Expr lhs, org.jaxpath.expr.Expr rhs, Operator.AdditiveOperator operator) {
        if (operator.getEnumValue() == Operator.AdditiveOperator.Enum.ADD ) {
            return new BinaryExpr( Op.PLUS, lhs, rhs );
        }
        else
        if (operator.getEnumValue() == Operator.AdditiveOperator.Enum.ADD ) {
            return new BinaryExpr( Op.MINUS, lhs, rhs );
        }
        return null;
    }
    
    public org.jaxpath.expr.BinaryExpr createMultiplicativeExpr(org.jaxpath.expr.Expr lhs, org.jaxpath.expr.Expr rhs, Operator.MultiplicativeOperator operator) {
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
    
    public org.jaxpath.expr.UnaryExpr createUnaryExpr(org.jaxpath.expr.Expr expr, Operator.UnaryOperator operator) {
/*        
        switch (operator.getEnumValue() ) {
            case Operator.AdditiveOperator.DIV:
                return new BinaryExpr( Op.MINUS, null, expr );
        }
        return null;
*/
        throw new RuntimeException( "UnaryExpr not supported yet" );
    }
    
    public org.jaxpath.expr.UnionExpr createUnionExpr(org.jaxpath.expr.Expr lhs, org.jaxpath.expr.Expr rhs) {
        return new UnionExpr( (Expr) lhs, (Expr) rhs );
    }
    
    public org.jaxpath.expr.FilterExpr createFilterExpr(org.jaxpath.expr.Expr expr) {
        return new FilterExpr( (Expr) expr );
    }
    
    public org.jaxpath.expr.FunctionCallExpr createFunctionCallExpr(String functionName) {
        return new FunctionExpr( functionName );
    }
    
    public org.jaxpath.expr.NumberExpr createNumberExpr(int number) {
        return new NumberExpr( new Integer(number) );
    }
    
    public org.jaxpath.expr.NumberExpr createNumberExpr(double number) {
        return new NumberExpr( new Double(number) );
    }
    
    public org.jaxpath.expr.LiteralExpr createLiteralExpr(String literal) {
        return new StringExpr( literal );
    }
    
    public org.jaxpath.expr.VariableReferenceExpr createVariableReferenceExpr(String variable) {
        return new VariableExpr( variable );
    }
    
    public org.jaxpath.expr.Step createNameStep(Axis axis, String prefix, String localName) {
        return new NameTestStep( axis, prefix, localName );
    }
    
    public org.jaxpath.expr.Step createTextNodeStep(Axis axis) {
        //return new TextNodeStep( axis );
        return new NodeTypeStep( Node.TEXT_NODE, axis );
    }
    
    public org.jaxpath.expr.Step createCommentNodeStep(Axis axis) {
        //return new CommentNodeStep( axis );
        return new NodeTypeStep( Node.COMMENT_NODE, axis );
    }
    
    public org.jaxpath.expr.Step createAllNodeStep(Axis axis) {
        //return new AllNodeStep( axis );
        return new NodeTypeStep( Node.ANY_NODE, axis );
    }
    
    public org.jaxpath.expr.Step createProcessingInstructionNodeStep(Axis axis, String piName) {
        return new PIStep( axis, piName );
    }
    
    public org.jaxpath.expr.Predicate createPredicate(org.jaxpath.expr.Expr predicateExpr) {
        return new Predicate( (Expr) predicateExpr );
    }
    
}
