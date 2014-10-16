/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.osiris.sensornet.core;

import br.uff.labtempo.osiris.util.data.DataPacket;

/**
 *
 * @author Felipe
 */
public interface OnSensorNetListener {
    void onNodeDisable(DataPacket packet);
    void onNodeEnable(DataPacket packet);
    void onNewNetworkFound(DataPacket packet);
    void onNewNodeFound(DataPacket packet);

    public void onNetworkLost(DataPacket packet);
}
