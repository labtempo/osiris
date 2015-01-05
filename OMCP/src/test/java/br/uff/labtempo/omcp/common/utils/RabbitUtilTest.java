/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common.utils;

import br.uff.labtempo.omcp.common.Request;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe
 */
public class RabbitUtilTest {

    String DOMAIN = "osiris";
    String HOST = "alerts.ex." + DOMAIN;
    String RESOURCE = "/sensornet/warning";
    String OMCP = "omcp://" + HOST + RESOURCE;
    String hostResult = "osiris.ex.alerts";
    String routingKeyResult = "warning.sensornet";

    /**
     * Test of getHostAddress method, of class RabbitUtil.
     */
    @Test
    public void testGetHostAddress_URI_String() throws URISyntaxException {
        System.out.println("getHostAddress");
        URI uri = new URI(OMCP);
        String result = RabbitUtil.getHostAddress(uri, DOMAIN);
        assertEquals(hostResult, result);
    }

    @Test
    public void testGetHostAddress_URI_Custom() throws URISyntaxException {
        System.out.println("getHostAddress");
        URI uri = new URI("omcp://collector.ex/lab/*");
        String result = RabbitUtil.getHostAddress(uri, DOMAIN);
        assertEquals("osiris.ex.collector", result);
    }

    /**
     * Test of getHostAddress method, of class RabbitUtil.
     */
    @Test
    public void testGetHostAddress_Request_String() {
        System.out.println("getHostAddress");
        Request request = new RequestBuilder().onNotify(OMCP).content("").build();
        String result = RabbitUtil.getHostAddress(request, DOMAIN);
        assertEquals(hostResult, result);
    }

    /**
     * Test of getHostAddress method, of class RabbitUtil.
     */
    @Test
    public void testGetHostAddress_String_String() {
        System.out.println("getHostAddress");
        String result = RabbitUtil.getHostAddress(HOST, DOMAIN);
        assertEquals(hostResult, result);
    }

    /**
     * Test of getRoutingKey method, of class RabbitUtil.
     */
    @Test
    public void testGetRoutingKey_URI() throws URISyntaxException {
        System.out.println("getRoutingKey");
        URI uri = new URI(OMCP);
        String result = RabbitUtil.getRoutingKey(uri);
        assertEquals(routingKeyResult, result);
    }

    @Test
    public void testGetRoutingKey_URI_Custom() throws URISyntaxException {
        System.out.println("getRoutingKey");
        URI uri = new URI("omcp://collector.ex.osiris/coletor1/amostra");
        String result = RabbitUtil.getRoutingKey(uri);
        assertEquals("amostra.coletor1", result);
    }

    /**
     * Test of getRoutingKey method, of class RabbitUtil.
     */
    @Test
    public void testGetRoutingKey_Request() {
        System.out.println("getRoutingKey");
        Request request = new RequestBuilder().onNotify(OMCP).content("").build();
        String result = RabbitUtil.getRoutingKey(request);
        assertEquals(routingKeyResult, result);
    }

    /**
     * Test of getRoutingKey method, of class RabbitUtil.
     */
    @Test
    public void testGetRoutingKey_String() {
        System.out.println("getRoutingKey");
        String result = RabbitUtil.getRoutingKey(RESOURCE);
        assertEquals(routingKeyResult, result);
    }
}
