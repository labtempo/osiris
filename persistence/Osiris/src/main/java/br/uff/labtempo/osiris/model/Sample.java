/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Felipe
 */
@Entity
public class Sample implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Mote mote;

    @ManyToOne
    private VirtualSensor sensor;

    private String sample;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Sample() {
    }

    public Sample(Mote mote, String sample, Date timestamp) {
        this.mote = mote;
        this.sample = sample;
        this.timestamp = timestamp;
    }

    public String getSample() {
        return this.sample;
    }
}
