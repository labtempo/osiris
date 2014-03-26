/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.core;

import br.uff.labtempo.osiris.util.data.DataPacket;
import br.uff.labtempo.osiris.util.logging.Log;
import java.io.Serializable;

/**
 *
 * @author Felipe
 */
public class Node implements Serializable {

    private String id;
    private String source;
    private String parent;
    private String role;
    private long timestampOfUpdate;
    private long intervalToUpdate;
    private long delayToUpdate;
    private long timePrecisionOfSensorReading = 2;//milliseconds
    private String data;
    private transient long oldTimestampOfUpdate;
    private transient long systemTimeOfUpdate;
    private transient long oldSystemTimeOfUpdate;
    private transient Status status;

    public Node(DataPacket packet) {
        this.source = packet.getSourceId();
        this.id = packet.getId();
        this.parent = packet.getParent();
        this.role = packet.getRole();
        this.timestampOfUpdate = packet.getTimestamp();
        this.intervalToUpdate = packet.getInterval();
        this.delayToUpdate = packet.getDelay();
        this.data = packet.getData();
        this.oldTimestampOfUpdate = this.timestampOfUpdate - packet.getInterval();
        this.systemTimeOfUpdate = getSystemMillis();
        this.oldSystemTimeOfUpdate = systemTimeOfUpdate - intervalToUpdate;
    }

    private long getSystemMillis() {
        return System.currentTimeMillis();
    }

    public boolean isSensor() {
        if (data == null || data.isEmpty()) {
            return false;
        }
        return true;
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getParent() {
        return parent;
    }

    public String getRole() {
        return role;
    }

    public long getTimestampOfUpdate() {
        return timestampOfUpdate;
    }

    public long getIntervalToUpdate() {
        return intervalToUpdate;
    }

    public long getDelayToUpdate() {
        return delayToUpdate;
    }

    public synchronized long getRealisticDelayToUpdate() {
        long time = systemTimeOfUpdate + intervalToUpdate;

        long system = getSystemMillis();

        time -= system - 5;

        if (time <= 0) {
            throw new ArithmeticException("Shouldn't generate zero or negative number: " + time);
        }

        return time;
    }

    public long getSystemTimeOfUpdate() {
        return systemTimeOfUpdate;
    }

    public String getData() {
        return data;
    }

    public synchronized void update(DataPacket packet) {
        if (packet.getTimestamp() != timestampOfUpdate) {            
            this.oldTimestampOfUpdate = this.timestampOfUpdate;
            this.timestampOfUpdate = packet.getTimestamp();
            this.intervalToUpdate = packet.getInterval();
            this.delayToUpdate = packet.getDelay();
            this.data = packet.getData();
            this.oldSystemTimeOfUpdate = systemTimeOfUpdate;
            this.systemTimeOfUpdate = getSystemMillis();
            Log.D(id + " updating...");
        }
    }

    public synchronized void restart(DataPacket packet) {
        this.timestampOfUpdate = packet.getTimestamp() - packet.getInterval();
        this.systemTimeOfUpdate = getSystemMillis() - packet.getInterval();
        update(packet);
        this.intervalToUpdate += this.intervalToUpdate/4;
        Log.D(id + " restarting...");
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public synchronized boolean isTimeUp() {
        long delay = oldSystemTimeOfUpdate - systemTimeOfUpdate;

        if (delay > delayToUpdate) {
            Log.D(id + " exiting by message delay: " + delay + " of " + delayToUpdate);
            return false;
        }

        long diff = timestampOfUpdate - oldTimestampOfUpdate;
        long diff2 = Math.abs(diff - intervalToUpdate);

        if (diff2 > timePrecisionOfSensorReading) {
            Log.D(id + " exiting by sensor reading delay: " + diff2);
            return false;
        }

        return true;
    }

    public String getFullResourcePath() {
        return source + "." + id;
    }

    public enum Status {

        RUNNING, SLEEPING
    }
}
