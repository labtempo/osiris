/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.servertest;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.omcp.server.Controller;

/**
 *
 * @author Felipe
 */
public class RootController extends Controller {

    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        switch (request.getMethod()) {
            case GET:
                if(match(request.getResource(), "/sensor/")){
                    break;
                }                
                
                if (match(request.getResource(), "/")) {
                    return get(request);
                } else if (match(request.getResource(), "/:id")) {
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
        Response response = new ResponseBuilder().ok("corpo todos").build();
        return response;
    }

    private Response getItem(Request request) {
        Response response = new ResponseBuilder().ok("corpo item 5").build();
        return response;
    }

}
