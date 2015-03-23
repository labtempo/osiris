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
package br.uff.labtempo.osiris.sensornet.controller;

import br.uff.labtempo.osiris.sensornet.model.Collector;
import br.uff.labtempo.osiris.sensornet.model.Collector_;
import br.uff.labtempo.osiris.sensornet.model.Network;
import br.uff.labtempo.osiris.sensornet.model.Network_;
import br.uff.labtempo.osiris.sensornet.model.Sensor;
import br.uff.labtempo.osiris.sensornet.model.Sensor_;
import br.uff.labtempo.osiris.sensornet.model.state.ModelState;
import br.uff.labtempo.osiris.sensornet.model.util.AnnouncerWrapper;
import br.uff.labtempo.osiris.sensornet.persistence.CollectorDao;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.sensornet.persistence.SensorDao;
import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistence;
import br.uff.labtempo.osiris.sensornet.thirdparty.announcer.AnnouncerAgent;
import br.uff.labtempo.osiris.utils.scheduling.SchedulerItem;
import br.uff.labtempo.osiris.utils.scheduling.SchedulingCallback;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SchedulerController implements SchedulingCallback {

    private final BatchPersistence persistence;
    private final DaoFactory factory;
    private AnnouncerAgent announcer;

    public SchedulerController(DaoFactory factory, AnnouncerAgent announcer) {
        this.factory = factory;
        this.persistence = factory.getBatchPersistence();
        this.announcer = new AnnouncerWrapper(announcer);
    }

    public SchedulerController(DaoFactory factory) {
        this(factory, null);
    }

    @Transactional
    @Override
    public void callback(List<? extends SchedulerItem> items) {
        if (items != null && items.size() > 0) {
            for (SchedulerItem item : items) {
                long sensorId = item.getObjectId();
                checkSensor(sensorId);
            }
        }
    }

    private synchronized void checkSensor(long sensorId) {
        SensorDao sdao = factory.getPersistentSensorDao();
        //check collector and network to deactivation
        Sensor sensor = getSensorById(sensorId);

        {
            sensor.deactivate();
            update(sensor);
            SensorSnTo to = sensor.getTransferObject();
            announcer.notifyDeactivation(to);
            announcer.broadcastIt(to);
        }

        Network network = sensor.getNetwork();
        Collector collector = sensor.getCollector();

        List<Sensor> sensors = sdao.getAllInactive(network.getId(), collector.getId());

        if (collector.getSensors().length == sensors.size()) {
            collector.deactivate();
            update(collector);
            CollectorSnTo to = collector.getTransferObject();
            announcer.notifyDeactivation(to);
            announcer.broadcastIt(to);
        }

        sensors = sdao.getAllInactive(network.getId());
        if (network.getSensors().length == sensors.size()) {
            network.deactivate();
            update(network);
            NetworkSnTo to = network.getTransferObject();
            announcer.notifyDeactivation(to);
            announcer.broadcastIt(to);
        }
    }

    public void setAnnouncerAgent(AnnouncerAgent announcerAgent) {
        announcer = announcerAgent;
    }    
    
    private Sensor getSensorById(long id){
        return persistence.get(Sensor.class, id);
    }
    
    private void update(Object o){
        persistence.update(o);
    }
    
}
