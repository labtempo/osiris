/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components.conn;

import br.uff.labtempo.osiris.util.components.Component;
import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Service;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.tools.jsonrpc.JsonRpcClient;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class JSONRpcClient extends Component {

    private String AMQPHostAddress;
    private String AMQPResourceName;

    private String AMQPRoutingKey;
    private String AMQPQueue;

    private Connection AMQPConnection;
    private Channel AMQPChannel;
    private JsonRpcClient RabbitmqRpcClient;

    private Class proxyClass;
    private Object proxyInstance;

    public JSONRpcClient(String AMQPResourceName, String AMQPHostAddress, Class<?> proxyClass) {
        this.AMQPHostAddress = AMQPHostAddress;
        this.AMQPResourceName = AMQPResourceName;
        this.proxyClass = proxyClass;
    }

    public <T> T getProxy() {
        return (T) proxyInstance;
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            Properties prop = Config.getProperties();
            AMQPRoutingKey = prop.getProperty("amqp.rpc.routing.key") + AMQPResourceName;
            AMQPQueue = prop.getProperty("amqp.rpc.exchange");
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
            RabbitmqRpcClient = new JsonRpcClient(AMQPChannel, AMQPQueue, AMQPRoutingKey, 60);
            proxyInstance = RabbitmqRpcClient.createProxy(proxyClass);
        } catch (Exception ex) {
            throw new ComponentInitializationException(ex);
        }
    }

    @Override
    protected void onStop() {
        try {
            if (RabbitmqRpcClient != null) {
                RabbitmqRpcClient.close();
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
