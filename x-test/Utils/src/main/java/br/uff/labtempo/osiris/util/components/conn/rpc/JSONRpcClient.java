/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components.conn.rpc;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.jsonrpc.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author Felipe
 */
class JSONRpcClient<T> implements RPCClient {

    private Connection connection;
    private Channel channel;

    private JsonRpcClient client;
    private T service;

    private String serverQueue;
    private String routingKey;
    private Class klass;
    private String host;

    public JSONRpcClient(String host, String serverQueue, String routingKey, Class<?> klass) {
        this.host = host;
        this.klass = klass;
        this.serverQueue = serverQueue;
        this.routingKey = routingKey;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    @Override
    public T createProxy() throws Exception {
        connect();
        service = (T) client.createProxy(klass);
        return service;
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.close();
        }
        if (channel != null) {
            channel.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void connect() throws IOException, TimeoutException, RPCClientException {

        try {
            defineConnection();
            client = new JsonRpcClient(channel, serverQueue, routingKey, 60);
        } catch (TimeoutException e) {
            close();
            throw e;
        }catch (JsonRpcException e ){
            throw new RPCClientException("Client connection unexpected error",e);
        }
    }

    private void defineConnection() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();
    }
}