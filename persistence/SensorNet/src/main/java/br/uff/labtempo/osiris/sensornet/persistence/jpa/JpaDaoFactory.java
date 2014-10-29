/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.persistence.jpa;

import br.uff.labtempo.osiris.sensornet.announcer.Announcer;
import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.persistence.AnnouncerDao;
import br.uff.labtempo.osiris.sensornet.persistence.CollectorDao;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.sensornet.persistence.SensorDao;

/**
 *
 * @author Felipe
 */
public class JpaDaoFactory implements DaoFactory, AutoCloseable {

    public static DataManager data;
    public static Announcer announcer;

    public JpaDaoFactory() throws Exception {
        if (data == null) {
            data = new DataManager();
        }
    }

    public JpaDaoFactory(String ip, String usr, String pwd) throws Exception {
        this();
        if (announcer == null) {
            announcer = new Announcer(ip, usr, pwd);
        }
    }

    @Override
    public SensorDao<Sensor> getSensorDao() {
        return new SensorJpa(data);
    }

    @Override
    public CollectorDao<Collector> getCollectorDao() {
        return new CollectorJpa(data);
    }

    @Override
    public NetworkDao<Network> getNetworkDao() {
        return new NetworkJpa(data);
    }

    @Override
    public void close() throws Exception {
        data.close();
    }

    @Override
    public AnnouncerDao getAnnouncerDao() {
        if (announcer == null) {
            throw new RuntimeException("Announcer is null");
        }
        return announcer.getProducer();
    }

}
