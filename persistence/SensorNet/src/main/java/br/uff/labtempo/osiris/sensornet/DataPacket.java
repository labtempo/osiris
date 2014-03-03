/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet;

/**
 *
 * @author Felipe
 */
class DataPacket {

    String sensorID;
    long timestamp;
    String measures;

    @Override
    public String toString() {
        StringBuilder packet = new StringBuilder();

        packet.append("id:");
        packet.append(sensorID);
        packet.append(";time:");
        packet.append(timestamp);
        packet.append(";measures:{");
        packet.append(measures);
        packet.append("}}");
        return packet.toString();
    }

}
