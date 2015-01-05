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
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import br.uff.labtempo.omcp.server.rabbitmq.RabbitServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class OmcpConnectionTest {

    private RabbitClient connection;
    private RabbitServer server;
    private final String pwd;
    private final String usr;
    private final String ip;

    public OmcpConnectionTest() {
        ip = "192.168.0.7";
        usr = "admin";
        pwd = "admin";

    }

    //@Before
    public void configure() {
        try {
            this.connection = new RabbitClient(ip, usr, pwd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            this.server = new RabbitServer("test", ip, usr, pwd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                server.start();
                System.out.println("Thread end!");
            }
        });

        t.setName("server");
        t.setDaemon(true);
        t.start();
    }

    public void closeAll() throws Exception {
        connection.close();
        server.close();
    }

    //@Test
    public void doGet() {
        Response resp = connection.doGet("omcp://test/resouce");
        assertEquals(StatusCode.NOT_FOUND, resp.getStatusCode());
    }

    //@Test
    public void doPost() {
        Response resp = connection.doPost("omcp://test/resouce", "teste post");
        assertEquals(StatusCode.NOT_FOUND, resp.getStatusCode());
    }

}
