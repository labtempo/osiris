/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.virtualsensornet;

import br.uff.labtempo.osiris.to.common.definitions.State;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;

/**
 *
 * @author Felipe
 */
public class DataTypeVsnTo {

    private long id;
    private final String displayName;
    private final String type;
    private final String unit;
    private final String symbol;
    
    //helper attributes
    private transient ValueType helperValueType;

    public DataTypeVsnTo(String displayName, ValueType valueType, String unit, String symbol) {
        this.displayName = displayName;
        this.type = valueType.toString();
        this.unit = unit;
        this.symbol = symbol;
        
        this.helperValueType = valueType;
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ValueType getType() {
        if (helperValueType == null) {
            helperValueType = Enum.valueOf(ValueType.class, type);
        }
        return helperValueType;
    }

    public String getUnit() {
        return unit;
    }

    public String getSymbol() {
        return symbol;
    }
    
    
}
