/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.persistence.jpa;

import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorLink;
import br.uff.labtempo.osiris.virtualsensornet.persistence.LinkDao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Felipe
 */
public class LinkJpa implements LinkDao {

    private final DataManager data;

    public LinkJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public List<VirtualSensorLink> getAll(String networkId, String collectorId, String sensorId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<VirtualSensorLink> criteriaQuery = cb.createQuery(VirtualSensorLink.class);
        Root<VirtualSensorLink> root = criteriaQuery.from(VirtualSensorLink.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<String>get("networkId"), networkId));
        predicates.add(cb.equal(root.<String>get("collectorId"), collectorId));
        predicates.add(cb.equal(root.<String>get("sensorId"), sensorId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);

        List<VirtualSensorLink> links = data.getQuery(criteriaQuery);
        return links;
    }

    @Override
    public void save(VirtualSensorLink o) {
        data.save((VirtualSensor) o);
    }

    @Override
    public void update(VirtualSensorLink o) {
        data.update((VirtualSensor) o);
    }

    @Override
    public void delete(VirtualSensorLink o) {
        data.delete((VirtualSensor) o);
    }

}
