/*
 * Copyright 2015 Felipe.
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

import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class BatchPersistenceAutoCommitted extends AbstractBatchPersistence {

    public BatchPersistenceAutoCommitted(EntityManager em) {
        super(em);
    }

    @Override
    public synchronized void save(Object o) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(o);
        em.getTransaction().commit();
    }

    @Override
    public synchronized void update(Object o) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(o);
        em.getTransaction().commit();
    }

    @Override
    public synchronized void delete(Object o) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.remove(em.contains(o) ? o : em.merge(o));
        em.getTransaction().commit();
    }

    @Override
    public void forceCommit() {
    }
}
