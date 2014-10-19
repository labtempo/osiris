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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class RamStorage {

    private static RamStorage instance;
    private Map<NetworkWrapper, Map<CollectorWrapper, List<SensorWrapper>>> storage;

    private RamStorage() {
        storage = Collections.synchronizedMap(new HashMap<NetworkWrapper, Map<CollectorWrapper, List<SensorWrapper>>>());
    }

    public static RamStorage getInstance() {
        if (instance == null) {
            instance = new RamStorage();
        }
        return instance;
    }

    public Map<NetworkWrapper, Map<CollectorWrapper, List<SensorWrapper>>> getStorage() {
        return storage;
    }

    public Map<CollectorWrapper, List<SensorWrapper>> addNetwork(NetworkWrapper network) {
        if (!storage.containsKey(network)) {
            return storage.put(network, Collections.synchronizedMap(new HashMap<CollectorWrapper, List<SensorWrapper>>()));
        }
        return null;

    }

    public List<SensorWrapper> addCollector(Map<CollectorWrapper, List<SensorWrapper>> collectors, CollectorWrapper collector) {
        if (!collectors.containsKey(collector)) {
            collectors.put(collector, Collections.synchronizedList(new ArrayList<SensorWrapper>()));
            return collectors.get(collector);
        }
        throw new RuntimeException("Network is not exist!");
    }
}
