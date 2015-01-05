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
 * @author Felipe Santos <fralph at ic.uff.br>
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
