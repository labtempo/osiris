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
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class DataManager implements AutoCloseable {

    private EntityManagerFactory EMF;
    private EntityManager EM;

    public DataManager() throws Exception {
        EMF = Persistence.createEntityManagerFactory("Standalone");
        EM = EMF.createEntityManager();
    }

    @Override
    public void close() throws Exception {
        if (EMF.isOpen()) {
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

    public <T> T getQuerySingle(CriteriaQuery<T> query) {
        TypedQuery<T> tquery = EM.createQuery(query);
        List<T> list = tquery.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return (T)list.get(0);
        //return (T) tquery.getSingleResult();
    }
}
