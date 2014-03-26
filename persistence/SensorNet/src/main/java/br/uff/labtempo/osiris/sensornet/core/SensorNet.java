/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.core;

import br.uff.labtempo.osiris.util.data.DataPacket;
import br.uff.labtempo.osiris.util.logging.Log;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe
 */
public class SensorNet {

    //sensornet monitora a rede fisica
    //sensornet monitora todos os coletores
   private NodeManager manager;

    public SensorNet() {
        manager = new NodeManager();
    }

    //sensornet conhece todos os nos da rede fisica
    //sensornet dispara eventos
    public void newSensorEvent(DataPacket packet) {
        manager.onEventIncoming(packet);
        Log.D(packet.getFullResourcePath() + " new packet");
    }
}