/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Felipe
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
