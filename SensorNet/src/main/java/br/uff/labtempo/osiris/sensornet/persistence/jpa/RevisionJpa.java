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
import br.uff.labtempo.osiris.sensornet.model.Revision;
import br.uff.labtempo.osiris.sensornet.model.Revision_;
import br.uff.labtempo.osiris.sensornet.model.Sensor;
import br.uff.labtempo.osiris.sensornet.model.Sensor_;
import br.uff.labtempo.osiris.sensornet.persistence.RevisionDao;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RevisionJpa implements RevisionDao {

    private final DataManager data;

    public RevisionJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public List<Revision> getFromTo(String networkId, String collectorId, String sensorId, Calendar from, Calendar to, int limit) {
        long fromTime = from.getTimeInMillis();
        long toTime = to.getTimeInMillis();
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Revision> criteriaQuery = cb.createQuery(Revision.class);
        Root<Revision> root = criteriaQuery.from(Revision.class);

        Join<Revision, Sensor> senRoot = root.join(Revision_.sensor, JoinType.LEFT);
        Join<Sensor, Network> netroot = senRoot.join(Sensor_.network);
        Join<Sensor, Collector> colroot = senRoot.join(Sensor_.collector);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(netroot.<String>get(Network_.id), networkId));
        predicates.add(cb.equal(colroot.<String>get(Collector_.id), collectorId));
        predicates.add(cb.equal(senRoot.<String>get(Sensor_.id), sensorId));
        predicates.add(cb.lessThanOrEqualTo(root.<Long>get(Revision_.storageTimestampInMillis), fromTime));
        predicates.add(cb.greaterThanOrEqualTo(root.<Long>get(Revision_.storageTimestampInMillis), toTime));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        criteriaQuery.orderBy(cb.desc(root.get(Revision_.storageTimestampInMillis)));

        List<Revision> revisions;
        if (limit > 0) {
            revisions = data.getQuery(criteriaQuery, limit);
        } else {
            revisions = data.getQuery(criteriaQuery);
        }

        return revisions;
    }

    @Override
    public List<Revision> getTodayTo(String networkId, String collectorId, String sensorId, Calendar to, int limit) {
        long toTime = to.getTimeInMillis();
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Revision> criteriaQuery = cb.createQuery(Revision.class);
        Root<Revision> root = criteriaQuery.from(Revision.class);

        Join<Revision, Sensor> senRoot = root.join(Revision_.sensor, JoinType.LEFT);
        Join<Sensor, Network> netroot = senRoot.join(Sensor_.network);
        Join<Sensor, Collector> colroot = senRoot.join(Sensor_.collector);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(netroot.<String>get(Network_.id), networkId));
        predicates.add(cb.equal(colroot.<String>get(Collector_.id), collectorId));
        predicates.add(cb.equal(senRoot.<String>get(Sensor_.id), sensorId));
        predicates.add(cb.greaterThanOrEqualTo(root.<Long>get(Revision_.storageTimestampInMillis), toTime));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        criteriaQuery.orderBy(cb.desc(root.get(Revision_.storageTimestampInMillis)));

        List<Revision> revisions;
        if (limit > 0) {
            revisions = data.getQuery(criteriaQuery, limit);
        } else {
            revisions = data.getQuery(criteriaQuery);
        }

        return revisions;
    }

    @Override
    public List<Revision> getFrom(String networkId, String collectorId, String sensorId, Calendar from, int limit) {
        long fromTime = from.getTimeInMillis();
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Revision> criteriaQuery = cb.createQuery(Revision.class);
        Root<Revision> root = criteriaQuery.from(Revision.class);

        Join<Revision, Sensor> senRoot = root.join(Revision_.sensor, JoinType.LEFT);
        Join<Sensor, Network> netroot = senRoot.join(Sensor_.network);
        Join<Sensor, Collector> colroot = senRoot.join(Sensor_.collector);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(netroot.<String>get(Network_.id), networkId));
        predicates.add(cb.equal(colroot.<String>get(Collector_.id), collectorId));
        predicates.add(cb.equal(senRoot.<String>get(Sensor_.id), sensorId));
        predicates.add(cb.lessThanOrEqualTo(root.<Long>get(Revision_.storageTimestampInMillis), fromTime));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        criteriaQuery.orderBy(cb.desc(root.get(Revision_.storageTimestampInMillis)));

        List<Revision> revisions;
        if (limit > 0) {
            revisions = data.getQuery(criteriaQuery, limit);
        } else {
            revisions = data.getQuery(criteriaQuery);
        }

        return revisions;
    }

    @Override
    public List<Revision> getToday(String networkId, String collectorId, String sensorId, int limit) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Revision> criteriaQuery = cb.createQuery(Revision.class);
        Root<Revision> root = criteriaQuery.from(Revision.class);

        Join<Revision, Sensor> senRoot = root.join(Revision_.sensor, JoinType.LEFT);
        Join<Sensor, Network> netroot = senRoot.join(Sensor_.network);
        Join<Sensor, Collector> colroot = senRoot.join(Sensor_.collector);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(netroot.<String>get(Network_.id), networkId));
        predicates.add(cb.equal(colroot.<String>get(Collector_.id), collectorId));
        predicates.add(cb.equal(senRoot.<String>get(Sensor_.id), sensorId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        criteriaQuery.orderBy(cb.desc(root.get(Revision_.storageTimestampInMillis)));

        List<Revision> revisions;
        if (limit > 0) {
            revisions = data.getQuery(criteriaQuery, limit);
        } else {
            revisions = data.getQuery(criteriaQuery);
        }

        return revisions;
    }

    @Override
    public boolean hasVirtualSensor(String networkId, String collectorId, String sensorId) {
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
        Sensor sensor = data.getQuerySingle(criteriaQuery);
        if (sensor == null) {
            return false;
        }
        return true;
    }
}
