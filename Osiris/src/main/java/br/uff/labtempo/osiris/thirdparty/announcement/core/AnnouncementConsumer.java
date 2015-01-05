/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.thirdparty.announcement.core;

import br.uff.labtempo.omcp.client.OmcpClient;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Felipe
 */
class AnnouncementConsumer implements Runnable, AutoCloseable {

    private final BlockingQueue<AnnouncementItem> queue;
    private final OmcpClient client;
    private Thread consumerThread;

    AnnouncementConsumer(BlockingQueue<AnnouncementItem> queue, OmcpClient client) {
        this.queue = queue;
        this.client = client;
    }

    @Override
    public void run() {
        consumerThread = Thread.currentThread();
        consumerThread.setPriority(2);
        while (true) {
            try {
                AnnouncementItem announcement = queue.take();
                if (announcement != null && announcement.getObject() != null) {
                    publish(announcement);
                } else {
                    break;
                }
            } catch (InterruptedException ex) {
                break;
            }
        }
        try {
            client.close();
        } catch (Exception ex) {
        }
    }

    private void publish(AnnouncementItem announcement) {
        client.doNofity(announcement.getUri(), announcement.getObject());
    }

    @Override
    public void close() throws Exception {
        queue.add(new AnnouncementItem(null, null));
        consumerThread.join();
    }
}
