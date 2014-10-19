/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector.temp;

/**
 *
 * @author Felipe
 */
public class Collector extends Network {

    public Collector(String id) {
        super(id);
    }

    public Collector(long id) {
        this(String.valueOf(id));
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

        Collector other = (Collector) obj;

        if (getId().equals(other.getId())) {
            return true;
        }
        return false;
    }    
}
