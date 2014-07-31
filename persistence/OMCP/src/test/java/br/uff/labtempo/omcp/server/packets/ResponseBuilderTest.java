/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.packets;

import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.omcp.common.Response;
import static br.uff.labtempo.omcp.common.StatusCode.*;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import java.net.URI;
import java.net.URISyntaxException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Felipe
 */
public class ResponseBuilderTest {
    private String host;
    private String description;
    private String version;

    @Before
    public void setBuilder() {
        this.host = "omcp://test-module";
        this.description = "test-java/0.1";
        this.version = "OMCP/0.1";
        ResponseBuilder.config(host, description, version);
    }

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
        String uri = ("/test");
        Response response = new ResponseBuilder().created(uri).build();
        assertEquals(CREATED, response.getStatusCode());
        assertEquals(host + uri, response.getLocation());
    }

    @Test
    public void testNotImplemented() {
        System.out.println("testNotImplemented");
        Response response = new ResponseBuilder().error(new NotImplementedException("error")).build();
        assertEquals(NOT_IMPLEMENTED, response.getStatusCode());
    }

    @Test
    public void testServerError() {
        System.out.println("serverError");
        String error = "Test error";
        Response response = new ResponseBuilder().error(new InternalServerErrorException(error)).build();
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(error, response.getErrorMessage());
    }
}
