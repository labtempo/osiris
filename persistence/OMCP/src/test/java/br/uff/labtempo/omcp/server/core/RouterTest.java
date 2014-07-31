/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.core;

import test.data.builders.TestEmptyController;
import test.data.builders.TestRequestBuilder;
import br.uff.labtempo.omcp.common.Response;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static br.uff.labtempo.omcp.common.StatusCode.*;
import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;


/**
 *
 * @author Felipe
 */
public class RouterTest {

    private Router router;
    private TestRequestBuilder builder;

    @Before
    public void createRouter() {
        this.router = new Router();
        this.builder = new TestRequestBuilder();
    }

    @Test(expected = NotImplementedException.class)
    public void testNullRoutify() throws BadRequestException, MethodNotAllowedException, InternalServerErrorException, NotFoundException, NotImplementedException{
        //TODO: refazer teste!
        System.out.println("testNullRoutify");
        router.addController(new TestEmptyController());

        Response response;

        response = router.routify(builder.method("GET").buildNoContent());
        assertEquals(NOT_IMPLEMENTED, response.getStatusCode());

        response = router.routify(builder.method("POST").buildContent());
        assertEquals(NOT_IMPLEMENTED, response.getStatusCode());

        response = router.routify(builder.method("PUT").buildContent());
        assertEquals(NOT_IMPLEMENTED, response.getStatusCode());

        response = router.routify(builder.method("DELETE").buildNoContent());
        assertEquals(NOT_IMPLEMENTED, response.getStatusCode());

        response = router.routify(builder.method("NOTIFY").buildNoContent());
        assertEquals(null, response);
    }

}
