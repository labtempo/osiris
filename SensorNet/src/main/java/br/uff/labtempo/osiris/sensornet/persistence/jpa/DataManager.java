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
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class DataManager {

    private final JpaDaoFactory factory;

    public DataManager(JpaDaoFactory factory) {
        this.factory = factory;
    }

    public void save(Object o) {
        EntityManager EM = getEntityManager();
        EntityTransaction et = EM.getTransaction();
        et.begin();
        EM.persist(o);
        et.commit();
    }

    public <T> T get(Class<T> entityType, Object key) {
        EntityManager EM = getEntityManager();
        T object = EM.find(entityType, key);
        return object;
    }

    public <T> T getReference(Class<T> entityType, Object key) {
        EntityManager EM = getEntityManager();
        T ref = EM.getReference(entityType, key);
        return ref;
    }

    public void update(Object o) {
        EntityManager EM = getEntityManager();
        EntityTransaction et = EM.getTransaction();
        et.begin();
        EM.merge(o);
        et.commit();
        //EM.close();
    }

    public void delete(Object o) {
        EntityManager EM = getEntityManager();
        EntityTransaction et = EM.getTransaction();
        et.begin();
        EM.remove(EM.contains(o) ? o : EM.merge(o));
        et.commit();
        //EM.close();
    }

    public CriteriaBuilder getCriteriaBuilder() {
        EntityManager EM = getEntityManager();
        CriteriaBuilder cb = EM.getCriteriaBuilder();
        return cb;
    }

    public <T> List<T> getQuery(CriteriaQuery<T> query) {
        EntityManager EM = getEntityManager();
        TypedQuery<T> tquery = EM.createQuery(query);
        List<T> result = tquery.getResultList();
        return result;
    }

    public <T> T getQuerySingle(CriteriaQuery<T> query) {
        EntityManager EM = getEntityManager();
        TypedQuery<T> tquery = EM.createQuery(query);
        List<T> list = tquery.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return (T) list.get(0);
        //return (T) tquery.getSingleResult();
    }

    public Query getQquery(String sql, Class klass) {
        EntityManager EM = getEntityManager();
        Query query = EM.createNativeQuery(sql, klass);
        return query;
    }

    public synchronized <T> List<T> getQuery(CriteriaQuery<T> query, int limit) {
        EntityManager EM = getEntityManager();
        TypedQuery<T> tquery = EM.createQuery(query);
        List<T> result = tquery.setMaxResults(limit).getResultList();
        return result;
    }

    private EntityManager getEntityManager() {
        return factory.getEntityManager();
    }
}
