/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.osiris.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Felipe
 */
@Entity
public class VirtualSensor implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    
    @OneToOne
    private Mote mote;    
    
    @OneToMany(mappedBy = "sensor")
    private List<Sample> samples;

    public VirtualSensor() {
    }

    public VirtualSensor(String name) {
        this.name = name;
    }

    public void setMote(Mote mote) {
        this.mote = mote;
    }

    public List<Sample> getSamples() {
        return this.samples;                
    }
}
