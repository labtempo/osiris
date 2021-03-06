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
package br.uff.labtempo.osiris.virtualsensornet.persistence.jpa;

import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistence;
import br.uff.labtempo.osiris.virtualsensornet.model.Revision;
import br.uff.labtempo.osiris.virtualsensornet.model.Revision_;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;
import br.uff.labtempo.osiris.virtualsensornet.persistence.RevisionDao;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RevisionJpaPersistent implements RevisionDao {

    private final BatchPersistence data;

    public RevisionJpaPersistent(BatchPersistence data) {
        this.data = data;
    }

    @Override
    public List<Revision> getFromTo(long sensorId, Calendar from, Calendar to, int limit) {
        long fromTime = from.getTimeInMillis();
        long toTime = to.getTimeInMillis();
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Revision> criteriaQuery = cb.createQuery(Revision.class);
        Root<Revision> root = criteriaQuery.from(Revision.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<Long>get(Revision_.virtualSensorId), sensorId));
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
    public List<Revision> getTodayTo(long sensorId, Calendar to, int limit) {
        long toTime = to.getTimeInMillis();
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Revision> criteriaQuery = cb.createQuery(Revision.class);
        Root<Revision> root = criteriaQuery.from(Revision.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<Long>get(Revision_.virtualSensorId), sensorId));
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
    public List<Revision> getFrom(long sensorId, Calendar from, int limit) {
        long fromTime = from.getTimeInMillis();
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Revision> criteriaQuery = cb.createQuery(Revision.class);
        Root<Revision> root = criteriaQuery.from(Revision.class);
        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<Long>get(Revision_.virtualSensorId), sensorId));
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
    public List<Revision> getToday(long sensorId, int limit) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Revision> criteriaQuery = cb.createQuery(Revision.class);
        Root<Revision> root = criteriaQuery.from(Revision.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<Long>get(Revision_.virtualSensorId), sensorId));
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
    public int deleteAllByVirtualSensor(long sensorId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaDelete<Revision> criteriaQuery = cb.createCriteriaDelete(Revision.class);
        Root<Revision> root = criteriaQuery.from(Revision.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<Long>get(Revision_.virtualSensorId), sensorId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        return data.getQuery(criteriaQuery);
    }

    @Override
    public boolean hasVirtualSensor(long sensorId) {
        VirtualSensor vs = data.get(VirtualSensor.class, sensorId);
        if (vs == null) {
            return false;
        }
        return true;
    }

    @Override
    public void save(Revision o) {
        data.save(o);
    }

    @Override
    public void update(Revision o) {
        data.update(o);
    }

    @Override
    public void delete(Revision o) {
        data.delete(o);
    }
}
