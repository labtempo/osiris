/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.basicosiris.model;

/**
 *
 * @author Felipe
 */
public class VirtualSensor implements Comparable<VirtualSensor> {

    private String name;
    private long id;

    public VirtualSensor() {
        this.id = System.currentTimeMillis();
    }

    void change(Object data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compareTo(VirtualSensor o) {
        if (this.id < o.id) {
            return -1;
        } else if (this.id > o.id) {
            return 1;
        } else {
            return 0;
        }
    }
}
