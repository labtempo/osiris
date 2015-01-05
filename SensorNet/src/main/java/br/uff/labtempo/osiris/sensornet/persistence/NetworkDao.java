/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.persistence;

import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import java.util.List;

/**
 *
 * @author Felipe
 */
public interface NetworkDao extends Dao<Network> {

    public Network get(Network o);

    public Network get(String networkId);

    public List<Network> getAll();
}
