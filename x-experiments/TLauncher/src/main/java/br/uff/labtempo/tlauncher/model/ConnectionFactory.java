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
package br.uff.labtempo.tlauncher.model;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.rabbitmq.RabbitClient;
import br.uff.labtempo.omcp.service.OmcpService;
import br.uff.labtempo.omcp.service.rabbitmq.RabbitService;
import java.util.Properties;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class ConnectionFactory {

    private String ip;
    private String user;
    private String pass;

    public ConnectionFactory(Properties config) {
        this.ip = config.getProperty("rabbitmq.server.ip");
        this.user = config.getProperty("rabbitmq.user.name");
        this.pass = config.getProperty("rabbitmq.user.pass");
    }

    public OmcpClient getClient() {
        return new RabbitClient(ip, user, pass);
    }

    public OmcpService getService() {
        return new RabbitService(ip, user, pass);
    }
    
    public OmcpService getSilentService() {
        return new RabbitService(ip, user, pass, true);
    }
}
