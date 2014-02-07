/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.remotestorage.amqp;

/**
 *
 * @author Felipe
 */
public interface RPCServer <T>{

    public void start();

    public void close()throws Exception;
}
