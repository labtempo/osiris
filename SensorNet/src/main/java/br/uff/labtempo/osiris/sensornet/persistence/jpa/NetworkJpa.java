/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Felipe
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
