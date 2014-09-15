/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common.utils;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.RequestMethod;
import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import java.net.URI;
import java.net.URISyntaxException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import test.data.builders.TestRequestBuilder;

/**
 *
 * @author Felipe
 */
public class RequestPacketTest {

    private TestRequestBuilder builder;

    @Before
    public void builders() {
        this.builder = new TestRequestBuilder();
    }

    @Test
    public void testGet() throws URISyntaxException, BadRequestException, MethodNotAllowedException{
        System.out.println("testGet");

        String host = "sensornet";
        String resource = "/sensors";
        String version = "OMCP/0.1";

        Request request = builder.method("GET").host(host).resource(resource).version(version).buildNoContent();;

        assertEquals(RequestMethod.GET, request.getMethod());
        assertEquals(host, request.getModule());
        assertEquals(resource, request.getResource());
        assertEquals(version, request.getVersion());
        assertEquals(null, request.getContent());
    }

    @Test
    public void testPost() throws URISyntaxException, BadRequestException, MethodNotAllowedException {
        System.out.println("testPost");
        
        String host = "sensornet";
        String resource = "/sensors";
        String version = "OMCP/0.1";
        String content = "content";

        Request request = builder.method("POST").host(host).resource(resource).version(version).content(content).buildContent();

       assertEquals(RequestMethod.POST, request.getMethod());
        assertEquals(host, request.getModule());
        assertEquals(resource, request.getResource());
        assertEquals(version, request.getVersion());
        assertEquals(content, request.getContent());
    }

    @Test
    public void testPut() throws URISyntaxException, BadRequestException, MethodNotAllowedException {
        System.out.println("testPut");

        String host = "sensornet";
        String resource = "/sensors";
        String version = "OMCP/0.1";
        String content = "content";

        Request request = builder.method("PUT").host(host).resource(resource).version(version).content(content).buildContent();

assertEquals(RequestMethod.PUT, request.getMethod());
        assertEquals(host, request.getModule());
        assertEquals(resource, request.getResource());
        assertEquals(version, request.getVersion());
        assertEquals(content, request.getContent());
    }

    @Test
    public void testDelete() throws URISyntaxException, BadRequestException, MethodNotAllowedException {
        System.out.println("testDelete");

        String host = "sensornet";
        String resource = "/sensors";
        String version = "OMCP/0.1";

        Request request = builder.method("DELETE").host(host).resource(resource).version(version).buildNoContent();
        assertEquals(RequestMethod.DELETE, request.getMethod());
        assertEquals(host, request.getModule());
        assertEquals(resource, request.getResource());
        assertEquals(version, request.getVersion());
        assertEquals(null, request.getContent());
    }

    @Test
    public void testNotify() throws URISyntaxException, BadRequestException, MethodNotAllowedException {
        System.out.println("testNotify");

        String host = "sensornet";
        String resource = "/sensors";
        String version = "OMCP/0.1";

        Request request = builder.method("NOTIFY").host(host).resource(resource).version(version).buildContent();

        assertEquals(RequestMethod.NOTIFY, request.getMethod());
        assertEquals(host, request.getModule());
        assertEquals(resource, request.getResource());
        assertEquals(version, request.getVersion());
        assertEquals(null, request.getContent());
    }

    @Test(expected = BadRequestException.class)
    public void testWrongMethod() throws URISyntaxException, BadRequestException, MethodNotAllowedException {
        System.out.println("testWrongMethod");
        builder.method("GETS").buildNoContent();
    }

    @Test(expected = BadRequestException.class)
    public void testNoDate() throws URISyntaxException, BadRequestException, MethodNotAllowedException {
        System.out.println("testNoDate");
        builder.method("GET").buildNoDate();
    }

    @Test(expected = BadRequestException.class)
    public void testWrongDateFormat() throws URISyntaxException, BadRequestException, MethodNotAllowedException {
        System.out.println("testWrongDateFormat");
        builder.method("GET").date("2014-07-20 19:56:39 GMT").buildNoContent();
    }

    @Test(expected = BadRequestException.class)
    public void testNoHost() throws URISyntaxException, BadRequestException, MethodNotAllowedException {
        System.out.println("testNoHost");
        builder.method("GET").buildNoHost();
    }

    @Test(expected = BadRequestException.class)
    public void testPostWithNoContent() throws URISyntaxException, BadRequestException, MethodNotAllowedException {
        System.out.println("testPostWithNoContent");
        builder.method("POST").buildNoContent();
    }

    @Test(expected = BadRequestException.class)
    public void testPutWithNoContent() throws URISyntaxException, BadRequestException, MethodNotAllowedException {
        System.out.println("testPutWithNoContent");
        builder.method("PUT").buildNoContent();
    }

}
