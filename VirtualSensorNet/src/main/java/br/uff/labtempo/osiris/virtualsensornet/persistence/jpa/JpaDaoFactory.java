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

import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistence;
import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistenceAutoCommitted;
import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistenceCommitBySecond;
import br.uff.labtempo.osiris.virtualsensornet.persistence.BlendingDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.CompositeDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.ConverterDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.FieldDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.FunctionDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.LinkDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.RevisionDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.SchedulerDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.VirtualSensorDao;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class JpaDaoFactory implements DaoFactory, AutoCloseable {

    private static JpaDaoFactory instance;
    private EntityManagerFactory emf;
    private BatchPersistence batchPersistence;
    private BatchPersistence ultraPersistence;
    private ThreadLocal<EntityManager> threadLocal = new ThreadLocal();

    private JpaDaoFactory(EntityManagerFactory emf) throws Exception {
        try {
            this.emf = emf;
            this.batchPersistence = new BatchPersistenceAutoCommitted(emf.createEntityManager());
            this.ultraPersistence = new BatchPersistenceCommitBySecond(emf.createEntityManager());
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
            ultraPersistence.close();
        } catch (Exception e) {
        }
        try {
            emf.close();
        } catch (Exception e) {
        }
    }

    @Override
    public VirtualSensorDao getVirtualSensorDao() {
        return new VirtualSensorJpa(getDataManager());
    }

    @Override
    public RevisionDao getRevisionDao() {
        return new RevisionJpa(getDataManager());
    }

    @Override
    public RevisionDao getUltraRevisionDao() {
        return new RevisionJpaPersistent(ultraPersistence);
    }

    @Override
    public LinkDao getLinkDao() {
        return new LinkJpa(getDataManager());
    }

    @Override
    public LinkDao getPersistentLinkDao() {
        return new LinkJpaPersistent(batchPersistence);
    }

    @Override
    public CompositeDao getCompositeDao() {
        return new CompositeJpa(getDataManager());
    }

    @Override
    public CompositeDao getPersistentCompositeDao() {
        return new CompositeJpaPersistent(batchPersistence);
    }

    @Override
    public BlendingDao getBlendingDao() {
        return new BlendingJpa(getDataManager());
    }

    @Override
    public BlendingDao getPersistentBlendingDao() {
        return new BlendingJpaPersistent(batchPersistence);
    }

    @Override
    public FieldDao getPersistentFieldDao() {
        return new FieldJpaPersistent(batchPersistence);
    }

    @Override
    public FieldDao getFieldDao() {
        return new FieldJpa(getDataManager());
    }

    @Override
    public ConverterDao getConverterDao() {
        return new ConverterJpa(getDataManager());
    }

    @Override
    public DataTypeDao getDataTypeDao() {
        return new DataTypeJpa(getDataManager());
    }

    @Override
    public FunctionDao getFunctionDao() {
        return new FunctionJpa(getDataManager());
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
