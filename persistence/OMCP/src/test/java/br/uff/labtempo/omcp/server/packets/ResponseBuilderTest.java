/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.packets;

import static br.uff.labtempo.omcp.common.OmcpStatusCodes.*;
import java.net.URI;
import java.net.URISyntaxException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Felipe
 */
public class ResponseBuilderTest{

    @Test
    public void testOk() {
        System.out.println("ok");
        String content = "test";
        Response response = new ResponseBuilder().ok(content).build();
        assertEquals(OK, response.getStatusCode());
        assertEquals(content, response.getContent());
        assertEquals(content.length(), response.getContentLength());
    }

    @Test
    public void testCreated() throws URISyntaxException {
        System.out.println("created");
        URI uri = new URI("/test");
        Response response = new ResponseBuilder().created(uri).build();
        assertEquals(CREATED, response.getStatusCode());
        assertEquals(uri, response.getLocation());
    }

    @Test
    public void testNotImplemented() {
        System.out.println("testNotImplemented");
        Response response = new ResponseBuilder().notImplemented().build();
        assertEquals(NOT_IMPLEMENTED, response.getStatusCode());
    }

    @Test
    public void testServerError() {
        System.out.println("serverError");
        String error = "Test error";
        Response response = new ResponseBuilder().serverError(new RuntimeException(error)).build();
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(error, response.getErrorMessage());
    }    
}
