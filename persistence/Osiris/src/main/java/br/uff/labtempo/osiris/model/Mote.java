/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Felipe
 */
@Entity
public class Mote implements Serializable {

    @Id
    private String id;

    @Column(nullable = true)
    private String parent;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    private String lastSample;

    @OneToOne(mappedBy = "mote")
    private VirtualSensor sensor;
    
    @OneToMany(mappedBy = "mote")
    private List<Sample> samples = new ArrayList<>();

    public Mote() {
    }

    public Mote(String id, String sample) {
        this.id = id;
        this.lastModified = new Date();
        this.lastSample = sample;
    }

    public String getId() {
        return this.id;
    }

    public String getLastSample() {
        return this.lastSample;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastSample(String data) {
        this.lastSample = data;
    }

    public void setLastModified(long timestamp) {
        this.lastModified = new Date(timestamp);
    }

    public boolean hasSensor() {
        return this.sensor != null;
    }

    public VirtualSensor getSensor() {
        return this.sensor;
    }

}
