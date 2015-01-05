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
public enum ValueType {

    NUMBER("number"),
    LOGIC("logic"),
    TEXT("text");
    
    private final String type;
    
    private ValueType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
    
    public static ValueType getByCode(String type) {
        for (ValueType valueType : values()) {
            if (valueType.type.equalsIgnoreCase(type)) {
                return valueType;
            }
        }
        throw new IllegalArgumentException("No enum constant " + ValueType.class.getName());
    }
}
