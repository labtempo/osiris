/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components.conn;

import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Service;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.jsonrpc.JsonRpcServer;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Felipe
 */
public class JSONRpcServer extends Service {

    private String AmqpHostAddress;
    private String AmqpResourceName;

    private String AmqpExchange;
    private String AmqpExchangeType;
    private String AmqpRoutingKey;
    private String AmqpQueue;

    private Connection AmqpConnection;
    private Channel AmqpChannel;
    private JsonRpcServer RabbitmqRpcServer;

    private Class proxyClass;
    private Object proxyInstance;

    public JSONRpcServer(String AMQPResourceName, String AMQPHostAddress, Object proxyInstance, Class<?> proxyClass) {
        super("JSON RPC Server: " + AMQPResourceName);
        this.AmqpHostAddress = AMQPHostAddress;
        this.AmqpResourceName = AMQPResourceName;
        this.proxyClass = proxyClass;
        this.proxyInstance = proxyInstance;
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            Properties prop = Config.getProperties();
            AmqpExchange = prop.getProperty("amqp.rpc.exchange");
            AmqpExchangeType = prop.getProperty("amqp.rpc.exchange.type");;
            AmqpRoutingKey = prop.getProperty("amqp.rpc.routing.key") + AmqpResourceName;
            AmqpQueue = AmqpRoutingKey;
        } catch (Exception ex) {
            throw new ComponentInitializationException(ex);
        }
    }

    @Override
    protected void onStart() throws ComponentInitializationException {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(AmqpHostAddress);
            AmqpConnection = factory.newConnection();
            AmqpChannel = AmqpConnection.createChannel();
            AmqpChannel.exchangeDeclare(AmqpExchange, AmqpExchangeType);
            AmqpChannel.queueDeclare(AmqpQueue, false, true, true, null);
            AmqpChannel.queueBind(AmqpQueue, AmqpExchange, AmqpRoutingKey);
            RabbitmqRpcServer = new JsonRpcServer(AmqpChannel, AmqpQueue, proxyClass, proxyInstance);
        } catch (Exception ex) {
            throw new ComponentInitializationException(ex);
        }
    }

    @Override
    protected void onLoop() throws ComponentInitializationException {
        try {
            RabbitmqRpcServer.mainloop();
        } catch (IOException ex) {
            throw new ComponentInitializationException(ex);
        }
    }

    @Override
    protected void onStop() {
        try {
            if (RabbitmqRpcServer != null) {
                RabbitmqRpcServer.terminateMainloop();
            }
        } catch (Exception ex) {
        }
        try {
            if (AmqpChannel != null) {
                AmqpChannel.close();
            }
        } catch (Exception ex) {
        }
        try {
            if (AmqpConnection != null) {
                AmqpConnection.close();
            }
        } catch (Exception ex) {
        }
    }
}
