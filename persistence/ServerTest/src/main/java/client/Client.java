/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.utils.ResponsePacket;

/**
 *
 * @author Felipe
 */
public class Client {

    public static void main(String[] args) throws Exception {
        try (OmcpClient connection = new OmcpClientBuilder().host("192.168.0.7").user("admin", "admin").source("generico").build()) {
            //Response  r = connection.doGet("omcp://teste/");  
            connection.doNofity("omcp://test.ex/sensor/","teste");
            connection.doNofity("omcp://test.ex/collector1/sample","teste");
            connection.doNofity("omcp://test.ex/collector/","teste");
            connection.doNofity("omcp://test.ex/collector/01","teste");
            //System.out.println(new ResponsePacket().generate(r));
        }
    }
}
