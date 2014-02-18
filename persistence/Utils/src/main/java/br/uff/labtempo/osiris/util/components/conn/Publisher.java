/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components.conn;

import br.uff.labtempo.osiris.util.components.Component;
import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Properties;

/**
 *
 * @author Felipe
 */
public class Publisher extends Component {

    private String AMQPHostAddress;
    private String AMQPExchange;
    private String AMQPExchangeType;
    private String AMQPResourceName;
    private String AMQPRoutingKey;
    private Connection AMQPConnection;
    private Channel AMQPChannel;

    public Publisher(String AMQPResourceName, String AMQPHostAddress) {
        this.AMQPHostAddress = AMQPHostAddress;
        this.AMQPResourceName = AMQPResourceName;
    }

    @Override
    protected void beforeBoot() throws ComponentInitializationException {
        try {
            Properties prop = Config.getProperties();
            AMQPExchange = prop.getProperty("amqp.event.exchange");
            AMQPExchangeType = prop.getProperty("amqp.event.exchange.type");
            AMQPRoutingKey = prop.getProperty("amqp.event.routing.key") + AMQPResourceName;
        } catch (Exception ex) {
            throw new ComponentInitializationException(ex);
        }
    }

    @Override
    protected void boostrap() throws ComponentInitializationException {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(AMQPHostAddress);
            AMQPConnection = factory.newConnection();
            AMQPChannel = AMQPConnection.createChannel();
            AMQPChannel.exchangeDeclare(AMQPExchange, AMQPExchangeType);
        } catch (Exception ex) {
            throw new ComponentInitializationException(ex);
        }
    }

    @Override
    protected void shutdown() {
        try {
            if (AMQPChannel != null) {
                AMQPChannel.close();
            }
            if (AMQPConnection != null) {
                AMQPConnection.close();
            }
        } catch (Exception ex) {
        }
    }

    public boolean publish(String message) {
        try {
            AMQPChannel.basicPublish(AMQPExchange, AMQPRoutingKey, null, message.getBytes());
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
