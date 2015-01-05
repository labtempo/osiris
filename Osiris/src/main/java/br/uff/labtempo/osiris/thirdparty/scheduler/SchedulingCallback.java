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
public interface SchedulingCallback<T>{   

    void callback(List<? extends SchedulerItem<T>> items) throws Exception;
}
