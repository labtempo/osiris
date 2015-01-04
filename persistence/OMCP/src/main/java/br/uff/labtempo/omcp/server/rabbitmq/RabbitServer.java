/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.rabbitmq;

import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.RequestPacket;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.server.RequestHandler;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.omcp.common.utils.ResponsePacket;
import br.uff.labtempo.omcp.server.OmcpServer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class RabbitServer implements OmcpServer, RabbitListener {

    private final String DOMAIN = "osiris.";
    private final String PROTOCOL_VERSION = "OMCP/0.1";    
    private final String MODULE_NAME;
    private final String MODULE_DESCRIPTION;
    private final String HOST;
    private ServerSocket socket;
    private boolean running;
    private RequestHandler handler;

    public RabbitServer(String moduleName, String host, String user, String password) {
        this.MODULE_NAME = moduleName;
        this.MODULE_DESCRIPTION = moduleName + "-java/0.1";
        this.HOST = "omcp://" + moduleName;
        this.socket = new RabbitServerSocket(DOMAIN + moduleName, host, user, password);
        this.socket.setListener(this);
        this.running = true;
    }

    public void start() {
        while (running) {
            try {
                this.socket.run();
            } catch (Exception e) {
                this.socket.close();
            }
        }
        System.out.println("Server shutdown complete!");
    }

    public void purge() {
        ((RabbitServerSocket) socket).purge();
    }

    @Override
    public boolean incoming(String message, ServerResponseContainer responseContainer) {
        Response response;
        boolean isDone = false;

        try {
            Request request = new RequestPacket().parse(message);
            response = handler.handler(request);
            isDone = true;
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
        }catch(Exception ex){
            response = new ResponseBuilder().error(new InternalServerErrorException(ex)).build();
            Logger.getLogger(RabbitServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (responseContainer != null && response != null) {
            response.setHost(HOST);
            response.setModule(MODULE_DESCRIPTION);
            response.setProtocolVersion(PROTOCOL_VERSION);
            responseContainer.exec(new ResponsePacket().generate(response));
        }

        return isDone;
    }

    @Override
    public void setHandler(RequestHandler handler) {
        this.handler = handler;
        this.handler.setContext(new RabbitContext(HOST, PROTOCOL_VERSION, MODULE_DESCRIPTION, MODULE_NAME));
    }

    @Override
    public void close() {
        this.running = false;
        this.socket.close();
    }

    @Override
    public void addReference(String url) {
        socket.addReference(url);
    }
}
