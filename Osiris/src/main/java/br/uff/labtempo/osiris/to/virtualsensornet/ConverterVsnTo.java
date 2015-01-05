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
public class ConverterVsnTo {
    private long id;
    private String name;
    private String expression;
    private long dataTypeId;

    public ConverterVsnTo(String name, String expression, long dataTypeId) {
        this.name = name;
        this.expression = expression;
        this.dataTypeId = dataTypeId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExpression() {
        return expression;
    }

    public long getDataTypeId() {
        return dataTypeId;
    }
    
    
}
