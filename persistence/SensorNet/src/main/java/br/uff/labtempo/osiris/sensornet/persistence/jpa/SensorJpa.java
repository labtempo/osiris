/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.persistence.jpa;

import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.persistence.SensorDao;
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
public class SensorJpa implements SensorDao<Sensor> {

    private final DataManager data;

    public SensorJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public Sensor get(Sensor o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sensor get(String networkId, String collectorId, String sensorId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Sensor> criteriaQuery = cb.createQuery(Sensor.class);
        Root<Sensor> root = criteriaQuery.from(Sensor.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<String>get("network"), networkId));
        predicates.add(cb.equal(root.<String>get("collector"), collectorId));
        predicates.add(cb.equal(root.<String>get("id"), sensorId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        return data.getQuerySingle(criteriaQuery);
    }

    @Override
    public List<Sensor> getAll(String networkId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Sensor> criteriaQuery = cb.createQuery(Sensor.class);
        Root<Sensor> root = criteriaQuery.from(Sensor.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<String>get("network"), networkId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        List<Sensor> sensors = data.getQuery(criteriaQuery);

        return sensors;
    }

    @Override
    public List<Sensor> getAll(String networkId, String collectorId) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Sensor> criteriaQuery = cb.createQuery(Sensor.class);
        Root<Sensor> root = criteriaQuery.from(Sensor.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<String>get("network"), networkId));
        predicates.add(cb.equal(root.<String>get("collector"), collectorId));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(root);
        List<Sensor> sensors = data.getQuery(criteriaQuery);

        return sensors;
    }

    @Override
    public void insert(Sensor o) {
        data.save(o);
    }

    @Override
    public void update(Sensor o) {
        data.update(o);
    }

    @Override
    public void delete(Sensor o) {
        data.delete(o);
    }

}
