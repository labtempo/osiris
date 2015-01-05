/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.omcp.service;

/**
 *
 * @author Felipe
 */
public interface OmcpService extends AutoCloseable{
    void addReference(String url);
    void setHandler(EventHandler handler);
    void start();
}
