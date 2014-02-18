/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public abstract class Service extends Component implements Runnable {

    private Thread thread;
    private boolean close;
    private Service obj;

    public Service(String name) {
        thread = new Thread(this, "Service - " + name);
        thread.setDaemon(true);
    }

    public Thread getThread() {
        return thread;
    }

    @Override
    public void run() throws ComponentInitializationException {
        loop();
    }

    @Override
    public void start() throws ComponentInitializationException {
        try {
            beforeBoot();
            boostrap();
            afterBoot();
            thread.start();
        } catch (Exception e) {
            close();
            throw new ComponentInitializationException(e);
        }
    }

    @Override
    public void close() {
        beforeShutdown();
        shutdown();
        afterShutdown();
    }

    protected void loop() throws ComponentInitializationException {
        obj = this;
        synchronized (this) {
            while (!close) {
                try {
                    wait();
                } catch (InterruptedException ex) {

                }
            }
        }
    }

    @Override
    protected void shutdown() throws ComponentInitializationException {
        if (obj != null) {
            close = true;
            synchronized (obj) {
                obj.notifyAll();
            }
        }
    }
}
