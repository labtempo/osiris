/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.model;

import java.util.Calendar;

/**
 *
 * @author Felipe
 */
class Value {

    private String value;
    private Calendar date;

    public Value(String value) {
        this.value = value;
        this.date = Calendar.getInstance();
    }

    public Value(long value) {
        this(String.valueOf(value));
    }

    public Value(double value) {
        this(String.valueOf(value));
    }

    public Value(boolean value) {
        this(String.valueOf(value));
    }

    public String getValue() {
        return value;
    }

    public Calendar getDate() {
        return date;
    }

}
