/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.client;

/**
 *
 * @author Felipe
 */
public class OmcpConnectionBuilder {

    private String host;
    private String user;
    private String pass;

    public OmcpConnectionBuilder host(String host) {
        this.host = host;
        return this;
    }

    public OmcpConnectionBuilder user(String user, String pass) {
        this.pass = pass;
        this.user = user;
        return this;
    }

    public OmcpConnection build() {
        if (user != null) {
            return new OmcpConnection(host, user, pass);
        }
        return new OmcpConnection(host);
    }

}
