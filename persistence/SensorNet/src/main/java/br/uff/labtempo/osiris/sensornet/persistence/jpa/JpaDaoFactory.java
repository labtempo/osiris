/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.persistence.jpa;

import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.persistence.CollectorDao;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.sensornet.persistence.SensorDao;

/**
 *
 * @author Felipe
 */
public class JpaDaoFactory implements DaoFactory, AutoCloseable {

    DataManager data;

    public JpaDaoFactory() throws Exception {
        data = new DataManager();
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

}
