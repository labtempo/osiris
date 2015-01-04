/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.thirdparty.scheduler;

import br.uff.labtempo.osiris.thirdparty.scheduler.SchedulingStorage;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.persistence.jpa.DataManager;
import br.uff.labtempo.osiris.thirdparty.scheduler.SchedulerItem;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Felipe
 */
public class SchedulerStorageSensor implements SchedulingStorage<Sensor> {

    private final DataManager data;

    public SchedulerStorageSensor(DataManager data) {
        this.data = data;
    }

    @Override
    public SchedulerItem<Sensor> getItemByObject(Sensor o) {
        return data.get(SchedulerItemSensor.class, o.getSid());
    }

    @Override
    public List<? extends SchedulerItem<Sensor>> getAllByTimeLimit(long timeLimitInMillis) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<SchedulerItemSensor> criteriaQuery = cb.createQuery(SchedulerItemSensor.class);
        Root<SchedulerItemSensor> root = criteriaQuery.from(SchedulerItemSensor.class);

        //condition where
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.lessThanOrEqualTo(root.<Long>get("timeToNextUpdate"), timeLimitInMillis));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        List<SchedulerItemSensor> items = data.getQuery(criteriaQuery);
        return items;
    }

    @Override
    public void save(SchedulerItem<Sensor> o) {
        data.save(o);
    }

    @Override
    public void update(SchedulerItem<Sensor> o) {
        data.update(o);
    }

    @Override
    public void delete(SchedulerItem<Sensor> o) {
        data.delete(o);
    }
}
