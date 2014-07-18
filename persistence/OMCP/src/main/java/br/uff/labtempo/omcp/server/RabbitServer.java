/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server;

import br.uff.labtempo.omcp.server.packets.RequestBuilder;
import br.uff.labtempo.omcp.server.packets.Request;
import br.uff.labtempo.omcp.server.packets.Response;
import br.uff.labtempo.omcp.server.core.ControllerTemplate;
import br.uff.labtempo.omcp.server.core.Router;
import br.uff.labtempo.omcp.server.core.ServerRequestListener;
import br.uff.labtempo.omcp.server.core.ServerResponseContainer;
import br.uff.labtempo.omcp.server.core.ServerSocket;


/**
 *
 * @author Felipe
 */
public class RabbitServer implements ServerRequestListener {

    private ServerSocket socket;
    private Router router;

    public RabbitServer() {
        this.socket = new RabbitServerSocket();
        this.router = new Router();
        this.socket.setListener(this);
    }

    public void start() {
        this.socket.run();
    }

    public void stop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void incoming(String message, ServerResponseContainer responseContainer) {
        //colocar threads aqui
        Request request = new RequestBuilder().message(message).build();
        Response response = this.router.routify(request);
        if (response != null) {
            responseContainer.exec(response.toString());
        }
    }

    public void addController(ControllerTemplate controller) {
        this.router.addController(controller);
    }
}
