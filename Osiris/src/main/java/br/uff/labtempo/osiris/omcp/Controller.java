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
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.omcp.server.Context;
import br.uff.labtempo.omcp.server.RequestHandler;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public abstract class Controller implements RequestHandler {

    private Controller nextController;
    private Context context;

    public void setNext(Controller controller) {
        this.nextController = controller;
        controller.setContext(context);
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
        if (nextController != null) {
            nextController.setContext(context);
        }
    }

    public Context getContext() {
        return context;
    }

    public abstract Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException;

    @Override
    public Response handle(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
        Response response = process(request);
        if (response == null) {
            return goToNext(request);
        }
        return response;
    }

    protected Response goToNext(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
        if (nextController != null) {
            return nextController.handle(request);
        }
        throw new NotFoundException(request.getResource() + " not found!");
    }

    /*
     * transformar para regex acima method params
     */
    protected boolean match(String resource, String customRegex) {
        return HandlerTools.match(resource, customRegex);
    }

    protected Map<String, String> extractParams(String path, String customRegex) {
        return HandlerTools.extractParams(path, customRegex);
    }

    protected Response builderOk(Object obj, String contentType) {
        Response response;
        if (contentType == null) {
            response = new ResponseBuilder().ok(obj, ResponseBuilder.ContentType.JSON).build();
        } else {
            response = new ResponseBuilder().ok(obj, contentType).build();
        }
        return response;

    }
}
