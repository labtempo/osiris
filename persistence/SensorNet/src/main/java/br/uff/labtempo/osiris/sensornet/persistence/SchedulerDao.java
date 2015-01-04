/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.persistence;

import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;

/**
 *
 * @author Felipe
 */
public interface SchedulerDao {
    void schedule(Sensor sensor);
}
