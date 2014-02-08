/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.connection.client;

/**
 *
 * @author Felipe
 */
public interface RPCClient <T> {

    public void connect() throws Exception;
    
    public T createProxy()  throws Exception;

    public void close() throws Exception;

}
