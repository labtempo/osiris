/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.model.state;

import java.io.Serializable;
import java.util.Calendar;
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
public abstract class Model<T extends Model> implements Serializable {

    @Enumerated(EnumType.STRING)
    protected ModelState state;

    @Temporal(TemporalType.TIMESTAMP)
    Calendar lastModified;

    public Model() {
        this.state = ModelState.NEW;
        updateDate();
    }

    protected void deactivate() {
        state.deactivate(this);
    }

    protected void reactivate() {
        state.reactivate(this);
    }

    protected final void update() {
        if (state == ModelState.INACTIVE) {
            state.reactivate(this);
        } else {
            state.update(this);
        }
    }

    protected void malfunction() {
        state.malfunction(this);
    }

    public Calendar getLastModifiedDate() {
        return lastModified;
    }

    public ModelState getState() {
        return state;
    }

    void updateDate() {
        lastModified = Calendar.getInstance();
    }

}
