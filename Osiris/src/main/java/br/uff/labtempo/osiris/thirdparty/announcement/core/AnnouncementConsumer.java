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
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
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
