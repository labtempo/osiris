/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.state;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MapKey;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Felipe
 */
@MappedSuperclass
public abstract class Model<T extends Model> implements Serializable{

    @Enumerated(EnumType.STRING)
    ModelState state;

    @Temporal(TemporalType.TIMESTAMP)
    Calendar lastModified;
    
    @OneToMany(cascade = CascadeType.ALL)
    @MapKey(name = "info")
    private Map<String, String> info;

    public Model() {
        this.state = ModelState.NEW;
    }

    public void deactivate() {
        state.deactivate(this);
    }

    public void reactivate() {
        state.reactivate(this);
    }

    protected final void update() {
        state.update(this);
    }

    public Calendar getLastModifiedDate() {
        return lastModified;
    }

    public ModelState getState() {
        return state;
    }

    public Map<String, String> getInfo() {
        return info;
    }
    
    
}
