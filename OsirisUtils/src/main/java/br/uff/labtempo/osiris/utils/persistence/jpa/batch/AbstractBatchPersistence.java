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
package br.uff.labtempo.osiris.utils.persistence.jpa.batch;

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
public abstract class AbstractBatchPersistence implements BatchPersistence {

    private final EntityManager em;

    public AbstractBatchPersistence(EntityManager em) {
        this.em = em;
    }

    @Override
    public synchronized <T> T get(Class<T> entityType, Object key) {
        T object = em.find(entityType, key);
        return object;
    }

    @Override
    public synchronized <T> T getReference(Class<T> entityType, Object key) {
        T ref = em.getReference(entityType, key);
        return ref;
    }

    @Override
    public synchronized CriteriaBuilder getCriteriaBuilder() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        return cb;
    }

    @Override
    public synchronized <T> int getQuery(CriteriaDelete<T> query) {
        EntityManager EM = getEntityManager();
        EntityTransaction et = EM.getTransaction();

        et.begin();
        Query deleteQuery = EM.createQuery(query);
        int result = deleteQuery.executeUpdate();
        et.commit();

        return result;
    }

    @Override
    public synchronized <T> List<T> getQuery(CriteriaQuery<T> query) {
        TypedQuery<T> tquery = em.createQuery(query);
        List<T> result = tquery.getResultList();
        return result;
    }

    public synchronized <T> List<T> getQuery(CriteriaQuery<T> query, int limit) {
        TypedQuery<T> tquery = em.createQuery(query);
        List<T> result = tquery.setMaxResults(limit).getResultList();
        return result;
    }

    @Override
    public synchronized <T> T getQuerySingle(CriteriaQuery<T> query) {
        TypedQuery<T> tquery = em.createQuery(query);
        List<T> list = tquery.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return (T) list.get(0);
    }

    @Override
    public void close() throws Exception {
        try {
            em.close();
        } catch (Exception e) {
        }
    }

    protected EntityManager getEntityManager() {
        return em;
    }
}
