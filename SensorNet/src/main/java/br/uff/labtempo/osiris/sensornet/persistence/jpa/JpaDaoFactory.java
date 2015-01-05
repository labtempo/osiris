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

import br.uff.labtempo.osiris.sensornet.persistence.AnnouncerDao;
import br.uff.labtempo.osiris.sensornet.persistence.CollectorDao;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.sensornet.persistence.SchedulerDao;
import br.uff.labtempo.osiris.sensornet.persistence.SensorDao;
import br.uff.labtempo.osiris.sensornet.thirdparty.announcer.AnnouncementBootstrap;
import br.uff.labtempo.osiris.sensornet.thirdparty.scheduler.SchedulerBootstrap;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class JpaDaoFactory implements DaoFactory, AutoCloseable {

    private static JpaDaoFactory instance;

    private DataManager data;
    private AnnouncementBootstrap announcementConfig;
    private SchedulerBootstrap schedulingConfig;

    private JpaDaoFactory(String ip, String usr, String pwd, String moduleName) throws Exception {
        try {
            announcementConfig = new AnnouncementBootstrap(ip, usr, pwd, moduleName);
            data = new DataManager();
            schedulingConfig = new SchedulerBootstrap(data, this);           
        } catch (Exception ex) {
            close();
            throw ex;
        }
    }

    public static JpaDaoFactory newInstance(String ip, String usr, String pwd, String moduleName) throws Exception {
        if (instance == null) {
            instance = new JpaDaoFactory(ip, usr, pwd, moduleName);
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
    public SensorDao getSensorDao() {
        return new SensorJpa(data);
    }

    @Override
    public CollectorDao getCollectorDao() {
        return new CollectorJpa(data);
    }

    @Override
    public NetworkDao getNetworkDao() {
        return new NetworkJpa(data);
    }

    @Override
    public void close() throws Exception {
        try {

            schedulingConfig.close();
        } catch (Exception e) {
        }
        try {
            announcementConfig.close();
        } catch (Exception e) {
        }
        try {
            data.close();
        } catch (Exception e) {
        }
    }

    @Override
    public AnnouncerDao getAnnouncerDao() {
        return announcementConfig.getAnnouncer();
    }

    @Override
    public SchedulerDao getSchedulerDao() {
        return schedulingConfig.getScheduler();
    }

}
