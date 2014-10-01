/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.client;

import br.uff.labtempo.omcp.client.rabbitmq.RabbitClient;
import br.uff.labtempo.omcp.common.exceptions.ConnectionException;

/**
 *
 * @author Felipe
 */
public class OmcpClientBuilder {

    private String host;
    private String user;
    private String pass;
    private String source;

    public OmcpClientBuilder host(String host) {
        this.host = host;
        return this;
    }

    public OmcpClientBuilder user(String user, String pass) {
        this.pass = pass;
        this.user = user;
        return this;
    }

    public OmcpClientBuilder source(String source) {
        this.source = source;
        return this;
    }

    public OmcpClient build() throws ConnectionException {
        OmcpClient client = null;
        if (user != null) {
            client = new RabbitClient(host, user, pass);
        } else {
            client = new RabbitClient(host);
        }
        if (source != null) {
            client.setSourceId(source);
        }
        return client;
    }
}
