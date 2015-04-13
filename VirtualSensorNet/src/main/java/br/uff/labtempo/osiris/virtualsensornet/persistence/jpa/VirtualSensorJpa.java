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

import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor_;
import br.uff.labtempo.osiris.virtualsensornet.model.state.ModelState;
import br.uff.labtempo.osiris.virtualsensornet.persistence.VirtualSensorDao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VirtualSensorJpa implements VirtualSensorDao {

    private final DataManager data;

    VirtualSensorJpa(DataManager data) {
        this.data = data;
    }

    @Transactional
    @Override
    public VirtualSensor get(VirtualSensor o) {
        return get(o.getId());
    }

    @Override
    public VirtualSensor get(long vsensorId) {
        return data.get(VirtualSensor.class, vsensorId);
    }

    @Override
    public List<VirtualSensor> getAll() {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<VirtualSensor> criteriaQuery = cb.createQuery(VirtualSensor.class);
        Root<VirtualSensor> root = criteriaQuery.from(VirtualSensor.class);
        criteriaQuery.select(root);
        List<VirtualSensor> virtualSensors = data.getQuery(criteriaQuery);
        return virtualSensors;
    }

    @Override
    public List<VirtualSensor> getAllInactive() {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<VirtualSensor> criteriaQuery = cb.createQuery(VirtualSensor.class);
        Root<VirtualSensor> root = criteriaQuery.from(VirtualSensor.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<ModelState>get(VirtualSensor_.modelState), ModelState.INACTIVE));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        List<VirtualSensor> virtualSensors = data.getQuery(criteriaQuery);
        return virtualSensors;
    }

    @Override
    public void save(VirtualSensor o) {
        data.save(o);
    }

    @Override
    public void update(VirtualSensor o) {
        data.update(o);
    }

    @Override
    public void delete(VirtualSensor o) {
        data.delete(o);
    }

}
