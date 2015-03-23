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
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorComposite;
import br.uff.labtempo.osiris.virtualsensornet.persistence.CompositeDao;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class CompositeJpaPersistent implements CompositeDao {

    private final BatchPersistence persistence;

    public CompositeJpaPersistent(BatchPersistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public VirtualSensorComposite getById(long id) {
        return persistence.get(VirtualSensorComposite.class, id);
    }

    @Override
    public VirtualSensorComposite get(VirtualSensorComposite sensorLink) {
        return getById(sensorLink.getId());
    }

    @Override
    public List<VirtualSensorComposite> getAll() {
        CriteriaBuilder cb = persistence.getCriteriaBuilder();
        CriteriaQuery<VirtualSensorComposite> criteriaQuery = cb.createQuery(VirtualSensorComposite.class);
        Root<VirtualSensorComposite> root = criteriaQuery.from(VirtualSensorComposite.class);
        criteriaQuery.select(root);
        List<VirtualSensorComposite> virtualSensors = persistence.getQuery(criteriaQuery);
        return virtualSensors;
    }

    @Override
    public void save(VirtualSensorComposite o) {
        persistence.save((VirtualSensor) o);
    }

    @Override
    public void update(VirtualSensorComposite o) {
        persistence.update((VirtualSensor) o);
    }

    @Override
    public void update(VirtualSensor sensor) {
        persistence.update(sensor);
    }

    @Override
    public void delete(VirtualSensorComposite o) {
        persistence.delete((VirtualSensor) o);
    }

}
