/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.jpa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Felipe
 */
@Embeddable
public class Rule implements Serializable {

    private String name;
    private String operator;
    private int value;
    private String message;

    protected Rule() {
    }

    public Rule(String name, String operator, int value, String message) {
        this.name = name;
        this.operator = operator;
        this.value = value;
        this.message = message;
    }

    public Rule(String name, String operator, String value, String message) {
        this(name, operator, Integer.parseInt(value), message);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Rule other = (Rule) obj;

        if (!name.equals(other.name)) {
            return false;
        }
        if (!operator.equals(other.operator)) {
            return false;
        }
        if (value != (other.value)) {
            return false;
        }
        if (!message.equals(other.message)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    public String getOperator() {
        return operator;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
    
    
}
