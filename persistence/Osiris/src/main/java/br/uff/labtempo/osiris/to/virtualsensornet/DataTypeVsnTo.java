/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.virtualsensornet;

/**
 *
 * @author Felipe
 */
public class DataTypeVsnTo {

    private long id;
    private String displayName;
    private String type;
    private String unit;
    private String symbol;

    public DataTypeVsnTo(String displayName, String type, String unit, String symbol) {
        this.displayName = displayName;
        this.type = type;
        this.unit = unit;
        this.symbol = symbol;
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public String getSymbol() {
        return symbol;
    }
    
    
}
