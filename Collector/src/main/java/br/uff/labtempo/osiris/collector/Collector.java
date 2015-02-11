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
package br.uff.labtempo.osiris.collector;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Collector implements AutoCloseable{

    public void execute() throws Exception{
        try (OmcpClient connection = new OmcpClientBuilder().host("192.168.0.7").user("admin", "admin").source("virtual-lab").build()) {
            populate(connection);
        }
    }
    
    public void populate(OmcpClient client) throws Exception {
        DataBuilder db;

        db = new DataBuilder("labtempo", "labpos");
        for (int i = 0; i < 20; i++) {            
            client.doNofity(getUri()+"labtempo/labpos/",db.generateSample());
        }

        db = new DataBuilder("labtempo", "datacenter1");
        for (int i = 0; i < 20; i++) {
            client.doNofity(getUri()+"labtempo/datacenter1",db.generateSample());
        }
        db = new DataBuilder("labtempo", "datacenter2");
        for (int i = 0; i < 20; i++) {
            client.doNofity(getUri()+"labtempo/datacenter2",db.generateSample());
        }

        db = new DataBuilder("posgradS", "lab1");
        for (int i = 0; i < 20; i++) {
            client.doNofity(getUri()+"posgradS/lab1",db.generateSample());
        }

        db = new DataBuilder("posgradS", "lab2");
        for (int i = 0; i < 20; i++) {
            client.doNofity(getUri()+"posgradS/lab2",db.generateSample());
        }
    }
    
    @Override
    public void close() throws Exception {
        
    }
    
    private String getUri(){
        String uri = "omcp://collector.messagegroup/";
        return uri;
    }
    

}
