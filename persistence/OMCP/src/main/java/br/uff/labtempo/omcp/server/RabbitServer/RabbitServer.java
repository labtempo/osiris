/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.RabbitServer;

import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.RequestPacket;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.server.core.Router;
import br.uff.labtempo.omcp.server.RequestHandler;
import br.uff.labtempo.omcp.server.core.ServerResponseContainer;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.omcp.common.utils.ResponsePacket;
import br.uff.labtempo.omcp.server.OmcpServer;

/**
 *
 * @author Felipe
 */
public class RabbitServer implements OmcpServer, RabbitListener{

    private final String DOMAIN = "osiris.";   
    private final String PROTOCOL_VERSION = "OMCP/0.1";
    private final String MODULE_DESCRIPTION;
    private ServerSocket socket;
    private Router router;
    private boolean running;
    private RequestHandler handler;

    public RabbitServer(String moduleName, String host, String user, String password) {
        this.MODULE_DESCRIPTION = moduleName + "-java/0.1";
        this.socket = new RabbitServerSocket(DOMAIN + moduleName, host, user, password);
        this.router = new Router();
        this.socket.setListener(this);
        ResponseBuilder.config("omcp://" + moduleName, MODULE_DESCRIPTION, PROTOCOL_VERSION);
        this.running = true;
    }

    public void start() {
        while (running) {
            try {
                this.socket.run();
            } catch (Exception e) {
                this.socket.abort();
            }
        }
        System.out.println("Server shutdown complete!");
    }

    public void stop() {
        this.running = false;
        this.socket.abort();
    }
    
    public void purge(){
        ((RabbitServerSocket)socket).purge();
    }

    @Override
    public boolean incoming(String message, ServerResponseContainer responseContainer) {
        Response response;
        boolean process = false;

        try {
            Request request = new RequestPacket().parse(message);
            response = handler.process(request);
            process = true;
        } catch (BadRequestException ex) {
            response = new ResponseBuilder().error(ex).build();
        } catch (MethodNotAllowedException ex) {
            response = new ResponseBuilder().error(ex).build();
        } catch (NotFoundException ex) {
            response = new ResponseBuilder().error(ex).build();
        } catch (InternalServerErrorException ex) {
            response = new ResponseBuilder().error(ex).build();
        } catch (NotImplementedException ex) {
            response = new ResponseBuilder().error(ex).build();
        }

        if (responseContainer != null && response != null) {
            responseContainer.exec(new ResponsePacket().generate(response));
        }
        
        return process;
    }

    @Override
    public void setHandler(RequestHandler handler) {
        this.handler = handler;
    }
}
