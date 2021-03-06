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
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorLink;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorLink_;
import br.uff.labtempo.osiris.virtualsensornet.persistence.LinkDao;
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
public class LinkJpaPersistent implements LinkDao {

    private final BatchPersistence persistence;

    public LinkJpaPersistent(BatchPersistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public VirtualSensorLink get(long id) {
        return persistence.get(VirtualSensorLink.class, id);
    }

    @Override
    public VirtualSensorLink get(VirtualSensorLink sensorLink) {
        return get(sensorLink.getId());
    }

    @Override
    public List<VirtualSensorLink> getAll() {
        CriteriaBuilder cb = persistence.getCriteriaBuilder();
        CriteriaQuery<VirtualSensorLink> criteriaQuery = cb.createQuery(VirtualSensorLink.class);
        Root<VirtualSensorLink> root = criteriaQuery.from(VirtualSensorLink.class);
        criteriaQuery.select(root);
        List<VirtualSensorLink> virtualSensors = persistence.getQuery(criteriaQuery);
        return virtualSensors;
    }

    @Override
    public List<VirtualSensorLink> getAllByReferences(String networkId, String collectorId, String sensorId) {
        CriteriaBuilder cb = persistence.getCriteriaBuilder();
        CriteriaQuery<VirtualSensorLink> criteriaQuery = cb.createQuery(VirtualSensorLink.class);
        Root<VirtualSensorLink> root = criteriaQuery.from(VirtualSensorLink.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<String>get(VirtualSensorLink_.networkId), networkId));
        predicates.add(cb.equal(root.<String>get(VirtualSensorLink_.collectorId), collectorId));
        predicates.add(cb.equal(root.<String>get(VirtualSensorLink_.sensorId), sensorId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);

        List<VirtualSensorLink> links = persistence.getQuery(criteriaQuery);
        return links;
    }

    @Override
    public void save(VirtualSensorLink o) {
        persistence.save((VirtualSensor) o);
    }

    @Override
    public void update(VirtualSensorLink o) {
        persistence.update((VirtualSensor) o);
    }

    @Override
    public void delete(VirtualSensorLink o) {
        persistence.delete((VirtualSensor) o);
    }

    @Override
    public void commit() {
        persistence.forceCommit();
    }

    
}
