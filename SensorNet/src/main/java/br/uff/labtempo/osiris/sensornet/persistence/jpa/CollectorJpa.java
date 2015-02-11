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
import br.uff.labtempo.osiris.sensornet.model.state.ModelState;
import br.uff.labtempo.osiris.sensornet.persistence.CollectorDao;
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
public class CollectorJpa implements CollectorDao {

    private final DataManager data;

    public CollectorJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public Collector getByUniqueId(long uniqueId) {
        return data.get(Collector.class, uniqueId);
    }

    @Override
    public Collector get(Collector o) {
        return data.getReference(Collector.class, o);
    }

    @Override
    public Collector get(String networkId, String collectorId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Collector> criteriaQuery = cb.createQuery(Collector.class);
        Root<Collector> root = criteriaQuery.from(Collector.class);

        Join<Collector, Network> netroot = root.join(Collector_.network);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(netroot.<String>get(Network_.id), networkId));
        predicates.add(cb.equal(root.<String>get(Collector_.id), collectorId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        return data.getQuerySingle(criteriaQuery);
    }

    @Override
    public List<Collector> getAll(String networkId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Collector> criteriaQuery = cb.createQuery(Collector.class);
        Root<Collector> root = criteriaQuery.from(Collector.class);

        Join<Collector, Network> subroot = root.join(Collector_.network);

        criteriaQuery.where(cb.equal(subroot.<String>get(Network_.id), networkId));
        criteriaQuery.select(root);
        List<Collector> collectors = data.getQuery(criteriaQuery);

        return collectors;
    }

    @Override
    public void save(Collector o) {
        data.save(o);
    }

    @Override
    public void update(Collector o) {
        data.update(o);
    }

    @Override
    public void delete(Collector o) {
        data.delete(o);
    }

    @Override
    public List<Collector> getAllInactive(String networkId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Collector> criteriaQuery = cb.createQuery(Collector.class);
        Root<Collector> root = criteriaQuery.from(Collector.class);

        Join<Collector, Network> netroot = root.join(Collector_.network);

        criteriaQuery.where(cb.equal(netroot.<String>get(Network_.id), networkId));
        criteriaQuery.where(cb.equal(root.<ModelState>get(Collector_.modelState), ModelState.INACTIVE));
        criteriaQuery.select(root);
        List<Collector> collectors = data.getQuery(criteriaQuery);

        return collectors;
    }
}
