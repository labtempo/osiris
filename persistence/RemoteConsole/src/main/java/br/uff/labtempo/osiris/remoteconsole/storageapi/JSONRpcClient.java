/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.remoteconsole.storageapi;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.RpcClient;
import com.rabbitmq.tools.jsonrpc.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class  JSONRpcClient <T>  implements RPCClient {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";
    private JsonRpcClient client;
    private T service;
    private Class klass;
    private String host;

    public JSONRpcClient(Class<?> klass, String host) {
        this.host = host;
        this.klass = klass;
    }

    @Override
    public T build()  throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();

        client = new JsonRpcClient(channel, "", requestQueueName);

        service = (T) client.createProxy(klass);
        
        return service;
    }

    @Override
    public void close() throws Exception {
        client.close();
        channel.close();
        connection.close();
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

}
