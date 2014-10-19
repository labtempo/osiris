/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model;

import br.uff.labtempo.osiris.sensornet.model.state.Model;
import br.uff.labtempo.osiris.collector.temp.Network;

/**
 *
 * @author Felipe
 */
public class NetworkWrapper extends Model<NetworkWrapper> {

    private Network network;

    public NetworkWrapper(Network network) {
        this.network = network;
    }

    public Object getId() {
        return network.getId();
    }

    @Override
    public boolean equals(Object obj) {
        return network.equals(obj);
    }

    public boolean equalsInfo(NetworkWrapper obj) {
        if (equals(obj) && network.getInfo().equals(obj.network.getInfo())) {
            return true;
        }
        return false;
    }

    @Override
    public void update(NetworkWrapper obj) {
        if (!network.getInfo().equals(obj.network.getInfo())) {
            network = obj.network;
            super.update();
        }
    }

    public Network getContent() {
        return network;
    }

}
