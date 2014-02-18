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
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.tools.jsonrpc.JsonRpcServer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class Subscriber extends Service {

    private String AMQPHostAddress;
    private String[] AMQPSubjects;

    private String AMQPExchange;
    private String AMQPExchangeType;
    private List<String> AMQPBindingKeys;
    private String AMQPQueue;

    private Connection AMQPConnection;
    private Channel AMQPChannel;
    private QueueingConsumer RabbitmqConsumer;

    private OnMessageListener listener;
    private boolean active = true;

    public Subscriber(String[] AMQPSubjects, String AMQPHostAddress, OnMessageListener listener) {
        super("Subscriber");
        this.AMQPHostAddress = AMQPHostAddress;
        this.AMQPSubjects = AMQPSubjects;
        this.listener = listener;
        AMQPBindingKeys = new ArrayList<String>();
    }

    @Override
    protected void beforeBoot() throws ComponentInitializationException {
        try {
            Properties prop = Config.getProperties();
            AMQPExchange = prop.getProperty("amqp.event.exchange");
            AMQPExchangeType = prop.getProperty("amqp.event.exchange.type");

            for (String subject : AMQPSubjects) {
                AMQPBindingKeys.add(prop.getProperty("amqp.event.routing.key") + subject);
            }

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
            AMQPQueue = AMQPChannel.queueDeclare().getQueue();

            for (String bindingKey : AMQPBindingKeys) {
                AMQPChannel.queueBind(AMQPQueue, AMQPExchange, bindingKey);
            }
            
        } catch (Exception ex) {
            throw new ComponentInitializationException(ex);
        }
    }

    @Override
    protected void loop() throws ComponentInitializationException {
        try {
            RabbitmqConsumer = new QueueingConsumer(AMQPChannel);
            try {
                AMQPChannel.basicConsume(AMQPQueue, true, RabbitmqConsumer);
            } catch (IOException ex) {
                Logger.getLogger(Subscriber.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (active) {
                QueueingConsumer.Delivery delivery;
                try {
                    delivery = RabbitmqConsumer.nextDelivery();
                    listener.onReceiveMessage(new String(delivery.getBody()), getSubject(delivery.getEnvelope().getRoutingKey()));
                } catch (ShutdownSignalException ex) {
                    Logger.getLogger(Subscriber.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ConsumerCancelledException ex) {
                    Logger.getLogger(Subscriber.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (InterruptedException ex) {
            throw new ComponentInitializationException(ex);
        }
    }

    @Override
    protected void shutdown() {
        try {
            active = false;
            if (AMQPChannel != null) {
                AMQPChannel.close();
            }
            if (AMQPConnection != null) {
                AMQPConnection.close();
            }
        } catch (IOException ex) {
        }
    }
    
    private String getSubject(String routingKey){
        if(AMQPBindingKeys.contains(routingKey)){
            int i = AMQPBindingKeys.indexOf(routingKey);
            return AMQPSubjects[i];
        }        
        return null;
    }

}
