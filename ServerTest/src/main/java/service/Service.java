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
package service;

import br.uff.labtempo.omcp.service.OmcpService;
import br.uff.labtempo.omcp.service.rabbitmq.RabbitPersistentService;
import br.uff.labtempo.omcp.service.rabbitmq.RabbitService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Service {

    static OmcpService service;

    public static void main(String[] args) throws Exception {
        service = new RabbitService("192.168.0.7", "admin", "admin");
        //service.addReference("omcp://notification.messagegroup/#");
        
        service.addReference("omcp://update.messagegroup/sensornet/#");
        service.addReference("omcp://update.messagegroup/virtualsensornet/#");

        //shutdown();
        service.start();

    }

    private static void shutdown() {
        Thread t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(6000);
                    try {
                        service.close();
                    } catch (Exception ex) {
                        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.setDaemon(true);
        t.start();

    }
}
