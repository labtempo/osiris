/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components.conn;

import br.uff.labtempo.osiris.util.components.Component;
import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.logging.Log;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Properties;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Publisher extends Component {

    private String AmqpHostAddress;
    private String AmqpExchange;
    private String AmqpExchangeType;
    private String AmqpResourceName;
    private String AmqpRoutingKey;
    private Connection AmqpConnection;
    private Channel AmqpChannel;

    public Publisher(String AMQPResourceName, String AMQPHostAddress) {
        this.AmqpHostAddress = AMQPHostAddress;
        this.AmqpResourceName = AMQPResourceName;
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            Properties prop = Config.getProperties();
            AmqpExchange = prop.getProperty("amqp.event.exchange");
            AmqpExchangeType = prop.getProperty("amqp.event.exchange.type");
            AmqpRoutingKey = prop.getProperty("amqp.event.routing.key") + AmqpResourceName;
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
        } catch (Exception ex) {
            throw new ComponentInitializationException(ex);
        }
    }

    @Override
    protected void onStop() {
        Log.D("Publisher is stoping");
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

    public boolean isActive() {
        if (AmqpChannel.isOpen() && AmqpConnection.isOpen()) {
            return true;
        }
        return false;
    }

    public synchronized boolean publish(String message) {
        try {
            AmqpChannel.basicPublish(AmqpExchange, AmqpRoutingKey, null, message.getBytes());
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    
    public synchronized boolean publish(String message, String resourceId) {
        try {
            AmqpChannel.basicPublish(AmqpExchange, AmqpRoutingKey+"."+resourceId, null, message.getBytes());
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
