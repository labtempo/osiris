/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.omcp;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.server.Context;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe
 */
public class ControllerTest {

    public ControllerTest() {
    }

    @Test
    public void testUrl() throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        Controller c = new Controller() {

            @Override
            public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {

                String path = "/:aid/controller/:bid/";

                String requestPath = "/23s/controller/ds3/";

                Map<String, String> map = extract(requestPath, path);

                assertTrue(map.containsKey(":aid"));
                assertTrue(map.containsKey(":bid"));

                return null;
            }

            @Override
            public void setContext(Context context) {
            }
        };
        c.process(null);

    }

    @Test
    public void testUrlWithParams() throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        Controller c = new Controller() {

            @Override
            public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {

                String path = "/:aid/controller/:bid/";

                String requestPath = "/23s/controller/ds3?chave=valor&chave2=valor2";

                Map<String, String> map = extract(requestPath, path);

                assertTrue(map.containsKey(":aid"));
                assertTrue(map.containsKey(":bid"));

                assertTrue(map.containsKey("chave"));
                assertTrue(map.containsKey("chave2"));

                return null;
            }

            @Override
            public void setContext(Context context) {
            }
        };
        c.process(null);

    }

    @Test
    public void testUrlWithOneParam() throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        Controller c = new Controller() {

            @Override
            public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {

                String path = "/:aid/controller/:bid/";

                String requestPath = "/23s/controller/ds3/?chave=valor";

                Map<String, String> map = extract(requestPath, path);

                assertTrue(map.containsKey(":aid"));
                assertTrue(map.containsKey(":bid"));

                assertTrue(map.containsKey("chave"));

                return null;
            }

            @Override
            public void setContext(Context context) {
            }
        };
        c.process(null);

    }
    
    @Test
    public void testAmbiguousInput() throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        Controller c = new Controller() {

            @Override
            public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {

                String path = "/:aid/controller/";

                String requestPath = "/23s/controller/215/nkjg";
                
                assertFalse(match(requestPath, path));
                
                requestPath = "/23s/controller/215/nkjg/?chave=valor";
                
                assertFalse(match(requestPath, path));
                
                
                path = "/:aid/controller/:bid";
                
                assertFalse(match(requestPath, path)); 

                return null;
            }

            @Override
            public void setContext(Context context) {
            }
        };
        c.process(null);

    }

}
