/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Felipe
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
