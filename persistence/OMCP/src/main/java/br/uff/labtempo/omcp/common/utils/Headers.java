/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common.utils;

/**
 *
 * @author Felipe
 */
public enum Headers {

    DATE, MODULE, CONTENT_LENGTH, ERROR, LOCATION;

    private static final String SEPARATOR = ":";

    public static String getSeparator() {
        return SEPARATOR;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase().replace("_", "-");
    }

    public String getKey() {
        return toString().concat(SEPARATOR);
    }
}
