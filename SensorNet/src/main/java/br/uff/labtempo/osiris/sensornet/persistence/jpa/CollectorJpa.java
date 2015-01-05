/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.persistence.jpa;

import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
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
 * @author Felipe
 */
public class CollectorJpa implements CollectorDao {

    private final DataManager data;

    public CollectorJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public Collector get(Collector o) {
        Collector collector = get(o.getNetwork().getId(), o.getId());
        return collector;
    }

    @Override
    public Collector get(String networkId, String collectorId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Collector> criteriaQuery = cb.createQuery(Collector.class);
        
        Root<Collector> root = criteriaQuery.from(Collector.class);        
        Join<Collector, Network> subroot = root.join("network");
        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(subroot.<String>get("id"), networkId));
        predicates.add(cb.equal(root.<String>get("id"), collectorId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));
        
        criteriaQuery.select(root);
        return data.getQuerySingle(criteriaQuery);
    }

    @Override
    public List<Collector> getAll(String networkId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Collector> criteriaQuery = cb.createQuery(Collector.class);
        Root<Collector> root = criteriaQuery.from(Collector.class);
        Join<Collector, Network> subroot = root.join("network");
        criteriaQuery.where(cb.equal(subroot.<String>get("id"), networkId));
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
        Join<Collector, Network> subroot = root.join("network");
        criteriaQuery.where(cb.equal(subroot.<String>get("id"), networkId));        
        criteriaQuery.where(cb.equal(root.<String>get("state"), ModelState.INACTIVE));
        criteriaQuery.select(root);
        List<Collector> collectors = data.getQuery(criteriaQuery);

        return collectors;
    }

}
