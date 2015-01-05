/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.persistence;

import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import java.util.List;

/**
 *
 * @author Felipe
 */
public interface CollectorDao extends Dao<Collector> {

    public Collector get(Collector o);

    public Collector get(String networkId, String collectorId);

    public List<Collector> getAll(String networkId);

    public List<Collector> getAllInactive(String networkId);
}
