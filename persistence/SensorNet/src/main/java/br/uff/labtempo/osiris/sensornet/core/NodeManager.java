/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.core;

import br.uff.labtempo.osiris.util.data.DataPacket;
import br.uff.labtempo.osiris.util.logging.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 *
 * @author Felipe
 */
class NodeManager implements OnTaskListener {

    private final Map<String, NodeTask> running;
    private final Map<String, Node> sleeping;
    private final Map<String, Integer> networks;
    private final ScheduledThreadPoolExecutor executorService;
    private OnNodeManagerListener listener;

    public NodeManager(OnNodeManagerListener listener) {
        this.running = new HashMap<>();
        this.sleeping = new HashMap<>();
        this.networks = new HashMap<>();
        this.executorService = new ScheduledThreadPoolExecutor(100);
        this.listener = listener;
    }

    public void onEventIncoming(DataPacket packet) {
        Log.D(" ** " + packet.getId() + " distributing ** ");

        if (isRunning(packet)) {
            return;
        }

        if (isSleeping(packet)) {
            return;
        }
        
        
        Node node = new Node(packet);
        listener.onNodeChange(node, EventType.NEWNODEFOUND);
        createTask(node);        
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

    private synchronized void createTask(Node node) {
        NodeTask task = new NodeTask(node, this);
        running.put(node.getFullResourcePath(), task);
        executorService.execute(task);
        node.setStatus(Node.Status.RUNNING);

        if (!networks.containsKey(node.getSource())) {
            networks.put(node.getSource(), 1);
            listener.onNodeChange(node, EventType.NEWNETWORKFOUND);
        } else {
            networks.put(node.getSource(), networks.get(node.getSource()) + 1);
        }
        
        listener.onNodeChange(node, EventType.NODEUP);
        
        Log.D(node.getId() + " nodeTask is created!");
    }

    @Override
    public synchronized void onTaskClose(Node node) {
        running.remove(node.getFullResourcePath());
        sleeping.put(node.getFullResourcePath(), node);
        node.setStatus(Node.Status.SLEEPING);
        listener.onNodeChange(node, EventType.NODEDOWN);

        if (networks.containsKey(node.getSource())) {
            if (networks.get(node.getSource()) <= 1) {
                networks.remove(node.getSource());
                listener.onNodeChange(node, EventType.NETWORKLOST);

            } else {
                networks.put(node.getSource(), networks.get(node.getSource()) - 1);
            }
        }

        Log.D(node.getId() + " nodeTask is finished!");
    }

}
