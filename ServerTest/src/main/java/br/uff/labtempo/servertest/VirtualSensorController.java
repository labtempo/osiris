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
package br.uff.labtempo.servertest;

import br.uff.labtempo.omcp.common.Request;
import static br.uff.labtempo.omcp.common.RequestMethod.*;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.omcp.Controller;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VirtualSensorController extends Controller {

    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        switch (request.getMethod()) {
            case GET:
                if (match(request.getResource(), "/sensor/")) {
                    return get(request);
                }else if (match(request.getResource(), "/sensor/:id")) {
                    return getItem(request);
                }
                break;
            case POST:
                break;
            case PUT:
                break;
            case DELETE:
                break;
            case NOTIFY:
                break;
        }
        return goToNext(request);

    }

    private Response get(Request request) {
        Response response = new ResponseBuilder().ok("corpo todos os sensores").build();
        return response;
    }

    private Response getItem(Request request) {
        Response response = new ResponseBuilder().ok("corpo do sensor 5").build();
        return response;
    }

}
