/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.impl.AMQChannel;
import java.io.IOException;
import javax.management.RuntimeErrorException;

/**
 *
 * @author Felipe
 */
public class RabbitBridge {

    private Connection connection;
    private Channel channel;
    private String replyQueueName;
    private QueueingConsumer consumer;

    public RabbitBridge(String host, String user, String pass) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

        if (user != null && pass != null) {
            factory.setUsername(user);
            factory.setPassword(pass);
        }

        connection = factory.newConnection();
        channel = connection.createChannel();
        replyQueueName = channel.queueDeclare().getQueue();
        consumer = new QueueingConsumer(channel);
        channel.basicConsume(replyQueueName, true, consumer);
    }

    public RabbitBridge(String host) throws Exception {
        this(host, null, null);
    }

    /**
     * This method sends persistent messages (".deliveryMode(2)") to Osiris
     * modules and it contains no response. Method to notification event
     *
     *
     * @param queue - Osiris module's queue name
     * @param packet - Osiris packet
     * @throws Exception
     */
    public void publish(String queue, String packet) throws Exception {
        BasicProperties props = new BasicProperties.Builder()
                .deliveryMode(2)//persistent
                .contentType("text/plain")
                .contentEncoding("UTF-8")
                .build();

        sendMessage(props, queue, packet);
    }

    /**
     * This method executes rpc calls to Osiris' modules and receives a response
     * packet. Method to RPC call
     *
     * @param queue - Osiris module's queue name
     * @param packet - Osiris packet
     * @throws Exception
     */
    public void call(String queue, String packet) throws Exception {
        //adicionar o timeout para o encerramento
        //criar códigos de erro do cliente - exceptions
        String response = null;
        String corrId = java.util.UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .deliveryMode(1)//no-persistent
                .contentType("text/plain")
                .contentEncoding("UTF-8")
                .expiration("12000")//auto delete itself after this time
                .build();

        sendMessage(props, queue, packet);

//        while (true) {
//            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
//                response = new String(delivery.getBody());
//                break;
//            }
//        }
//
//        return response;
    }

    public void close() throws Exception {
        connection.close();
    }

    /**
     * Method alternative to check whether a queue exists or not
     * Need rewriting!
     * @param props - Properties
     * @param queueName - Name of queue
     * @param message - Text message
     * @throws IOException 
     */
    private void sendMessage(BasicProperties props, String queueName, String message) throws IOException {
        try {
            //checks if a queue exists
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            channel.queueDelete(queueName);
            throw new RuntimeException("Fila já existe");
        } catch (IOException e) {
            channel.basicPublish("", queueName, props, message.getBytes());
        }     
    }
}
