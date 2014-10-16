/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Felipe
 */
public class ContentProvider {

    private List<Network> networks;

    public ContentProvider() {
        networks = Collections.synchronizedList(new ArrayList<Network>());
    }

    public List<Network> getNetworks() {
        return networks;
    }

    public Sensor getSensorById(String networkId, String sensorId) {
        Network network = findNetworkById(networkId);
        if (network == null) {
            return null;
        }
        Sensor sensor = findSensorById(sensorId, network);
        if (sensor == null) {
            return null;
        }
        return sensor;

    }

    public List<Sensor> getSensorsByNetworkId(String networkId) {
        Network network = findNetworkById(networkId);
        if (network != null) {
            return network.getSensors();
        }
        return null;
    }

    private Network findNetworkById(String networkId) {
        for (Network network : networks) {
            if (network.getId().equals(networkId)) {
                return network;
            }
        }
        return null;
    }

    private Sensor findSensorById(String sensorId, Network network) {
        for (Sensor sensor : network.getSensors()) {
            if (sensor.getId().equals(sensorId)) {
                return sensor;
            }
        }
        return null;
    }

}
