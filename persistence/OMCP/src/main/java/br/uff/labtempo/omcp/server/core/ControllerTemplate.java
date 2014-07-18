/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.core;

import br.uff.labtempo.omcp.server.packets.ResponseBuilder;
import br.uff.labtempo.omcp.server.packets.Response;

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

    public Response onGet() {
        return new ResponseBuilder().notImplemented().build();
    }

    public Response onPost(String content) {
        return new ResponseBuilder().notImplemented().build();
    }

    public Response onPut(long id, String content) {
        return new ResponseBuilder().notImplemented().build();
    }

    public Response onDelete(long id) {
        return new ResponseBuilder().notImplemented().build();
    }

    public void onNotify(String content) {

    }

}
