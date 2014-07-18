/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class OmcpConnection {
    private RabbitBridge bridge;

    public OmcpConnection(String host, String user, String password) {
        try {
            this.bridge = new RabbitBridge(host, user, password);
        } catch (Exception ex) {
            Logger.getLogger(OmcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public OmcpConnection(String host) {
        try {
            this.bridge = new RabbitBridge(host);
        } catch (Exception ex) {
            Logger.getLogger(OmcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String doGet(String url) {
        return null;
    }

    public String doPost(String url, String content) {
        return null;
    }

    public String doPut(String url, String content) {
        return null;
    }

    public String doDelete(String url) {
        return null;
    }

    public void doNofity(String url) {

    }

}
