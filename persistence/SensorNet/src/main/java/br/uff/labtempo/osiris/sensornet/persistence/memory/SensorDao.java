/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.persistence.memory;

import br.uff.labtempo.osiris.sensornet.model.CollectorWrapper;
import br.uff.labtempo.osiris.sensornet.model.NetworkWrapper;
import br.uff.labtempo.osiris.sensornet.model.SensorWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class SensorDao implements DAO<SensorWrapper> {

    private final RamStorage storage;

    public SensorDao() {
        this.storage = RamStorage.getInstance();
    }

    @Override
    public List<SensorWrapper> getAll(Object... args) {
        try {
            NetworkWrapper network = getNetworkFromArgs(args);
            CollectorWrapper collector = getCollectorFromArgs(args);
            Map<CollectorWrapper, List<SensorWrapper>> collectors = storage.getStorage().get(network);
            return collectors.get(collector);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public SensorWrapper getById(String id, Object... args) {
        try {
            NetworkWrapper network = getNetworkFromArgs(args);
            CollectorWrapper collector = getCollectorFromArgs(args);
            Map<CollectorWrapper, List<SensorWrapper>> collectors = storage.getStorage().get(network);

            for (SensorWrapper sensor : collectors.get(collector)) {
                if (sensor.getId().equals(id)) {
                    return sensor;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean insert(SensorWrapper object, Object... args) {
        try {
            NetworkWrapper network = getNetworkFromArgs(args);
            CollectorWrapper collector = getCollectorFromArgs(args);
            Map<CollectorWrapper, List<SensorWrapper>> collectors = storage.getStorage().get(network);
            List<SensorWrapper> sensors = collectors.get(collector);
            sensors.add(object);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(SensorWrapper objectA, SensorWrapper objectB) {

        if (objectA.equals(objectB)) {
            objectA.update(objectB);
            return true;
        }
        return false;

    }

    private NetworkWrapper getNetworkFromArgs(Object... args) {
        if (args == null) {
            throw new RuntimeException("Null args");
        }

        if (args.length == 0) {
            throw new RuntimeException("Empty args");
        }

        if (!(args[0] instanceof NetworkWrapper)) {
            throw new RuntimeException("Another instance in args");
        }

        for (Object obj : args) {
            if (obj instanceof NetworkWrapper) {
                return (NetworkWrapper) obj;
            }
        }
        throw new RuntimeException("Another instance in args");
    }

    private CollectorWrapper getCollectorFromArgs(Object... args) {
        if (args == null) {
            throw new RuntimeException("Null args");
        }

        if (args.length == 0) {
            throw new RuntimeException("Empty args");
        }

        for (Object obj : args) {
            if (obj instanceof CollectorWrapper) {
                return (CollectorWrapper) obj;
            }
        }
        throw new RuntimeException("Another instance in args");
    }
}
