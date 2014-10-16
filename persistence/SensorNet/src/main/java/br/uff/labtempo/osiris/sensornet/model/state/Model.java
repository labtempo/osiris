/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.osiris.sensornet.model.state;

import java.util.Calendar;

/**
 *
 * @author Felipe
 */
public abstract class Model<T extends Model> {
    ModelState state;

    public Model() {
        this.state = ModelState.NEW;
    }   
    
    public void deactivate() {
        state.deactivate(this);
    }

    public void reactivate() {
        state.reactivate(this);
    }   
    
    public abstract void  update(T model);
    
    protected final void update() {
        state.update(this);
    }
    
    public Calendar getLastModifiedDate(){
        return state.getDate();
    }
    
    public ModelState getState(){
        return state;
    }
}
