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
package br.uff.labtempo.servertest;

import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.omcp.server.OmcpServer;
import br.uff.labtempo.omcp.server.rabbitmq.RabbitServer;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Server implements AutoCloseable {

    private final String pwd;
    private final String usr;
    private final String ip;

    private OmcpServer server;

    public Server() {
        ip = "192.168.0.7";
        usr = "admin";
        pwd = "admin";
        server = new RabbitServer("teste", ip, usr, pwd);

        Controller root = new RootController();
        Controller sensor = new VirtualSensorController();

        root.setNext(sensor);
        server.setHandler(root);

        server.addReference("omcp://collector.messagegroup/#");
        server.addReference("omcp://update.messagegroup/sensornet/#");

    }

    public void start() {
        server.start();
    }

    @Override
    public void close() throws Exception {
        server.close();
    }

}
