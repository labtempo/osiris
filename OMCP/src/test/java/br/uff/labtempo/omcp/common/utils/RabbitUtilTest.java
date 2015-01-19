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
package br.uff.labtempo.omcp.common.utils;

import br.uff.labtempo.omcp.common.Request;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RabbitUtilTest {

    String DOMAIN = "osiris";
    String HOST = "alerts.ex." + DOMAIN;
    String RESOURCE = "/sensornet/warning";
    String OMCP = "omcp://" + HOST + RESOURCE;
    String hostResult = "osiris.ex.alerts";
    String routingKeyResult = "sensornet.warning";

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
        assertEquals("coletor1.amostra", result);
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
