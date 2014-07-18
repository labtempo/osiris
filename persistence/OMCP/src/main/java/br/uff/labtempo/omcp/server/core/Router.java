/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.core;

import br.uff.labtempo.omcp.server.packets.Request;
import br.uff.labtempo.omcp.server.packets.Response;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class Router {

    private Map<String, ControllerTemplate> controllers;

    public Router() {
        this.controllers = new HashMap<>();
    }

    public void addController(ControllerTemplate controller) {
        this.controllers.put(controller.getResource(), controller);
    }

    public Response routify(Request request) {
        ControllerTemplate controller = this.controllers.get(request.getResource().toString());
        switch (request.getMethod()) {
            case GET:
                return controller.onGet();
            case POST:
                return controller.onPost(request.getContent());
            case PUT:
                return controller.onPut(1, request.getContent());
            case DELETE:
                return controller.onDelete(1);            
            case NOTIFY:
                controller.onNotify(request.getContent());
            default:
                return null;
        }
    }
}
