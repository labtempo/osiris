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
package br.uff.labtempo.osiris.monitor.omcp;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class DataDao {

    private final String password;
    private final String user;
    private final String ip;

    public DataDao(String ip, String user, String password) {
        this.password = password;
        this.user = user;
        this.ip = ip;
    }

    public List<String> getNetworks() throws Exception {
        try (OmcpClient connection = new OmcpClientBuilder().host(ip).user(user, password).source("generico").build()) {
            Response r = connection.doGet("omcp://sensornet/");
            NetworkSnTo[] networks = r.getContent(NetworkSnTo[].class);            
            List<String> names = new ArrayList<>();
            for (NetworkSnTo net : networks) {
                names.add(net.getId());
            }
            return names;
        }
    }
    
    public List<SensorSnTo> getSensors(String networkId) throws Exception {
        try (OmcpClient connection = new OmcpClientBuilder().host(ip).user(user, password).source("generico").build()) {
            Response r = connection.doGet("omcp://sensornet/"+networkId+"/sensors");
            SensorSnTo[] sensors = r.getContent(SensorSnTo[].class);            
            List<SensorSnTo> names = new ArrayList<>();
            for (SensorSnTo sensor : sensors) {
               names.add(sensor);
            }
            return names;
        }
    }
    
    public List<CollectorSnTo> getCollectors(String networkId) throws Exception {
        try (OmcpClient connection = new OmcpClientBuilder().host(ip).user(user, password).source("generico").build()) {
            Response r = connection.doGet("omcp://sensornet/"+networkId+"/collectors");
            CollectorSnTo[] collectors = r.getContent(CollectorSnTo[].class);            
            List<CollectorSnTo> names = new ArrayList<>();
            for (CollectorSnTo collector : collectors) {
               names.add(collector);
            }
            return names;
        }
    }

    public List<SensorSnTo> getSensors(String nid, String cid) throws Exception{
        try (OmcpClient connection = new OmcpClientBuilder().host(ip).user(user, password).source("generico").build()) {
            Response r = connection.doGet("omcp://sensornet/"+nid+"/collectors/"+cid+"/sensors");
            SensorSnTo[] sensors = r.getContent(SensorSnTo[].class);            
            List<SensorSnTo> names = new ArrayList<>();
            for (SensorSnTo sensor : sensors) {
               names.add(sensor);
            }
            return names;
        }}


}
