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

import br.uff.labtempo.osiris.sensornet.persistence.CollectorDao;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.sensornet.persistence.SchedulerDao;
import br.uff.labtempo.osiris.sensornet.persistence.SensorDao;
import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistence;
import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistenceAutoCommitted;
import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistenceCommitBySecond;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class JpaDaoFactory implements DaoFactory, AutoCloseable {

    private static JpaDaoFactory instance;
    //@PersistenceContext
    private EntityManagerFactory emf;
    private BatchPersistence batchPersistence;
    private ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();

    private JpaDaoFactory(EntityManagerFactory emf) throws Exception {
        try {
            this.emf = emf;
            this.batchPersistence = new BatchPersistenceAutoCommitted(emf.createEntityManager());
        } catch (Exception ex) {
            close();
            throw ex;
        }
    }

    public static JpaDaoFactory newInstance(EntityManagerFactory emf) throws Exception {
        if (instance == null) {
            instance = new JpaDaoFactory(emf);

        }
        return instance;
    }

    public static JpaDaoFactory getInstance() {
        if (instance == null) {
            throw new RuntimeException("Factory need be created as a new instance!");
        }
        return instance;
    }

    @Override
    public void close() throws Exception {
        try {
            batchPersistence.close();
        } catch (Exception e) {
        }
        try {
            emf.close();
        } catch (Exception e) {
        }
    }

    @Override
    public SensorDao getSensorDao() {
        return new SensorJpa(getDataManager());
    }

    @Override
    public CollectorDao getCollectorDao() {
        return new CollectorJpa(getDataManager());
    }

    @Override
    public NetworkDao getNetworkDao() {
        return new NetworkJpa(getDataManager());
    }

    @Override
    public SchedulerDao getSchedulerDao() {
        return new SchedulerJpa(batchPersistence);
    }

    @Override
    public BatchPersistence getBatchPersistence() {
        return batchPersistence;
    }
    
    @Override
    public void clear() {
        EntityManager em = threadLocal.get();
        if (em != null) {
            em.close();
            threadLocal.set(null);
        }
    }

    private DataManager getDataManager() {
        return new DataManager(this);
    }

    public EntityManager getEntityManager() {
        EntityManager em = threadLocal.get();
        if (em == null) {
            em = getNewEntityManager();
            threadLocal.set(em);
        }
        return em;
    }

    private EntityManager getNewEntityManager() {
        return emf.createEntityManager();
    }    
}
