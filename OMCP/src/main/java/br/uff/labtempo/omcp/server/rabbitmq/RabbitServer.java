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
 * @author Felipe Santos <fralph at ic.uff.br>
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

    public RabbitServer(String moduleName, String host, String user, String password, boolean silent) {
        this.MODULE_NAME = moduleName;
        this.MODULE_DESCRIPTION = moduleName + "-java/0.1";
        this.HOST = "omcp://" + moduleName + "/";
        this.socket = new RabbitServerSocket(DOMAIN + reverseDottedName(moduleName), host, user, password, silent);
        this.socket.setListener(this);
        this.running = true;
    }

    public RabbitServer(String moduleName, String host, String user, String password) {
        this(moduleName, host, user, password, false);
    }

    private String reverseDottedName(String name) {
        if (name.contains(".")) {
            String names[] = name.split("\\.");
            StringBuilder sb = new StringBuilder();
            for (int i = names.length - 1; i >= 0; i--) {
                if (sb.length() > 0) {
                    sb.append(".");
                }
                sb.append(names[i]);
            }
            return sb.toString();
        }
        return name;
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
        } catch (Exception ex) {
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
    public void linkToMessageGroup(String url) {
        socket.bindToExchange(url);
    }

    @Override
    public void createMessageGroup(String url) {
        socket.createExchange(url);
    }
}
