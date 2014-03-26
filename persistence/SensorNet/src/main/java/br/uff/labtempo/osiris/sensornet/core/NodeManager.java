/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.core;

import br.uff.labtempo.osiris.util.data.DataPacket;
import br.uff.labtempo.osiris.util.logging.Log;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 *
 * @author Felipe
 */
public class NodeManager implements OnTaskListener{

    private final Map<String, NodeTask> running;
    private final Map<String, Node> sleeping;
    private final ScheduledThreadPoolExecutor executorService;

    public NodeManager() {
        running = new Hashtable<>();
        sleeping = new Hashtable<>();
        executorService = new ScheduledThreadPoolExecutor(100);
    }

    public void onEventIncoming(DataPacket packet) {
        if (isRunning(packet)) {
            return;
        }

        if (isSleeping(packet)) {
            return;
        }

        createNode(packet);
    }

    private synchronized boolean isRunning(DataPacket packet) {
        if (running.containsKey(packet.getFullResourcePath())) {
            NodeTask task = running.get(packet.getFullResourcePath());
            Node node = task.getNode();
            node.update(packet);
            return true;
        }
        return false;
    }

    private synchronized boolean isSleeping(DataPacket packet) {
        if (sleeping.containsKey(packet.getFullResourcePath())) {
            Node node = sleeping.get(packet.getFullResourcePath());
            sleeping.remove(packet.getFullResourcePath());
            node.restart(packet);
            createTask(node);            
            return true;
        }
        return false;
    }

    private void createNode(DataPacket packet) {
        Node node = new Node(packet);
        createTask(node);
    }
    
    private synchronized void createTask(Node node){
        NodeTask task = new NodeTask(node, this);
        running.put(node.getFullResourcePath(),task);
        executorService.execute(task);
        node.setStatus(Node.Status.RUNNING);
        Log.D(node.getId()+ " node is running...");
    }

    @Override
    public synchronized void onTaskClose(Node node) {        
        running.remove(node.getFullResourcePath());
        sleeping.put(node.getFullResourcePath(),node);
        node.setStatus(Node.Status.SLEEPING);
        Log.D(node.getId()+" node is sleeping...");        
    }

}
