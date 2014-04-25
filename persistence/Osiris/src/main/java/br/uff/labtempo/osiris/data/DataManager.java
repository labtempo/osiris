/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.data;

import br.uff.labtempo.osiris.util.components.Component;
import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.logging.Log;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Felipe
 */
public class DataManager extends Component {

    private EntityManagerFactory EMF;
    private EntityManager EM;

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            EMF = Persistence.createEntityManagerFactory("Standalone");
            EM = EMF.createEntityManager();
        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }
    }

    @Override
    protected void onStop() {
        if (EMF.isOpen()) {
            Log.D("Closing DataManager");
            EM.close();
            EMF.close();
        }
    }

    public void save(Object o) {
        EntityTransaction et = EM.getTransaction();
        et.begin();
        EM.persist(o);
        et.commit();
    }

    public <T> T get(Class<T> entityType, Object key) {
        return EM.find(entityType, key);
    }

    public <T> T getReference(Class<T> entityType, Object key) {
        return EM.getReference(entityType, key);
    }

    public void update(Object o) {
        EntityTransaction et = EM.getTransaction();
        et.begin();
        EM.merge(o);
        et.commit();
    }

    public void delete(Object o) {
        EntityTransaction et = EM.getTransaction();
        et.begin();
        EM.remove(o);
        et.commit();
    }

    public <T> List<T> getNull(Class<T> entityType, String field) {
        CriteriaBuilder cb = EMF.getCriteriaBuilder();
        CriteriaQuery<T> c = cb.createQuery(entityType);
        Root<T> item = c.from(entityType);
        c.select(item);

        Predicate predicate = cb.isNull(item.get(field));
        c.where(predicate);

        TypedQuery<T> query = EM.createQuery(c);
        List<T> result = query.getResultList();
        return result;
    }

    public <T> List<T> getNotNull(Class<T> entityType, String field) {
        CriteriaBuilder cb = EMF.getCriteriaBuilder();

        CriteriaQuery<T> c = cb.createQuery(entityType);
        Root<T> item = c.from(entityType);
        c.select(item);
        Predicate predicate = cb.isNotNull(item.get(field));
        c.where(predicate);

        TypedQuery<T> query = EM.createQuery(c);
        List<T> result = query.getResultList();
        return result;
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return EMF.getCriteriaBuilder();
    }
    
    public <T> List<T> getQuery(CriteriaQuery<T> query) {
        TypedQuery<T> tquery = EM.createQuery(query);
        List<T> result = tquery.getResultList();
        return result;
    }
}
