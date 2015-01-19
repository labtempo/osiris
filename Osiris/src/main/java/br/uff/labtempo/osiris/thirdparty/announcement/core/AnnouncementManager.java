/* 
 * Copyright 2015 Felipe Santos <fralph at ic.uff.br>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.uff.labtempo.osiris.thirdparty.announcement.core;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.osiris.thirdparty.announcement.Announcement;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class AnnouncementManager implements Announcement {

    private final BlockingQueue<AnnouncementItem> queue;
    private AnnoucementConsumer consumer;
    private final OmcpClient client;

    public AnnouncementManager(OmcpClient client) {
        this.queue = new LinkedBlockingQueue<>();
        this.client = client;
    }

    @Override
    public void initialize() {        
        consumer = new AnnoucementConsumer(queue, client);
        createThreadConsumer(consumer);
    }

    private void createThreadConsumer(AnnoucementConsumer consumer) {
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
