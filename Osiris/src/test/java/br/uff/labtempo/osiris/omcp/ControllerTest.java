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
package br.uff.labtempo.osiris.omcp;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
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
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ControllerTest {

    public ControllerTest() {
    }

    @Test
    public void testUrl() throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
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
    public void testUrlWithParams() throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
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
    public void testUrlWithOneParam() throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
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
    public void testAmbiguousInput() throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
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
