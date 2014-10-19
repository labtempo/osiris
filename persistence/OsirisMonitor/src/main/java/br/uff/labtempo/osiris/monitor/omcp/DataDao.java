/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.monitor.omcp;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.utils.ResponsePacket;
import br.uff.labtempo.osiris.collector.temp.Network;
import br.uff.labtempo.osiris.collector.temp.Sensor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe
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
            Network[] networks = r.getContent(Network[].class);            
            List<String> names = new ArrayList<>();
            for (Network net : networks) {
                names.add(net.getId());
            }
            return names;
        }
    }
    
    public List<Sensor> getSensors(String networkId) throws Exception {
        try (OmcpClient connection = new OmcpClientBuilder().host(ip).user(user, password).source("generico").build()) {
            Response r = connection.doGet("omcp://sensornet/"+networkId+"/sensors");
            Sensor[] sensors = r.getContent(Sensor[].class);            
            List<Sensor> names = new ArrayList<>();
            for (Sensor sensor : sensors) {
               names.add(sensor);
            }
            return names;
        }
    }

}
