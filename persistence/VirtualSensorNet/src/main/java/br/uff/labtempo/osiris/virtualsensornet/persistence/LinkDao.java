/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.persistence;

import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorLink;
import java.util.List;

/**
 *
 * @author Felipe
 */
public interface LinkDao extends Dao<VirtualSensorLink>{
    public List<VirtualSensorLink> getAll(String networkId, String collectorId, String sensorId);
}
