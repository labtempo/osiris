/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server;

/**
 *
 * @author Felipe
 */
public interface OmcpServer extends AutoCloseable {
    void start();
    void addReference(String url);
    void setHandler(RequestHandler handler);
}
