/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.client;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.utils.RequestBuilder;
import br.uff.labtempo.omcp.common.utils.RequestPacket;
import br.uff.labtempo.omcp.common.utils.ResponsePacket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Felipe
 */
public class RabbitClientSocketTest {

    //TODO: melhorar teste quando o servidor está offline
    private RabbitClientSocket connection;

    @Before
    public void setUp() throws Exception {

        this.connection = new RabbitClientSocket("192.168.0.7", "admin", "admin");
        this.connection.connect();

    }

    @After
    public void tearDown() throws Exception {
        this.connection.close();
    }

    /**
     * Test of call method, of class RabbitBridge.
     */
    @Test
    public void testCall() throws Exception {
        if (connection.isLive() == false) {
            return;
        }

        Request request = new RequestBuilder().onGet("omcp://sensornet/resource").build();
        String packet = this.connection.call("osiris.sensornet", new RequestPacket().generate(request));
        
        ResponsePacket rp = new ResponsePacket();
        Response response = rp.parse(packet);
        System.out.println(rp.generate(response));
    }

    @Test
    public void testPublish() throws Exception {
        if (connection.isLive() == false) {

        }
        Request request = new RequestBuilder().onNotify("omcp://sensornet/resource").build();
        this.connection.publish("osiris.sensornet", new RequestPacket().generate(request));
    }

    @Test(expected = RuntimeException.class)
    public void testCallToInexistentQueue() throws Exception {
        if (connection.isLive() == false) {
            throw new RuntimeException();
        }
        this.connection.call("xyz", "teste");
    }

    @Test(expected = RuntimeException.class)
    public void testPublishToInexistentQueue() throws Exception {
        if (connection.isLive() == false) {
            throw new RuntimeException();
        }
        this.connection.publish("xyz", "teste");
    }
}
