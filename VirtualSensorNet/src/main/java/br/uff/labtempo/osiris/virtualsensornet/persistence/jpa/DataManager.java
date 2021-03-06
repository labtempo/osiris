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

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
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

    public synchronized void save(Object o) {
        EntityManager EM = getEntityManager();
        EntityTransaction et = EM.getTransaction();
        et.begin();
        EM.persist(o);
        et.commit();
    }

    public synchronized <T> T get(Class<T> entityType, Object key) {
        EntityManager EM = getEntityManager();
        T object = EM.find(entityType, key);
        return object;
    }

    public synchronized <T> T getReference(Class<T> entityType, Object key) {
        EntityManager EM = getEntityManager();
        return EM.getReference(entityType, key);
    }

    public synchronized void update(Object o) {
        EntityManager EM = getEntityManager();
        EntityTransaction et = EM.getTransaction();
        et.begin();
        EM.merge(o);
        et.commit();
    }

    public synchronized void delete(Object o) {
        EntityManager EM = getEntityManager();
        EntityTransaction et = EM.getTransaction();
        et.begin();
        EM.remove(o);
        et.commit();
    }

    public synchronized CriteriaBuilder getCriteriaBuilder() {
        EntityManager EM = getEntityManager();
        return EM.getCriteriaBuilder();
    }
    
    public synchronized <T> int getQuery(CriteriaDelete<T> query) {
        EntityManager EM = getEntityManager();
        EntityTransaction et = EM.getTransaction();  
        Query deleteQuery = EM.createQuery(query);
        et.begin();
        int result = deleteQuery.executeUpdate();
        et.commit();        
        return result;
    }

    public synchronized <T> List<T> getQuery(CriteriaQuery<T> query) {
        EntityManager EM = getEntityManager();
        TypedQuery<T> tquery = EM.createQuery(query);
        List<T> result = tquery.getResultList();
        return result;
    }
    
    public synchronized <T> List<T> getQuery(CriteriaQuery<T> query, int limit) {
        EntityManager EM = getEntityManager();
        TypedQuery<T> tquery = EM.createQuery(query);
        List<T> result = tquery.setMaxResults(limit).getResultList();
        return result;
    }

    public synchronized <T> T getQuerySingle(CriteriaQuery<T> query) {
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
        return EM.createNativeQuery(sql, klass);
    }

    private EntityManager getEntityManager() {
        return factory.getEntityManager();
    }
}
