/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.thirdparty.scheduler;

import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.SchedulerDao;
import br.uff.labtempo.osiris.sensornet.persistence.jpa.DataManager;
import br.uff.labtempo.osiris.thirdparty.scheduler.core.SchedulingManager;

/**
 *
 * @author Felipe
 */
public class SchedulerBootstrap implements AutoCloseable {

    private SchedulingManager<Sensor> manager;

    public SchedulerBootstrap(DataManager data, DaoFactory factory) throws Exception {
        try {
            SchedulerStorageSensor storage = new SchedulerStorageSensor(data);
            SchedulerCallbackSensor callback = new SchedulerCallbackSensor(factory);
            manager = new SchedulingManager<>(storage, callback);
            manager.initialize();
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    @Override
    public void close() throws Exception {
        manager.close();
    }

    public SchedulerDao getScheduler() {
        return new SchedulerController(manager);
    }

}
