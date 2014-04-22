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

    private String AmqpHostAddress;
    private String AmqpResourceName;

    private String AmqpRoutingKey;
    private String AmqpQueue;

    private Connection AmqpConnection;
    private Channel AmqpChannel;
    private JsonRpcClient RabbitmqRpcClient;

    private Class proxyClass;
    private Object proxyInstance;

    public JSONRpcClient(String AMQPResourceName, String AMQPHostAddress, Class<?> proxyClass) {
        this.AmqpHostAddress = AMQPHostAddress;
        this.AmqpResourceName = AMQPResourceName;
        this.proxyClass = proxyClass;
    }

    public <T> T getProxy() {
        return (T) proxyInstance;
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            Properties prop = Config.getProperties();
            AmqpRoutingKey = prop.getProperty("amqp.rpc.routing.key") + AmqpResourceName;
            AmqpQueue = prop.getProperty("amqp.rpc.exchange");
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
            RabbitmqRpcClient = new JsonRpcClient(AmqpChannel, AmqpQueue, AmqpRoutingKey, 6000);
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
