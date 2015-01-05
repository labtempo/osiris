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
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ResponseBuilderTest {

    private String host;
    private String module;
    private String version;

    @Before
    public void setBuilder() {
        this.host = "omcp://test-module";
        this.module = "test-java/0.1";
        this.version = "OMCP/0.1";
    }

    @Test
    public void testOk() {
        System.out.println("ok");
        String content = "test";
        Response response = new ResponseBuilder().ok(content).build();

        response.setHost(host);
        response.setModule(module);
        response.setProtocolVersion(version);

        assertEquals(OK, response.getStatusCode());
        assertEquals(content, response.getContent());
        assertEquals(content.length(), response.getContentLength());
    }

    @Test
    public void testCreated() throws URISyntaxException {
        System.out.println("created");
        String uri = ("/test");
        Response response = new ResponseBuilder().created(uri).build();

        response.setHost(host);
        response.setModule(module);
        response.setProtocolVersion(version);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(host + uri, response.getLocation());
    }

    @Test
    public void testNotImplemented() {
        System.out.println("testNotImplemented");
        Response response = new ResponseBuilder().error(new NotImplementedException("error")).build();

        response.setHost(host);
        response.setModule(module);
        response.setProtocolVersion(version);

        assertEquals(NOT_IMPLEMENTED, response.getStatusCode());
    }

    @Test
    public void testServerError() {
        System.out.println("serverError");
        String error = "Test error";
        Response response = new ResponseBuilder().error(new InternalServerErrorException(error)).build();

        response.setHost(host);
        response.setModule(module);
        response.setProtocolVersion(version);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(error, response.getErrorMessage());
    }
}
