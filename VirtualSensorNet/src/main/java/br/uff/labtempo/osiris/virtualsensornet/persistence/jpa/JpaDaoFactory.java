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

import br.uff.labtempo.osiris.virtualsensornet.persistence.ConverterDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.LinkDao;
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

    public static final ThreadLocal<EntityManager> LOCAL = new ThreadLocal();

    private JpaDaoFactory(EntityManagerFactory emf) throws Exception {
        try {
            this.emf = emf;
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
            emf.close();
        } catch (Exception e) {
        }
    }

    @Override
    public LinkDao getLinkDao() {
        return new LinkJpa(getDataManager());
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
    public SchedulerDao getSchedulerDao() {
        return new SchedulerJpa(getDataManager());
    }

    @Override
    public VirtualSensorDao getVirtualSensorDao() {
        return new VirtualSensorJpa(getDataManager());
    }

    private DataManager getDataManager() {
        DataManager dataManager = new DataManager(getEntityManager());
        return dataManager;
    }

    private EntityManager getEntityManager() {
//        EntityManager em = LOCAL.get();
//        if (em == null) {
//            em = emf.createEntityManager();
//            LOCAL.set(em);
//        }
        return emf.createEntityManager();
    }

    public void flush() {
        EntityManager em = LOCAL.get();
        if (em != null) {
            em.close();
        }
        LOCAL.remove();
    }

}
