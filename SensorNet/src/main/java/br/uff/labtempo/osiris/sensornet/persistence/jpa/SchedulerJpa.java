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

import br.uff.labtempo.osiris.sensornet.persistence.SchedulerDao;
import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistence;
import br.uff.labtempo.osiris.sensornet.thirdparty.scheduler.ModelSchedulerItem;
import br.uff.labtempo.osiris.sensornet.thirdparty.scheduler.ModelSchedulerItem_;
import br.uff.labtempo.osiris.utils.scheduling.SchedulerItem;
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
public class SchedulerJpa implements SchedulerDao {

    private final BatchPersistence persistence;

    public SchedulerJpa(BatchPersistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public SchedulerItem getItemByObjectId(long sensorId) {
        CriteriaBuilder cb = persistence.getCriteriaBuilder();
        CriteriaQuery<ModelSchedulerItem> criteriaQuery = cb.createQuery(ModelSchedulerItem.class);
        Root<ModelSchedulerItem> root = criteriaQuery.from(ModelSchedulerItem.class);
        
        //condition where
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<Long>get(ModelSchedulerItem_.sensorId), sensorId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        ModelSchedulerItem item = persistence.getQuerySingle(criteriaQuery);
        return item;
    }

    @Override
    public List<? extends SchedulerItem> getAllByTimeLimit(long timeLimitInMillis) {
        CriteriaBuilder cb = persistence.getCriteriaBuilder();
        CriteriaQuery<ModelSchedulerItem> criteriaQuery = cb.createQuery(ModelSchedulerItem.class);
        Root<ModelSchedulerItem> root = criteriaQuery.from(ModelSchedulerItem.class);

        //condition where
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.lessThanOrEqualTo(root.<Long>get(ModelSchedulerItem_.timeToNextUpdate), timeLimitInMillis));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        List<ModelSchedulerItem> items = persistence.getQuery(criteriaQuery);
        return items;
    }

    @Override
    public void save(SchedulerItem o) {
        persistence.save(o);
    }

    @Override
    public void update(SchedulerItem o) {
        persistence.update(o);
    }

    @Override
    public void delete(SchedulerItem o) {
        persistence.delete(o);
    }
}
