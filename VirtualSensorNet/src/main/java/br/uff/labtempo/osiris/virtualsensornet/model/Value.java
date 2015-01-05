/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Felipe
 */
@Entity
class Value implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    
    private String value;

    @ManyToOne
    private DataType dataType;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar date;

    @ManyToOne
    private Field field;

    public Value(String value, DataType dataType, Field field) {
        this.value = value;
        this.date = Calendar.getInstance();
        this.dataType = dataType;
        this.field = field;
    }

    public Value(long value, DataType dataType, Field field) {
        this(String.valueOf(value), dataType, field);
    }

    public Value(double value, DataType dataType, Field field) {
        this(String.valueOf(value), dataType, field);
    }

    public Value(boolean value, DataType dataType, Field field) {
        this(String.valueOf(value), dataType, field);
    }

    protected Value() {
    }

    public String getValue() {
        return value;
    }

    public Calendar getDate() {
        return date;
    }

    public DataType getDataType() {
        return dataType;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.value);
        hash = 17 * hash + Objects.hashCode(this.dataType);
        hash = 17 * hash + Objects.hashCode(this.date);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Value other = (Value) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Objects.equals(this.dataType, other.dataType)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        return true;
    }

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

}
