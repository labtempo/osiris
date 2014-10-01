/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.service.rabbitmq;

import br.uff.labtempo.omcp.server.rabbitmq.*;
import br.uff.labtempo.omcp.common.exceptions.ConnectionException;
import br.uff.labtempo.omcp.common.utils.RabbitUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;

/**
 *
 * @author Felipe
 */
public final class RabbitServiceSocket extends RabbitServerSocket {

    private final boolean persistentService;

    public RabbitServiceSocket(String queueName, String host, String user, String password, boolean persistentService) {
        super(queueName, host, user, password);
        this.persistentService = persistentService;
    }

    @Override
    protected void declareQueue() {
        /*
         * Servidor conectado
         * 
         * Declarar fila
         * Verificar se existem consumidores
         * Se existirem consumidores, abortar a execução do servidor
         * Se declarar, o servidor estará funcionando
         * 
         * Connected to the server
         * 
         * Declare queue
         * Checks if consumers exist
         * If has consumers, abort server starting
         * If can declare a queue, server is working
         */
        try {
            System.out.print("[x] Checking queue " + QUEUE_NAME + ": ");
            if (persistentService) {
                RabbitUtil.declareDurableQueue(channel, QUEUE_NAME);
            } else {
                QUEUE_NAME = RabbitUtil.declareTemporaryQueue(channel).getQueue();
            }
            channel.basicQos(1);
            System.out.println(OK);
        } catch (IOException ex) {
            System.out.println(ERROR);
            throw new ConnectionException("Server start aborted!");
        }
    }

    /*
     * Exemplo:
     * 1 - notify precisa ser processado e depois enviado o ack pra remoção do broker.
     * 2 - RPC precisa ser dado ack assim que recebe a mensagem do broker.
     * 
     * Example:
     * 1 - NOTIFY method needs be processed and after, it should sends the ack to remove the message from broker.
     * 2 - The other methods(GET,PUT,POST,DELETE) need a fast reply ack when the message is incoming.
     */
    @Override
    public void startListener(QueueingConsumer consumer) throws IOException, InterruptedException {
        try {
            System.out.println("...Awaiting requests");
            System.out.println(".");
            while (running) {
                QueueingConsumer.Delivery delivery = null;
                try {
                    delivery = consumer.nextDelivery();
                } catch (ConsumerCancelledException ex) {
                    running = false;
                }
                //TODO: if you want use threads, put it here!
                //One solution: create a temp queue!
                if (delivery != null) {
                    AMQP.BasicProperties props = delivery.getProperties();
                    String message = new String(delivery.getBody());
                    System.out.println(message);

                    if (props.getReplyTo() == null) {
                        try {
                            //TODO: here, if server shutdowning, the message will be lost!
                            if (eventConsumer(message)) {
                                System.out.println("ERROR EVENT!");
                            }
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        } catch (Exception ex) {
                            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                        }
                    }
                }
            }
        } catch (InterruptedException ex) {
            System.out.println(ERROR);
            throw ex;
        } catch (ShutdownSignalException ex) {
            //System.out.println(ERROR);
            throw ex;
        } 
    }
}
