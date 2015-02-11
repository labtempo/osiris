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
package br.uff.labtempo.osiris.sensornet.thirdparty.scheduler;

import br.uff.labtempo.osiris.sensornet.model.Collector;
import br.uff.labtempo.osiris.sensornet.model.Sensor;
import br.uff.labtempo.osiris.utils.scheduling.SchedulerItem;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
@Table(name = "SCHEDULING_TABLE")
public class ModelSchedulerItem implements Serializable, SchedulerItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private long sensorId;    
    private long timeToNextUpdate;
    private long intervalInMillis;

    public ModelSchedulerItem() {
    }

    public ModelSchedulerItem(Sensor sensor) {
        this.sensorId = sensor.getPersistenceId();
        Collector collector = sensor.getCollector();
        this.intervalInMillis = TimeUnit.MILLISECONDS.convert(collector.getInterval(), collector.getTimeUnit());
        this.timeToNextUpdate = sensor.getStorageTimestampInMillis() + intervalInMillis;
    }

    @Override
    public long getObjectId() {
        return sensorId;
    }

    @Override
    public long getTimeToNextUpdate() {
        return timeToNextUpdate;
    }

    @Override
    public void updateTimeToNextUpdate(long estimatedTimeToNextUpdate) {
        this.timeToNextUpdate = estimatedTimeToNextUpdate;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public long getIntervalInMillis() {
        return intervalInMillis / 2;
    }

}
