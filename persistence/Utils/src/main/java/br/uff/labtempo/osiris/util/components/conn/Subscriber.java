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

    private String AmqpHostAddress;
    private String[] AmqpSubjects;

    private String AmqpExchange;
    private String AmqpExchangeType;
    private List<String> AmqpBindingKeys;
    private String AmqpQueue;

    private Connection AmqpConnection;
    private Channel AmqpChannel;
    private QueueingConsumer RabbitmqConsumer;

    private String routingKeyPattern;
    private OnMessageListener listener;
    private boolean active = true;

    public Subscriber(String[] AMQPSubjects, String AMQPHostAddress, OnMessageListener listener) {
        super("Subscriber");
        this.AmqpHostAddress = AMQPHostAddress;
        this.AmqpSubjects = AMQPSubjects;
        this.listener = listener;
        AmqpBindingKeys = new ArrayList<String>();
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            Properties prop = Config.getProperties();
            AmqpExchange = prop.getProperty("amqp.event.exchange");
            AmqpExchangeType = prop.getProperty("amqp.event.exchange.type");
            routingKeyPattern = prop.getProperty("amqp.event.routing.key");
            for (String subject : AmqpSubjects) {
                AmqpBindingKeys.add(routingKeyPattern + subject);
            }

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
            AmqpQueue = AmqpChannel.queueDeclare().getQueue();

            for (String bindingKey : AmqpBindingKeys) {
                AmqpChannel.queueBind(AmqpQueue, AmqpExchange, bindingKey);
            }
            RabbitmqConsumer = new QueueingConsumer(AmqpChannel);
            AmqpChannel.basicConsume(AmqpQueue, true, RabbitmqConsumer);
        } catch (Exception ex) {
            throw new ComponentInitializationException(ex);
        }
    }

    @Override
    protected void onLoop() throws ComponentInitializationException {
        int i = 0;
        try {
            while (active) {
                QueueingConsumer.Delivery delivery;
                try {
                    delivery = RabbitmqConsumer.nextDelivery();
                    listener.onReceiveMessage(new String(delivery.getBody()), getSubject(delivery.getEnvelope().getRoutingKey()));
                } catch (ShutdownSignalException | ConsumerCancelledException ex) {
                    break;
                }
            }
        } catch (InterruptedException ex) {
            throw new ComponentInitializationException(ex);
        }
        System.out.println("saindo do loop");
    }

    @Override
    protected void onStop() {

        active = false;

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

    private String getSubject(String routingKey) {
        if (AmqpBindingKeys.contains(routingKey)) {
            int i = AmqpBindingKeys.indexOf(routingKey);
            return AmqpSubjects[i];
        }else{
            return routingKey.replace(routingKeyPattern, "");            
        }
    }

}
