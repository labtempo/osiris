/* 
 * Copyright 2015 Felipe Santos <fralph at ic.uff.br>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.uff.labtempo.omcp.service.rabbitmq;

import br.uff.labtempo.omcp.server.rabbitmq.*;
import br.uff.labtempo.omcp.common.exceptions.client.ConnectionException;
import br.uff.labtempo.omcp.common.utils.RabbitUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public final class RabbitServiceSocket extends RabbitServerSocket {

    private final boolean persistentService;

    public RabbitServiceSocket(String queueName, String host, String user, String password, boolean persistentService, boolean silent) {
        super(queueName, host, user, password, silent);
        this.persistentService = persistentService;
    }

    public RabbitServiceSocket(String queueName, String host, String user, String password, boolean persistentService) {
        this(queueName, host, user, password, persistentService, false);
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
                    if (!silent) {
                        System.out.println(message);
                    }

                    if (props.getReplyTo() == null) {
                        try {
                            //TODO: here, if server shutdown, the message will be lost!
                            if (!eventConsumer(message)) {
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
