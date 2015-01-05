/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.thirdparty.announcement.core;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.osiris.thirdparty.announcement.Announcement;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Felipe
 */
public class AnnouncementManager implements Announcement {

    private final BlockingQueue<AnnouncementItem> queue;
    private AnnouncementConsumer consumer;
    private final OmcpClient client;

    public AnnouncementManager(OmcpClient client) {
        this.queue = new LinkedBlockingQueue<>();
        this.client = client;
    }

    @Override
    public void initialize() {        
        consumer = new AnnouncementConsumer(queue, client);
        createThreadConsumer(consumer);
    }

    private void createThreadConsumer(AnnouncementConsumer consumer) {
        Thread threadConsumer = new Thread(consumer, "Announcer consumer");
        threadConsumer.setDaemon(true);
        threadConsumer.start();
    }

    @Override
    public AnnouncementProducer getAnnouncer() {
        return new AnnouncementProducer(queue);
    }

    @Override
    public void close() throws Exception {
        consumer.close();
    }
}
