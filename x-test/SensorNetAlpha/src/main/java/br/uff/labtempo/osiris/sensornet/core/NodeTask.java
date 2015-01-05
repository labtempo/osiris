/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.core;

import br.uff.labtempo.osiris.util.logging.Log;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
class NodeTask implements Runnable {

    private Node node;
    private OnTaskListener listener;

    public NodeTask(Node sensor, OnTaskListener listener) {
        this.node = sensor;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            while (true) {
                long time;
                synchronized (node) {
                    time = node.getRealisticDelayToUpdate();
                    Log.D(node.getId() + " next check at " + time + " milliseconds");
                }
                
                Thread.sleep(time);
                
                synchronized (node) {
                    Log.D(node.getId() + " checking...");
                    if (!node.isTimeUp()) {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(NodeTask.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            listener.onTaskClose(node);
        }
    }

    public Node getNode() {
        return node;
    }
}
