/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.osiris.util.components;

/**
 *
 * @author Felipe
 */
public abstract class Component {

    public void start() throws ComponentInitializationException {
        try {
            beforeBoot();
            boostrap();
            afterBoot();
        } catch (Exception e) {
            close();
            throw new ComponentInitializationException(e);
        }
    }

    public void close() {
        beforeShutdown();
        shutdown();
        afterShutdown();
    }

    /* operation */
    protected void beforeBoot() throws ComponentInitializationException {
    }

    protected void boostrap() throws ComponentInitializationException {
    }

    protected void afterBoot() throws ComponentInitializationException {
    }

    protected void beforeShutdown() {
    }

    protected void shutdown() {
    }

    protected void afterShutdown() {
    }
}
