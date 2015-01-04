/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.persistence;

import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import java.util.List;

/**
 *
 * @author Felipe
 */
public interface SensorDao extends Dao<Sensor> {

    public Sensor get(Sensor o);

    public Sensor get(String networkId, String collectorId, String sensorId);

    public List<Sensor> getAll(String networkId);

    public List<Sensor> getAll(String networkId, String collectorId);
    
    public List<Sensor> getAllInactive(String networkId);

    public List<Sensor> getAllInactive(String networkId, String collectorId);

}
