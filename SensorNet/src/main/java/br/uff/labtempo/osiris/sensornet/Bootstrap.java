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
package br.uff.labtempo.osiris.sensornet;

import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.omcp.server.OmcpServer;
import br.uff.labtempo.omcp.server.rabbitmq.RabbitServer;
import br.uff.labtempo.osiris.sensornet.controller.CollectorController;
import br.uff.labtempo.osiris.sensornet.controller.NetworkController;
import br.uff.labtempo.osiris.sensornet.controller.NotifyController;
import br.uff.labtempo.osiris.sensornet.controller.SensorController;
import br.uff.labtempo.osiris.sensornet.persistence.jpa.JpaDaoFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Bootstrap implements AutoCloseable {

    private final String pwd;
    private final String usr;
    private final String ip;
    private final String moduleName;

    private JpaDaoFactory factory;

    private OmcpServer server;

    public Bootstrap() throws Exception {
        ip = "192.168.0.7";
        usr = "admin";
        pwd = "admin";
        moduleName = "sensornet";

        try {
            server = new RabbitServer(moduleName, ip, usr, pwd);

            factory = JpaDaoFactory.newInstance(ip, usr, pwd, moduleName);

            Controller eCtrl = new NotifyController(factory);
            Controller sCtrl = new SensorController(factory);
            Controller nCtrl = new NetworkController(factory);
            Controller cCtrl = new CollectorController(factory);

            eCtrl.setNext(sCtrl);
            sCtrl.setNext(nCtrl);
            nCtrl.setNext(cCtrl);

            server.setHandler(eCtrl);

            server.addReference("omcp://collector.messagegroup/");
        } catch (Exception ex) {
            close();
            throw ex;
        }
        //server.addReference("omcp://test.ex/warning/");
    }

    public void start() {
        server.start();
    }

    @Override
    public void close() {
        try {
            server.close();
        } catch (Exception e) {
        }
        try {
            factory.close();
        } catch (Exception e) {
        }

    }
}
