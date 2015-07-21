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
package br.uff.labtempo.omcp.service.rabbitmq;

import br.uff.labtempo.omcp.server.rabbitmq.*;
import br.uff.labtempo.omcp.common.utils.RequestPacket;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.service.EventHandler;
import br.uff.labtempo.omcp.service.OmcpService;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RabbitPersistentService implements OmcpService, RabbitListener {

    private final String DOMAIN = "osiris.service.";
    private RabbitServiceSocket socket;
    private boolean running;
    private EventHandler handler;

    public RabbitPersistentService(String serviceName, String host, String user, String password, boolean silent) {
        this.socket = new RabbitServiceSocket(DOMAIN + serviceName, host, user, password, true, silent);
        this.socket.setListener(this);
        this.running = true;
    }

    public RabbitPersistentService(String serviceName, String host, String user, String password) {
        this(serviceName, host, user, password, false);
    }

    @Override
    public void start() {
        while (running) {
            try {
                this.socket.run();
            } catch (Exception e) {
                this.socket.close();
            }
        }
        System.out.println("Service shutdown complete!");
    }

    public void purge() {
        socket.purge();
    }

    @Override
    public boolean incoming(String message, ServerResponseContainer responseContainer) {
        boolean process = false;

        try {
            Request request = new RequestPacket().parse(message);
            handler.process(request);
            process = true;
        } catch (Exception ex) {

        }

        return process;
    }

    @Override
    public void setHandler(EventHandler handler) {
        this.handler = handler;
    }

    @Override
    public void close() {
        this.running = false;
        this.socket.close();
    }

    @Override
    public void addReference(String url) {
        socket.addReference(url);
    }

    @Override
    public boolean isStarted() {
        return socket.isStarted();
    }
}
