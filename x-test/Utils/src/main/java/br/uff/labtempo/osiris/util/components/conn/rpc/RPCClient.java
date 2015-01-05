/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components.conn.rpc;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
interface RPCClient <T> {

    public void connect() throws Exception;
    
    public T createProxy()  throws Exception;

    public void close() throws Exception;

}
