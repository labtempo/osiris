/* 
 * Copyright 2015 Felipe Santos <fralph at ic.uff.br>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.uff.labtempo.osiris.virtualsensornet.controller;

import br.uff.labtempo.osiris.thirdparty.scheduler.SchedulerItem;
import br.uff.labtempo.osiris.thirdparty.scheduler.SchedulingCallback;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;
import br.uff.labtempo.osiris.virtualsensornet.model.util.AnnouncerWrapper;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer.AnnouncerAgent;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SchedulerController implements SchedulingCallback<VirtualSensor> {

    private final DaoFactory factory;
    private AnnouncerAgent announcer;

    public SchedulerController(DaoFactory factory, AnnouncerAgent announcer) {
        this.factory = factory;
        this.announcer = new AnnouncerWrapper(announcer);
    }

    public SchedulerController(DaoFactory factory) {
        this.factory = factory;
        this.announcer = new AnnouncerWrapper(null);
    }

    @Override
    public void callback(List<? extends SchedulerItem<VirtualSensor>> items) {
        if (items != null && items.size() > 0) {
            for (SchedulerItem<VirtualSensor> item : items) {
                VirtualSensor sensor = item.getObject();
                checkSensor(sensor);
            }
        }
    }

    private void checkSensor(VirtualSensor sensor) {

        //check collector and network to deactivation
        /*SensorDao sensorDao = factory.getSensorDao();
         NetworkDao networkDao = factory.getNetworkDao();
         CollectorDao collectorDao = factory.getCollectorDao();
         AnnouncerDao announcerDao = factory.getSchedulerDao();

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
         }*/
    }

    public void setAnnouncerAgent(AnnouncerAgent announcerAgent) {
        announcer = announcerAgent;
    }
}
