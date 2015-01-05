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

import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class NetworkJpa implements NetworkDao {

    private final DataManager data;

    public NetworkJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public Network get(Network o) {
        return get(o.getId());
    }

    @Override
    public Network get(String networkId) {
        return data.get(Network.class, networkId);
    }

    @Override
    public List<Network> getAll() {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Network> criteriaQuery = cb.createQuery(Network.class);
        Root<Network> root = criteriaQuery.from(Network.class);
        criteriaQuery.select(root);
        List<Network> networks = data.getQuery(criteriaQuery);

        return networks;
    }

    @Override
    public void save(Network o) {
        data.save(o);
    }

    @Override
    public void update(Network o) {
        data.update(o);
    }

    @Override
    public void delete(Network o) {
        data.delete(o);
    }

}
