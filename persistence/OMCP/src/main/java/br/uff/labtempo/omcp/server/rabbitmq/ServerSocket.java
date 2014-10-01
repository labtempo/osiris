/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.rabbitmq;

/**
 *
 * @author Felipe
 */
public interface ServerSocket extends AutoCloseable{

    void setListener(RabbitListener listener);

    void addReference(String url);

    void run();

    @Override
    void close();
}
