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
package br.uff.labtempo.omcp.client;

import br.uff.labtempo.omcp.client.rabbitmq.RabbitClient;
import br.uff.labtempo.omcp.common.exceptions.ConnectionException;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class OmcpClientBuilder {

    private String host;
    private String user;
    private String pass;
    private String source;

    public OmcpClientBuilder host(String host) {
        this.host = host;
        return this;
    }

    public OmcpClientBuilder user(String user, String pass) {
        this.pass = pass;
        this.user = user;
        return this;
    }

    public OmcpClientBuilder source(String source) {
        this.source = source;
        return this;
    }

    public OmcpClient build() throws ConnectionException {
        OmcpClient client = null;
        if (user != null) {
            client = new RabbitClient(host, user, pass);
        } else {
            client = new RabbitClient(host);
        }
        if (source != null) {
            client.setSourceId(source);
        }
        return client;
    }
}
