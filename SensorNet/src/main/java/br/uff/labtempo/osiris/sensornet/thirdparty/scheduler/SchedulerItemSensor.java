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

import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.thirdparty.scheduler.SchedulerItem;
import java.io.Serializable;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
@Table(name = "SCHEDULING_TABLE")
public class SchedulerItemSensor implements Serializable, SchedulerItem<Sensor> {

    @Id
    @Column(name = "object_id")
    long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "object_id")
    private Sensor sensor;
    
    private long timeToNextUpdate;

    public SchedulerItemSensor() {
    }

    public SchedulerItemSensor(Sensor sensor) {
        this.sensor = sensor;

        Calendar lastModifiedDate = sensor.getLastModifiedDate();
        Collector collector = sensor.getCollector();
        long intervalInMillis = TimeUnit.MILLISECONDS.convert(collector.getInterval(), collector.getTimeUnit());
        long nextTimeToUpdate = lastModifiedDate.getTimeInMillis() + intervalInMillis;

        this.timeToNextUpdate = nextTimeToUpdate;
    }

    @Override
    public Sensor getObject() {
        return sensor;
    }

    @Override
    public long getTimeToNextUpdate() {
        return timeToNextUpdate;
    }

    @Override
    public void updateTimeToNextUpdate(long estimatedTimeToNextUpdate) {
        this.timeToNextUpdate = estimatedTimeToNextUpdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public long getIntervalInMillis() {
        Collector collector = sensor.getCollector();
        long intervalInMillis = TimeUnit.MILLISECONDS.convert(collector.getInterval(), collector.getTimeUnit());
        return intervalInMillis / 8;
    }

}
