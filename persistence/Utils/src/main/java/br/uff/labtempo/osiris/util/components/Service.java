/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components;

import br.uff.labtempo.osiris.util.logging.Log;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public abstract class Service extends Component implements Runnable {

    private String name;

    public Service(String name) {
        this.name = name;
    }

    @Override
    public void run() throws ComponentInitializationException {
        Thread.currentThread().setName("Service - " + name);
        onLoop();
    }
    
    @Override
    protected abstract void onCreate() throws ComponentInitializationException;
    
    protected abstract void onLoop() throws ComponentInitializationException;
}
