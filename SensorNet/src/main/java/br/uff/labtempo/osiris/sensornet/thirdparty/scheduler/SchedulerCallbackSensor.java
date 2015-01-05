/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.thirdparty.scheduler;

import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.persistence.AnnouncerDao;
import br.uff.labtempo.osiris.sensornet.persistence.CollectorDao;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.sensornet.persistence.SensorDao;
import br.uff.labtempo.osiris.thirdparty.scheduler.SchedulerItem;
import br.uff.labtempo.osiris.thirdparty.scheduler.SchedulingCallback;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.List;

/**
 *
 * @author Felipe
 */
public class SchedulerCallbackSensor implements SchedulingCallback<Sensor> {

    private final DaoFactory factory;

    SchedulerCallbackSensor(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public void callback(List<? extends SchedulerItem<Sensor>> items) {
        if (items != null && items.size() > 0) {
            for (SchedulerItem<Sensor> item : items) {
                Sensor sensor = item.getObject();
                checkSensor(sensor);
            }
        }
    }

    private void checkSensor(Sensor sensor) {

        //check collector and network to deactivation
        SensorDao sensorDao = factory.getSensorDao();
        NetworkDao networkDao = factory.getNetworkDao();
        CollectorDao collectorDao = factory.getCollectorDao();
        AnnouncerDao announcerDao = factory.getAnnouncerDao();

        {
            sensor.deactivate();
            sensorDao.update(sensor);
            SensorSnTo to = sensor.getTransferObject();
            announcerDao.notifyDeactivation(to);
            announcerDao.broadcastIt(to);
        }

        Network network = sensor.getNetwork();
        Collector collector = sensor.getCollector();

        List<Sensor> sensors = sensorDao.getAllInactive(network.getId(), collector.getId());

        if (collector.getSensors().length == sensors.size()) {
            collector.deactivate();
            collectorDao.update(collector);
            CollectorSnTo to = collector.getTransferObject();
            announcerDao.notifyDeactivation(to);
            announcerDao.broadcastIt(to);
        }

        sensors = sensorDao.getAllInactive(network.getId());
        if (network.getSensors().length == sensors.size()) {
            network.deactivate();
            networkDao.update(network);
            NetworkSnTo to = network.getTransferObject();
            announcerDao.notifyDeactivation(to);
            announcerDao.broadcastIt(to);
        }
    }    
}
