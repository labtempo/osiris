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
package br.uff.labtempo.osiris.function.sum;

import br.uff.labtempo.omcp.client.rabbitmq.RabbitClient;
import br.uff.labtempo.omcp.server.OmcpServer;
import br.uff.labtempo.omcp.server.rabbitmq.RabbitServer;
import br.uff.labtempo.osiris.function.sum.configuration.AppConfig;
import br.uff.labtempo.osiris.function.sum.controller.MainController;
import java.util.Properties;

/**
 * Class bootstrap/initialize the OMCP Server of the Sum Function module
 * @author Felipe Santos <fralph at ic.uff.br>
 * @author andre.ghigo
 * @version 1.0
 * @since 1.8
 */
public class Bootstrap implements AutoCloseable {

    private OmcpServer omcpServer;
    private RabbitClient omcpClient;

    public Bootstrap(Properties properties) throws Exception {

        String ip = properties.getProperty(AppConfig.RABBITMQ_IP_PROPERTIES_KEY);
        String user = properties.getProperty(AppConfig.RABBITMQ_USERNAME_PROPERTIES_KEY);
        String pass = properties.getProperty(AppConfig.RABBITMQ_PASSWORD_PROPERTIES_KEY);

        String moduleName = AppConfig.MODULE_NAME;

        try {            
            omcpServer = new RabbitServer(moduleName, ip, user, pass);
            omcpClient = new RabbitClient(ip, user, pass);
            omcpServer.setHandler(new MainController(omcpClient));
        } catch (Exception ex) {
            close();
            throw new RuntimeException(String.format("Failed to bootstrap the omcpServer for the %s module", AppConfig.MODULE_NAME));
        }
    }

    public void start() {
        try {
            omcpServer.start();
        } catch (Exception ex) {
            close();
            throw new RuntimeException("Failed to start the omcpServer");
        }
    }

    @Override
    public void close() {
        try {
            omcpServer.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to close the omcpServer");
        }        
    }
}
