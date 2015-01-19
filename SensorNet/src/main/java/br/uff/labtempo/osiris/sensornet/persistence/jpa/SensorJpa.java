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
package br.uff.labtempo.osiris.sensornet.persistence.jpa;

import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Collector_;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network_;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor_;
import br.uff.labtempo.osiris.sensornet.model.state.ModelState;
import br.uff.labtempo.osiris.sensornet.persistence.SensorDao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SensorJpa implements SensorDao {

    private final DataManager data;

    public SensorJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public Sensor get(Sensor o) {
        return data.get(Sensor.class,o.getSid());
    }

    @Override
    public Sensor get(String networkId, String collectorId, String sensorId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Sensor> criteriaQuery = cb.createQuery(Sensor.class);
        Root<Sensor> root = criteriaQuery.from(Sensor.class);
        Join<Sensor, Network> netroot = root.join(Sensor_.network);
        Join<Sensor, Collector> colroot = root.join(Sensor_.collector);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(netroot.<String>get(Network_.id), networkId));
        predicates.add(cb.equal(colroot.<String>get(Collector_.id), collectorId));
        predicates.add(cb.equal(root.<String>get(Sensor_.id), sensorId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        return data.getQuerySingle(criteriaQuery);
    }

    @Override
    public List<Sensor> getAll(String networkId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Sensor> criteriaQuery = cb.createQuery(Sensor.class);
        
        Root<Sensor> root = criteriaQuery.from(Sensor.class);
        Join<Sensor, Network> netroot = root.join(Sensor_.network);
        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(netroot.<String>get(Network_.id), networkId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        List<Sensor> sensors = data.getQuery(criteriaQuery);

        return sensors;
    }

    @Override
    public List<Sensor> getAll(String networkId, String collectorId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Sensor> criteriaQuery = cb.createQuery(Sensor.class);
        Root<Sensor> root = criteriaQuery.from(Sensor.class);
        Join<Sensor, Network> netroot = root.join(Sensor_.network);
        Join<Sensor, Collector> colroot = root.join(Sensor_.collector);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(netroot.<String>get(Network_.id), networkId));
        predicates.add(cb.equal(colroot.<String>get(Collector_.id), collectorId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        List<Sensor> sensors = data.getQuery(criteriaQuery);

        return sensors;
    }

    @Override
    public void save(Sensor o) {
        data.save(o);
    }

    @Override
    public void update(Sensor o) {
        data.update(o);
    }

    @Override
    public void delete(Sensor o) {
        data.delete(o);
    }

    @Override
    public List<Sensor> getAllInactive(String networkId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Sensor> criteriaQuery = cb.createQuery(Sensor.class);
        Root<Sensor> root = criteriaQuery.from(Sensor.class);
        Join<Sensor, Network> netroot = root.join(Sensor_.network);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(netroot.<String>get(Network_.id), networkId));            
        predicates.add(cb.equal(root.<ModelState>get(Sensor_.modelState), ModelState.INACTIVE));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        List<Sensor> sensors = data.getQuery(criteriaQuery);

        return sensors;
    }

    @Override
    public List<Sensor> getAllInactive(String networkId, String collectorId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Sensor> criteriaQuery = cb.createQuery(Sensor.class);
        Root<Sensor> root = criteriaQuery.from(Sensor.class);
        Join<Sensor, Network> netroot = root.join(Sensor_.network);
        Join<Sensor, Collector> colroot = root.join(Sensor_.collector);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(netroot.<String>get(Network_.id), networkId));
        predicates.add(cb.equal(colroot.<String>get(Collector_.id), collectorId));        
        predicates.add(cb.equal(root.<ModelState>get(Sensor_.modelState), ModelState.INACTIVE));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        List<Sensor> sensors = data.getQuery(criteriaQuery);

        return sensors;}

}
