/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.state;

import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Felipe
 */
@MappedSuperclass
public abstract class Model implements Serializable {

    @Enumerated(EnumType.STRING)
    protected ModelState modelState;

    @Temporal(TemporalType.TIMESTAMP)
    Calendar lastModified;

    //@ManyToOne(cascade = CascadeType.ALL)
    @ElementCollection
    private Map<String, String> info;

    public Model() {
        this.info = new LinkedHashMap<>();
        this.modelState = ModelState.NEW;
        updateDate();
    }

    public void deactivate() {
        modelState.deactivate(this);
    }

    public void reactivate() {
        modelState.reactivate(this);
    }

    public final void update() {
        if (modelState == ModelState.INACTIVE) {
            modelState.reactivate(this);
        } else {
            modelState.update(this);
        }
    }

    public void malfunction() {
        modelState.malfunction(this);
    }

    public Calendar getLastModifiedDate() {
        return lastModified;
    }

    public ModelState getModelState() {
        return modelState;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(Map<String, String> info) {
        this.info = info;
    }

    void updateDate() {
        lastModified = Calendar.getInstance();
    }

}
