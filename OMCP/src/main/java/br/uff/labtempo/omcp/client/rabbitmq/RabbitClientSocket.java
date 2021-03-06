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
package br.uff.labtempo.omcp.client.rabbitmq;

import br.uff.labtempo.omcp.common.exceptions.client.BadResponseException;
import br.uff.labtempo.omcp.common.exceptions.client.ConnectionException;
import br.uff.labtempo.omcp.common.exceptions.client.RequestException;
import br.uff.labtempo.omcp.common.exceptions.client.UnreachableModuleException;
import br.uff.labtempo.omcp.common.utils.RabbitComm;
import br.uff.labtempo.omcp.common.utils.RabbitUtil;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
final class RabbitClientSocket {

    private RabbitComm comm;
    private Channel channel;
    private final int TIMEOUT = 30;//SECONDS

    public RabbitClientSocket(String host, String user, String pass) {
        this.comm = new RabbitComm(host, user, user);
    }

    public RabbitClientSocket(String host) {
        this(host, null, null);
    }

    public void connect() throws ConnectionException {
        this.comm.connect();
        this.channel = comm.newChannel();
    }

    public void close() {
        comm.close();
    }

    public boolean isAlive() {
        return comm.isOpen();
    }

    /**
     * This method sends persistent messages (".deliveryMode(2)") to Osiris
     * modules and it contains no response. Method to notification event *
     *
     * @param queueName - Osiris module's queue name
     * @param packet - Osiris packet
     * @throws UnreachableModuleException
     */
    public void publish(String queueName, String packet, String routingKey) throws UnreachableModuleException {
        String queue = "";
        String exchange = "";

        if (queueName.contains("osiris.messagegroup")) {
            this.comm.checkExchangeOrDie(queueName);
            exchange = queueName;
            queue = routingKey;
        } else {
            this.comm.checkQueueOrDie(queueName);
            queue = queueName;
        }

        BasicProperties properties = new BasicProperties.Builder()
                .deliveryMode(2)//persistent mode
                .contentType("text/plain")
                .contentEncoding("UTF-8")
                .build();

        try {
            channel.basicPublish(exchange, queue, properties, packet.getBytes());
        } catch (Exception ex) {
            throw new RequestException("Could not publish", ex);
        }
    }

    /**
     * This method executes rpc calls to Osiris' modules and receives a response
     * packet. Method to RPC call
     *
     * @param queueName - Osiris module's queue name
     * @param packet - Osiris packet
     * @return Response
     * @throws UnreachableModuleException, ResponseTimeoutException,
     * BadResponseException
     *
     */
    public String call(String queueName, String packet) throws UnreachableModuleException, RequestException, BadResponseException {
        this.comm.checkHasConsumersOrDie(queueName);

        String corrId = java.util.UUID.randomUUID().toString();
        QueueingConsumer consumer;
        String basicConsumeName;
        String replyQueueName;
        try {
            replyQueueName = RabbitUtil.declareTemporaryQueue(channel).getQueue();
            consumer = new QueueingConsumer(channel);
            basicConsumeName = channel.basicConsume(replyQueueName, false, consumer);

            BasicProperties properties = new BasicProperties.Builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .deliveryMode(1)//no-persistent
                    .contentType("text/plain")
                    .contentEncoding("UTF-8")
                    .expiration("0")//auto delete itself after this time
                    .build();

            //enable acknowledgements
            channel.confirmSelect();
            channel.basicPublish("", queueName, properties, packet.getBytes());
            try {
                //wait for acknowledgements
                channel.waitForConfirmsOrDie(getTimeout());
            } catch (Exception ex) {// InterruptedException, TimeoutException
                throw new RequestException("Request reached the waiting ack time limit!", ex);
            }
        } catch (RequestException ex) {
            throw ex;
        } catch (Exception ex) {//IOException,
            throw new RequestException("Could not make rpc!", ex);
        }

        return waitForResponse(consumer, corrId, basicConsumeName, replyQueueName);
    }

    //mensagem consumida, esperando resposta: apenas o servidor pode dar timeout neste ponto.
    private String waitForResponse(QueueingConsumer consumer, String corrId, String basicConsumeName, String replyQueueName) throws RequestException {
        String response = null;
        while (true) {
            try {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                    response = new String(delivery.getBody());
                    channel.queueDelete(replyQueueName);//remove temporary queue
                    break;
                }
            } catch (ConsumerCancelledException ex) {
                try {
                    channel.basicCancel(basicConsumeName);
                } catch (Exception e) {
                    int i = 0x0;//do nothing
                }
                throw new RequestException("Response time reached the limit!", ex);
            } catch (Exception ex) {
                throw new RequestException("Response error!", ex);
            }
        }
        return response;
    }

    private long getTimeout() {
        return TIMEOUT * 1000;
    }
}
