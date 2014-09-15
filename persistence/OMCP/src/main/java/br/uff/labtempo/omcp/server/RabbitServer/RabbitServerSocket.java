/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.RabbitServer;

import br.uff.labtempo.omcp.common.exceptions.ConnectionException;
import br.uff.labtempo.omcp.common.exceptions.UnreachableModuleException;
import br.uff.labtempo.omcp.common.utils.RabbitComm;
import br.uff.labtempo.omcp.server.RequestHandler;
import br.uff.labtempo.omcp.server.core.ServerResponseContainer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class RabbitServerSocket implements ServerSocket {

    private final String QUEUE_NAME;
    private final int RETRY_TIME = 30;//SECONDS
    private RabbitListener listener;
    private RabbitComm comm;
    private Channel channel;

    private final String ERROR = "ERROR!";
    private final String OK = "OK!";
    private final String ABORT = "ABORT: ";

    private boolean closing;

    RabbitServerSocket(String queueName, String host, String user, String password) {
        QUEUE_NAME = queueName;
        this.comm = new RabbitComm(host, user, password);
    }

    public void setListener(RabbitListener listener) {
        this.listener = listener;
    }

    public void run() {
        try {
            connect();
            declareQueue();
            QueueingConsumer consumer = createConsumer();
            startListener(consumer);
        } catch (Exception e) {
            System.out.println(ABORT + e.getMessage());
            comm.close();
            throw new RuntimeException("server down!");
        }
    }

    public void abort() {
        comm.close();
    }

    public void purge() {
        try {
            channel.close();
            comm.purgeQueueOrDie(QUEUE_NAME);
            comm.close();
        } catch (IOException ex) {
            Logger.getLogger(RabbitServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void connect() {

        /**
         * Servidor não conectado
         *
         * Se não conectado, informar ao cliente e ficar tentando conectar(mesmo
         * ao cair) Teste de máquina e do servidor rabbit Se conectar, ir para a
         * declaração de fila
         *
         * Not connected to the server
         *
         * If not connected, announce to the client and try to connect Test the
         * rabbit' machine and its service If connected, go to the queue
         * declaration
         */
        while (true) {
            try {
                System.out.print("[x] Connecting to broker: ");
                this.comm.connect();
                System.out.println(OK);
                break;
            } catch (Exception e) {
                System.out.println(ERROR);
                System.out.println("...Retry in " + RETRY_TIME + " seconds");
                sleep();
            }
        }

        try {
            System.out.print("[x] Creating channel: ");
            this.channel = comm.newChannel();
            System.out.println(OK);
        } catch (Exception e) {
            System.out.println(ERROR);
            throw e;
        }
    }

    private void declareQueue() {
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
            channel.queueDeclare(QUEUE_NAME, true, false, false, getArgs());
            channel.basicQos(1);
            System.out.println(OK);
        } catch (IOException ex) {
            System.out.println(ERROR);
            throw new ConnectionException("Server start aborted!");
        }
    }

    private QueueingConsumer createConsumer() {

        //evita iniciar dois ou mais consumidores 
        //avoid to start two or more consumers
        checkNotHasConsumerOrDie();

        System.out.print("[x] Creating consumer: ");
        QueueingConsumer consumer = new QueueingConsumer(channel);
        try {
            channel.basicConsume(QUEUE_NAME, false, consumer);
            comm.checkHasOneConsumerOrDie(QUEUE_NAME);
            System.out.println(OK);
        } catch (Exception ex) {
            System.out.println(ERROR);
            throw new UnreachableModuleException("Could not create consumer!", ex);
        }

        //verifica se existe apenas um consumidor
        //checks if only consumer exists
        checkHasOneConsumerOrDie();

        return consumer;
    }

    private void checkHasOneConsumerOrDie() {
        try {
            System.out.print("[x] Checking if has only one consumer: ");
            this.comm.checkHasOneConsumerOrDie(QUEUE_NAME);
            System.out.println(OK);
        } catch (Exception e) {
            System.out.println(ERROR);
            throw e;
        }
    }

    private void checkNotHasConsumerOrDie() {
        try {
            System.out.print("[x] Checking if not has consumers: ");
            this.comm.checkNotHasConsumersOrDie(QUEUE_NAME);
            System.out.println(OK);
        } catch (Exception e) {
            System.out.println(ERROR);
            throw e;
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
    public void startListener(QueueingConsumer consumer) throws IOException, InterruptedException {
        try {
            System.out.println("...Awaiting requests");
            System.out.println(".");
            while (true) {
                final QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                //TODO: if you want use threads, put it here!
                //One solution: create a temp queue!
                final AMQP.BasicProperties props = delivery.getProperties();
                String message = new String(delivery.getBody());
                System.out.println(message);

                if (props.getReplyTo() != null) {
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    this.rpcConsumer(message, props);
                } else {
                    try {
                        //TODO: here, if server shutdowning, the message will be lost!
                        if (this.eventConsumer(message)) {
                            System.out.println("ERROR EVENT!");
                        }
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    } catch (Exception ex) {
                        channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                    }
                }
            }
        } catch (InterruptedException ex) {
            System.out.println(ERROR);
            throw ex;
        } catch (ShutdownSignalException ex) {
            System.out.println(ERROR);
            throw ex;
        } catch (ConsumerCancelledException ex) {
            System.out.println(ERROR);
            throw ex;
        }
    }

    private boolean eventConsumer(String message) {
        return listener.incoming(message, null);
    }

    private void rpcConsumer(String message, final AMQP.BasicProperties props) {
        final AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                .correlationId(props.getCorrelationId())
                .build();
        ServerResponseContainer serverResponse = new ServerResponseContainer() {
            @Override
            public void exec(String response) {
                if (props.getReplyTo() != null) {
                    try {
                        channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes());
                    } catch (Exception ex) {
                        Logger.getLogger(RabbitServerSocket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        listener.incoming(message, serverResponse);
    }

    private Map<String, Object> getArgs() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 60000);
        return args;
    }

    private void sleep() {
        try {
            Thread.sleep(RETRY_TIME * 1000);
        } catch (Exception ex) {
        }
    }
}
