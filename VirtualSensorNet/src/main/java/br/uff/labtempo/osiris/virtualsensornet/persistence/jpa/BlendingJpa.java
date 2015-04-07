/*
 * Copyright 2015 Felipe Santos <feliperalph at hotmail.com>.
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

import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorBlending;
import br.uff.labtempo.osiris.virtualsensornet.persistence.BlendingDao;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class BlendingJpa implements BlendingDao {

    private final DataManager data;

    public BlendingJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public VirtualSensorBlending getById(long id) {
        return data.get(VirtualSensorBlending.class, id);
    }

    @Override
    public VirtualSensorBlending get(VirtualSensorBlending blending) {
        return getById(blending.getId());
    }

    @Override
    public List<VirtualSensorBlending> getAll() {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<VirtualSensorBlending> criteriaQuery = cb.createQuery(VirtualSensorBlending.class);
        Root<VirtualSensorBlending> root = criteriaQuery.from(VirtualSensorBlending.class);
        criteriaQuery.select(root);
        List<VirtualSensorBlending> virtualSensors = data.getQuery(criteriaQuery);
        return virtualSensors;
    }

    @Override
    public void save(VirtualSensorBlending o) {
        data.save(o);
    }

    @Override
    public void update(VirtualSensorBlending o) {
        data.update(o);
    }

    @Override
    public void delete(VirtualSensorBlending o) {
        data.delete(o);
    }

}
