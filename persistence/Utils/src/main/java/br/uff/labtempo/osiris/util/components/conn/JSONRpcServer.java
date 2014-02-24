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

    private String AMQPHostAddress;
    private String AMQPResourceName;

    private String AMQPExchange;
    private String AMQPExchangeType;
    private String AMQPRoutingKey;
    private String AMQPQueue;

    private Connection AMQPConnection;
    private Channel AMQPChannel;
    private JsonRpcServer RabbitmqRpcServer;

    private Class proxyClass;
    private Object proxyInstance;

    public JSONRpcServer(String AMQPResourceName, String AMQPHostAddress, Object proxyInstance, Class<?> proxyClass) {
        super("JSON RPC Server: " + AMQPResourceName);
        this.AMQPHostAddress = AMQPHostAddress;
        this.AMQPResourceName = AMQPResourceName;
        this.proxyClass = proxyClass;
        this.proxyInstance = proxyInstance;
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            Properties prop = Config.getProperties();
            AMQPExchange = prop.getProperty("amqp.rpc.exchange");
            AMQPExchangeType = prop.getProperty("amqp.rpc.exchange.type");;
            AMQPRoutingKey = prop.getProperty("amqp.rpc.routing.key") + AMQPResourceName;
            AMQPQueue = AMQPRoutingKey;
        } catch (Exception ex) {
            throw new ComponentInitializationException(ex);
        }
    }

    @Override
    protected void onStart() throws ComponentInitializationException {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(AMQPHostAddress);
            AMQPConnection = factory.newConnection();
            AMQPChannel = AMQPConnection.createChannel();
            AMQPChannel.exchangeDeclare(AMQPExchange, AMQPExchangeType);
            AMQPChannel.queueDeclare(AMQPQueue, false, true, true, null);
            AMQPChannel.queueBind(AMQPQueue, AMQPExchange, AMQPRoutingKey);
            RabbitmqRpcServer = new JsonRpcServer(AMQPChannel, AMQPQueue, proxyClass, proxyInstance);
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
            if (AMQPChannel != null) {
                AMQPChannel.close();
            }
        } catch (Exception ex) {
        }
        try {
            if (AMQPConnection != null) {
                AMQPConnection.close();
            }
        } catch (Exception ex) {
        }
    }
}
