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
import br.uff.labtempo.omcp.service.OmcpService;
import br.uff.labtempo.tlauncher.data.DataBase;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class CommunicationManager {
    private ConnectionFactory factory;
    private String testName;
    private OmcpClient client;
    private OmcpService service;

    public CommunicationManager(ConnectionFactory factory, String testName) {
        this.factory = factory;
        this.testName = testName;
        this.client = factory.getClient();
        this.service = factory.getService();        
    }
    
    private void createLink(int count){
        String networkId = DataBase.NETWORK_ID;
        String collectorId = testName;
    }
    
    
}
