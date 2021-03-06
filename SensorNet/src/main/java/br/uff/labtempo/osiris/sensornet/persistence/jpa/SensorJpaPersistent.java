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

import br.uff.labtempo.osiris.sensornet.model.Collector;
import br.uff.labtempo.osiris.sensornet.model.Collector_;
import br.uff.labtempo.osiris.sensornet.model.Network;
import br.uff.labtempo.osiris.sensornet.model.Network_;
import br.uff.labtempo.osiris.sensornet.model.Sensor;
import br.uff.labtempo.osiris.sensornet.model.Sensor_;
import br.uff.labtempo.osiris.sensornet.model.state.ModelState;
import br.uff.labtempo.osiris.sensornet.persistence.SensorDao;
import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistence;
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
public class SensorJpaPersistent implements SensorDao {

    private final BatchPersistence persistence;

    public SensorJpaPersistent(BatchPersistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public Sensor getByUniqueId(long uniqueId) {
        return persistence.get(Sensor.class, uniqueId);
    }

    @Override
    public Sensor get(Sensor o) {
        return persistence.get(Sensor.class, o.getPersistenceId());
    }

    @Override
    public Sensor get(String networkId, String collectorId, String sensorId) {
        CriteriaBuilder cb = persistence.getCriteriaBuilder();
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
        return persistence.getQuerySingle(criteriaQuery);
    }

    @Override
    public List<Sensor> getAll(String networkId) {
        CriteriaBuilder cb = persistence.getCriteriaBuilder();
        CriteriaQuery<Sensor> criteriaQuery = cb.createQuery(Sensor.class);

        Root<Sensor> root = criteriaQuery.from(Sensor.class);
        Join<Sensor, Network> netroot = root.join(Sensor_.network);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(netroot.<String>get(Network_.id), networkId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        List<Sensor> sensors = persistence.getQuery(criteriaQuery);

        return sensors;
    }

    @Override
    public List<Sensor> getAll(String networkId, String collectorId) {
        CriteriaBuilder cb = persistence.getCriteriaBuilder();
        CriteriaQuery<Sensor> criteriaQuery = cb.createQuery(Sensor.class);
        Root<Sensor> root = criteriaQuery.from(Sensor.class);
        Join<Sensor, Network> netroot = root.join(Sensor_.network);
        Join<Sensor, Collector> colroot = root.join(Sensor_.collector);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(netroot.<String>get(Network_.id), networkId));
        predicates.add(cb.equal(colroot.<String>get(Collector_.id), collectorId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        List<Sensor> sensors = persistence.getQuery(criteriaQuery);

        return sensors;
    }

    @Override
    public void save(Sensor o) {
        persistence.save(o);
    }

    @Override
    public void update(Sensor o) {
        persistence.update(o);
    }

    @Override
    public void delete(Sensor o) {
        persistence.delete(o);
    }

    @Override
    public List<Sensor> getAllInactive(String networkId) {
        CriteriaBuilder cb = persistence.getCriteriaBuilder();
        CriteriaQuery<Sensor> criteriaQuery = cb.createQuery(Sensor.class);
        Root<Sensor> root = criteriaQuery.from(Sensor.class);
        Join<Sensor, Network> netroot = root.join(Sensor_.network);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(netroot.<String>get(Network_.id), networkId));
        predicates.add(cb.equal(root.<ModelState>get(Sensor_.modelState), ModelState.INACTIVE));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        List<Sensor> sensors = persistence.getQuery(criteriaQuery);

        return sensors;
    }

    @Override
    public List<Sensor> getAllInactive(String networkId, String collectorId) {
        CriteriaBuilder cb = persistence.getCriteriaBuilder();
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
        List<Sensor> sensors = persistence.getQuery(criteriaQuery);

        return sensors;
    }

}
