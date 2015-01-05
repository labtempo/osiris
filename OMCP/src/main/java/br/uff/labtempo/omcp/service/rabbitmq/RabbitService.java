/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.service.rabbitmq;

import br.uff.labtempo.omcp.server.rabbitmq.*;
import br.uff.labtempo.omcp.common.utils.RequestPacket;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.service.EventHandler;
import br.uff.labtempo.omcp.service.OmcpService;

/**
 *
 * @author Felipe
 */
public class RabbitService implements OmcpService, RabbitListener {

    private final ServerSocket socket;
    private boolean running;
    private EventHandler handler;

    public RabbitService(String host, String user, String password) {
        this.socket = new RabbitServiceSocket(null, host, user, password, false);
        this.socket.setListener(this);
        this.running = true;
    }

    @Override
    public void start() {
        while (running) {
            try {
                this.socket.run();
            } catch (Exception e) {
                this.socket.close();
            }
        }
        System.out.println("Service shutdown complete!");
    }

    public void purge() {
        ((RabbitServerSocket) socket).purge();
    }

    @Override
    public boolean incoming(String message, ServerResponseContainer responseContainer) {
        boolean process = false;

        try {
            Request request = new RequestPacket().parse(message);
            handler.process(request);
            process = true;
        } catch (Exception ex) {

        }

        return process;
    }

    @Override
    public void setHandler(EventHandler handler) {
        this.handler = handler;
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
