/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server;

import br.uff.labtempo.omcp.server.core.ServerSocket;
import br.uff.labtempo.omcp.server.core.ServerRequestListener;
import br.uff.labtempo.omcp.server.core.ServerResponseContainer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Felipe
 */
public class RabbitServerSocket implements ServerSocket {
    //adicionar commit de retirada(ack ou transaction)
    private String QUEUE_NAME = "osiris.sensornet";
    private ServerRequestListener listener;

    public void setListener(ServerRequestListener listener) {
        this.listener = listener;
    }

    public void run() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.0.7");
            factory.setUsername("admin");
            factory.setPassword("admin");

            Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicQos(1);
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(QUEUE_NAME, false, consumer);
            System.out.println(" [x] Awaiting RPC requests");

            while (true) {
                final QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                final AMQP.BasicProperties props = delivery.getProperties();
                final AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                        .correlationId(props.getCorrelationId())
                        .build();
                String message = new String(delivery.getBody());
                System.out.println(message);
                ServerResponseContainer serverResponse = new ServerResponseContainer() {
                    @Override
                    public void exec(String response) {
                        try {
                            channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes());
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        } catch (IOException ex) {
                            Logger.getLogger(RabbitServerSocket.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
                listener.incoming(message, serverResponse);
            }

        } catch (IOException ex) {
            Logger.getLogger(RabbitServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(RabbitServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ShutdownSignalException ex) {
            Logger.getLogger(RabbitServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConsumerCancelledException ex) {
            Logger.getLogger(RabbitServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
