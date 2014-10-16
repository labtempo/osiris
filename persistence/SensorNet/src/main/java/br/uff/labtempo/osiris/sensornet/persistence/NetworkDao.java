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
public class NetworkDao implements DAO<NetworkWrapper> {

    private final RamStorage storage;

    public NetworkDao() {
        this.storage = RamStorage.getInstance();
    }

    @Override
    public List<NetworkWrapper> getAll(Object... args) {
        try {
            return new ArrayList(storage.getStorage().keySet());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public NetworkWrapper getById(String id, Object... args) {
        try {
            Map<NetworkWrapper, Map<CollectorWrapper, List<SensorWrapper>>> map = storage.getStorage();
            for (Map.Entry<NetworkWrapper, Map<CollectorWrapper, List<SensorWrapper>>> entry : map.entrySet()) {
                NetworkWrapper networkW = entry.getKey();
                if (networkW.getId().equals(id)) {
                    return networkW;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean insert(NetworkWrapper object, Object... args) {
        try {
            storage.addNetwork(object);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(NetworkWrapper objectA, NetworkWrapper objectB) {

        if (objectA.equalsInfo(objectB)) {
            objectA.update(objectB);
            return true;
        }
        return false;
    }
}
