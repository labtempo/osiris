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
package br.uff.labtempo.omcp.server.rabbitmq;

import br.uff.labtempo.omcp.common.exceptions.client.ConnectionException;
import br.uff.labtempo.omcp.common.exceptions.client.UnreachableModuleException;
import br.uff.labtempo.omcp.common.utils.RabbitComm;
import br.uff.labtempo.omcp.common.utils.RabbitUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RabbitServerSocket implements ServerSocket {

    protected String QUEUE_NAME;
    private final int RETRY_TIME = 30;//SECONDS
    private RabbitListener listener;
    protected final RabbitComm comm;
    protected Channel channel;

    protected final String ERROR = "ERROR!";
    private final String FAIL = "FAIL!";
    protected final String OK = "OK!";
    private final String ABORT = "ABORT: ";

    private String consumerTag;

    protected boolean running;

    private final ArrayList<String> exchanges;
    private final ArrayList<String> newExchanges;
    private boolean isStarted;
    protected boolean silent;

    public RabbitServerSocket(String queueName, String host, String user, String password, boolean silent) {
        QUEUE_NAME = queueName;
        this.comm = new RabbitComm(host, user, password);
        this.exchanges = new ArrayList<>();
        this.newExchanges = new ArrayList<>();
        this.silent = silent;
    }

    public RabbitServerSocket(String queueName, String host, String user, String password) {
        this(queueName, host, user, password, false);
    }

    @Override
    public void setListener(RabbitListener listener) {
        this.listener = listener;
    }

    @Override
    public void bindToExchange(String url) {
        exchanges.add(url);
    }

    @Override
    public void createExchange(String url) {
        newExchanges.add(url);
    }

    @Override
    public void run() {
        try {
            running = true;
            connect();
            declareQueue();
            bindToExchanges();
            createExchanges();
            QueueingConsumer consumer = createConsumer();
            isStarted = true;
            startListener(consumer);
        } catch (Exception e) {
            isStarted = false;
            System.out.println(ABORT + e.getMessage());
        } finally {
            try {
                channel.close();
            } finally {
                try {
                    comm.close();
                } finally {
                    throw new RuntimeException("server down!");
                }
            }
        }
    }

    @Override
    public void close() {
        running = false;
        try {
            //channel.basicCancel(consumerTag);
            channel.abort();
        } catch (IOException ex) {
        }
    }

    @Override
    public boolean isStarted() {
        return isStarted;
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
        while (running) {
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
            RabbitUtil.declareDurableQueue(channel, QUEUE_NAME);
            channel.basicQos(1);
            System.out.println(OK);
        } catch (IOException ex) {
            System.out.println(ERROR);
            throw new ConnectionException("Server start aborted!");
        }
    }

    private void bindToExchanges() {
        for (String reference : exchanges) {
            try {
                String[] ref = extractReference(reference);
                String exchange = ref[0];
                String rountingKey = ref[1];
                String msg = "[x] Binding queue to " + exchange + " with rounting key '" + rountingKey + "' : ";
                System.out.print(msg);
                try {
                    comm.checkExchangeOrDie(exchange);
                } catch (Exception e) {
                    System.out.println(FAIL);
                    System.out.print("[x] Declaring exchange " + exchange + ": ");
                    RabbitUtil.declareExchange(channel, exchange);
                    System.out.println(OK);
                    System.out.print(msg);
                }
                channel.queueBind(QUEUE_NAME, exchange, rountingKey);
                System.out.println(OK);
            } catch (URISyntaxException ex) {
                System.out.println(FAIL);
            } catch (IOException ex) {
                System.out.println(FAIL);
            }
        }
    }

    private void createExchanges() {
        for (String reference : newExchanges) {
            try {
                String[] ref = extractReference(reference);
                String exchange = ref[0];
                try {
                    comm.checkExchangeOrDie(exchange);
                } catch (Exception e) {
                    System.out.print("[x] Declaring new exchange " + exchange + ": ");
                    RabbitUtil.declareExchange(channel, exchange);
                    System.out.println(OK);
                }                
            } catch (URISyntaxException ex) {
                System.out.println(FAIL);
            } catch (IOException ex) {
                System.out.println(FAIL);
            }
        }
    }

    private QueueingConsumer createConsumer() {

        //evita iniciar dois ou mais consumidores 
        //avoid to start two or more consumers
        checkNotHasConsumerOrDie();

        System.out.print("[x] Creating consumer: ");
        QueueingConsumer consumer = new QueueingConsumer(channel) {
            @Override
            public void handleCancel(String consumerTag) throws IOException {
                System.out.println("[!] Consumer has been cancelled unexpectedly");
                super.handleCancel(consumerTag);
            }

            @Override
            public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
                System.out.println("[!] Consumer has been received shutdown signal");
                super.handleShutdownSignal(consumerTag, sig);
            }
        };
        try {
            consumerTag = channel.basicConsume(QUEUE_NAME, false, consumer);
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

    private void checkHasExchangeOrDie() {
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

                    if (props.getReplyTo() != null) {
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        this.rpcConsumer(message, props);
                    } else {
                        try {
                            //TODO: here, if server shutdown, the message will be lost!
                            if (!this.eventConsumer(message)) {
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

    protected boolean eventConsumer(String message) {
        return listener.incoming(message, null);
    }

    protected void rpcConsumer(String message, final AMQP.BasicProperties props) {
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
                        Logger.getLogger(RabbitServerSocket.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        listener.incoming(message, serverResponse);
    }

    private void sleep() {
        try {
            Thread.sleep(RETRY_TIME * 1000);
        } catch (Exception ex) {
        }
    }

    private String[] extractReference(String url) throws URISyntaxException {
        URI uri = new URI(url);

        String[] ref = new String[2];
        ref[0] = RabbitUtil.getHostAddress(uri, "osiris");
        ref[1] = RabbitUtil.getRoutingKey(uri);

        return ref;
    }
}
