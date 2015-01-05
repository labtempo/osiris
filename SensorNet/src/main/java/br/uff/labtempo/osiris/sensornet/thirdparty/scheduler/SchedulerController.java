/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.thirdparty.scheduler;

import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.persistence.SchedulerDao;
import br.uff.labtempo.osiris.thirdparty.scheduler.core.SchedulingManager;

/**
 *
 * @author Felipe
 */
public class SchedulerController implements SchedulerDao {

    private final SchedulingManager<Sensor> manager;

    SchedulerController(SchedulingManager<Sensor> manager) {
        this.manager = manager;
    }

    @Override
    public void schedule(Sensor sensor) {
        SchedulerItemSensor item = new SchedulerItemSensor(sensor);
        manager.schedule(item);
    }
}
