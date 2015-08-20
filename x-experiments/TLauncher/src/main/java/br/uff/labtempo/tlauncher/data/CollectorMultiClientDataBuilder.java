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
package br.uff.labtempo.tlauncher.data;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.tlauncher.model.ConnectionFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class CollectorMultiClientDataBuilder extends DataBuilder {

    private ConnectionFactory factory;
    private final BlockingQueue<OmcpClient> queue;
    private Thread thread;

    public CollectorMultiClientDataBuilder(ConnectionFactory factory, String collectorId) {
        super(collectorId);
        this.factory = factory;
        this.queue = new LinkedBlockingQueue<>();
        thread = new Thread(new Consumer(), "Terminator consumer");
        thread.setDaemon(true);
        thread.setPriority(2);
        thread.start();
    }

    @Override
    public void publishSample(String sensorId, int messageId) {
        SampleCoTo sample = getSample(sensorId, messageId);
        OmcpClient client = factory.getClient();
        client.doNofity(makeUrl(sensorId), sample);
        queue.add(client);
    }

    private class Consumer implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    OmcpClient client = queue.take();
                    if (client != null) {
                        client.close();
                    } else {
                        break;
                    }
                } catch (Exception ex) {
                    break;
                }
            }
        }
    }
}
