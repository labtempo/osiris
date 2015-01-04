/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.common.definitions;

/**
 *
 * @author Felipe
 */
public enum LogicalOperator {

    EQUAL("=="),
    NOT_EQUAL("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN_OR_EQUAL("<=");

    private final String operator;

    private LogicalOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return operator;
    }
    
    public static LogicalOperator getByString(String operator) {
        for (LogicalOperator logicalOperator : values()) {
            if (logicalOperator.operator.equalsIgnoreCase(operator)) {
                return logicalOperator;
            }
        }
        throw new IllegalArgumentException("No enum constant " + LogicalOperator.class.getName());
    }

}
