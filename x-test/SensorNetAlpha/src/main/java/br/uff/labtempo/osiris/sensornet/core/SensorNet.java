/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.core;

import br.uff.labtempo.osiris.util.data.DataPacket;
import br.uff.labtempo.osiris.util.logging.Log;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SensorNet implements OnNodeManagerListener {

    //sensornet monitora a rede fisica
    //sensornet monitora todos os coletores
    private NodeManager manager;
    private OnSensorNetListener listener;

    public SensorNet(OnSensorNetListener listener) {
        this.manager = new NodeManager(this);
        this.listener = listener;
    }

    //sensornet conhece todos os nos da rede fisica
    //sensornet dispara eventos
    public void newSensorEvent(DataPacket packet) {
        Log.D(packet.getFullResourcePath() + " new packet arrived");
        manager.onEventIncoming(packet);
    }

    @Override
    public void onNodeChange(Node node, EventType type) {
        DataPacket packet = convertNodeToDataPacket(node);
        switch (type) {
            case NEWNETWORKFOUND:
                listener.onNewNetworkFound(packet);
                break;
            case NETWORKLOST:
                listener.onNetworkLost(packet);
                break;
            case NEWNODEFOUND:
                listener.onNewNodeFound(packet);
                break;
            case NODEDOWN:
                listener.onNodeDisable(packet);
                break;
            case NODEUP:
                listener.onNodeEnable(packet);
                break;
        }
    }
    
    private DataPacket convertNodeToDataPacket(Node node){
        DataPacket packet = DataPacket.create(node.getSource(), node.getId(), node.getParent(), node.getTimestampOfUpdate(), node.getDelayToUpdate(), node.getData());
        return packet;
    }
}
