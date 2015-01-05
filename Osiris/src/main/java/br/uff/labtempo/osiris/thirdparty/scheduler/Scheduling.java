/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.thirdparty.scheduler;

/**
 *
 * @author Felipe
 */
public interface Scheduling<T> extends AutoCloseable{
    void initialize();
    void schedule(SchedulerItem<T> item);
}
