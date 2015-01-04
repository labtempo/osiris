/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.thirdparty.scheduler;

import java.util.List;

/**
 *
 * @author Felipe
 */
public interface SchedulingStorage<T> {

    SchedulerItem<T> getItemByObject(T o);

    List<? extends SchedulerItem<T>> getAllByTimeLimit(long timeLimitInMillis);

    void save(SchedulerItem<T> o);

    void update(SchedulerItem<T> o);

    void delete(SchedulerItem<T> o);

}
