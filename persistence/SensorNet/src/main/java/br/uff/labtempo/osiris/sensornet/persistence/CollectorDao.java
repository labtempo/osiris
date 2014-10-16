/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.persistence;

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
public class CollectorDao implements DAO<CollectorWrapper> {

    private final RamStorage storage;

    public CollectorDao() {
        this.storage = RamStorage.getInstance();
    }

    @Override
    public List<CollectorWrapper> getAll(Object... args) {
        try {
            NetworkWrapper network = getNetworkFromArgs(args);
            Map<CollectorWrapper, List<SensorWrapper>> collectors = storage.getStorage().get(network);
            return new ArrayList(collectors.keySet());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CollectorWrapper getById(String id, Object... args) {
        try {
            NetworkWrapper network = getNetworkFromArgs(args);
            Map<CollectorWrapper, List<SensorWrapper>> collectors = storage.getStorage().get(network);

            for (Map.Entry<CollectorWrapper, List<SensorWrapper>> entry : collectors.entrySet()) {
                if (entry.getKey().getId().equals(id)) {
                    return entry.getKey();
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean insert(CollectorWrapper collector, Object... args) {
        try {
            NetworkWrapper network = getNetworkFromArgs(args);
            Map<CollectorWrapper, List<SensorWrapper>> collectors = storage.getStorage().get(network);
            storage.addCollector(collectors, collector);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(CollectorWrapper collectorA, CollectorWrapper collectorB) {

        if (collectorA.equalsInfo(collectorB)) {
            collectorA.update(collectorB);
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

        return (NetworkWrapper) args[0];
    }

}
