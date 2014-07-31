/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.core;

import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.Response;

/**
 *
 * @author Felipe
 */
public abstract class ControllerTemplate {

    private String resource;

    public ControllerTemplate(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }

    public Response onGet() throws InternalServerErrorException, NotImplementedException {
        throw new NotImplementedException("GET is not implemented");
    }

    public Response onPost(String content) throws InternalServerErrorException, NotImplementedException {
        throw new NotImplementedException("POST is not implemented");
    }

    public Response onPut(long id, String content) throws InternalServerErrorException, NotImplementedException {
        throw new NotImplementedException("PUT is not implemented");
    }

    public Response onDelete(long id) throws InternalServerErrorException, NotImplementedException {
        throw new NotImplementedException("DELETE is not implemented");
    }

    public void onNotify(String content) throws InternalServerErrorException, NotImplementedException {
        throw new NotImplementedException("NOTIFY is not implemented");
    }

}
