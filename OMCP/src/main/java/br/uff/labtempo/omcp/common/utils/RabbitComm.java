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
package br.uff.labtempo.omcp.common.utils;

import br.uff.labtempo.omcp.common.exceptions.client.ConnectionException;
import br.uff.labtempo.omcp.common.exceptions.client.UnreachableModuleException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.net.InetAddress;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RabbitComm {

    private Connection connection;
    private final String host;
    private final String user;
    private final String pass;

    public RabbitComm(String host, String user, String pass) {
        this.host = host;
        this.user = user;
        this.pass = pass;
    }

    public RabbitComm(String host) {
        this(host, null, null);
    }

    public void connect() throws ConnectionException {
        if (connection == null || !connection.isOpen()) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(this.host);

            if (this.user != null && this.pass != null) {
                factory.setUsername(this.user);
                factory.setPassword(this.pass);
            }

            try {
                connection = factory.newConnection();
            } catch (Exception ex) {//IOException
                close();
                this.checkHostOrDie(host);
                throw new ConnectionException("Could not connect to RabbitMQ", ex);
            }
        } else {
            throw new ConnectionException("Already connected to RabbitMQ");
        }
    }

    public boolean isOpen() {
        return connection.isOpen();
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception ex) {
            }
        }
    }

    public Channel newChannel() {
        try {
            return connection.createChannel();
        } catch (Exception ex) {
            throw new ConnectionException("Could not create new channel!");
        }
    }

    public void checkQueueOrDie(String queueName) throws UnreachableModuleException {
        if (!connection.isOpen()) {
            throw new UnreachableModuleException("Not connected to broker!");
        }
        try {
            Channel ch = connection.createChannel();
            AMQP.Queue.DeclareOk declare = ch.queueDeclarePassive(queueName);
            ch.close();
        } catch (Exception e) {
            throw new UnreachableModuleException("Queue not exists!");
        }
    }

    public void checkExchangeOrDie(String exchangeName) throws UnreachableModuleException {
        if (!connection.isOpen()) {
            throw new UnreachableModuleException("Not connected to broker!");
        }
        try {
            Channel ch = connection.createChannel();
            AMQP.Exchange.DeclareOk declare = ch.exchangeDeclarePassive(exchangeName);
            ch.close();
        } catch (Exception e) {
            throw new UnreachableModuleException("Queue not exists!");
        }
    }

    public void checkHasConsumersOrDie(String queueName) throws UnreachableModuleException {
        if (!connection.isOpen()) {
            throw new UnreachableModuleException("Not connected to broker!");
        }
        try {
            Channel ch = connection.createChannel();
            AMQP.Queue.DeclareOk declare = ch.queueDeclarePassive(queueName);
            ch.close();
            if (declare.getConsumerCount() <= 0) {
                throw new UnreachableModuleException("Module is not online");
            }
        } catch (UnreachableModuleException e) {
            throw e;
        } catch (Exception e) {
            throw new UnreachableModuleException("Queue not exists!");
        }
    }

    public void checkNotHasConsumersOrDie(String queueName) throws UnreachableModuleException {
        if (!connection.isOpen()) {
            throw new UnreachableModuleException("Not connected to broker!");
        }
        try {
            Channel ch = connection.createChannel();
            AMQP.Queue.DeclareOk declare = ch.queueDeclarePassive(queueName);            
            int consumers = declare.getConsumerCount();
            ch.close();
            if (consumers > 0) {
                throw new UnreachableModuleException("Has " + consumers + " consumers!");
            }
        } catch (UnreachableModuleException e) {
            throw e;
        } catch (Exception e) {
            throw new UnreachableModuleException("Queue not exists!");
        }
    }

    public void checkHasOneConsumerOrDie(String queueName) throws UnreachableModuleException {
        if (!connection.isOpen()) {
            throw new UnreachableModuleException("Not connected to broker!");
        }
        try {
            Channel ch = connection.createChannel();
            AMQP.Queue.DeclareOk declare = ch.queueDeclarePassive(queueName);            
            int consumers = declare.getConsumerCount();
            ch.close();
            if (consumers != 1) {
                throw new UnreachableModuleException("Has " + consumers + " consumers!");
            }
        } catch (UnreachableModuleException e) {
            throw e;
        } catch (Exception e) {
            throw new UnreachableModuleException("Queue not exists!");
        }
    }

    public int countMessagesFromQueue(String queueName) throws UnreachableModuleException {
        try {
            Channel ch = connection.createChannel();
            AMQP.Queue.DeclareOk declare = ch.queueDeclarePassive(queueName);
            ch.close();
            return declare.getMessageCount();
        } catch (Exception e) {
            throw new UnreachableModuleException("Queue not exists!");
        }
    }

    public void purgeQueueOrDie(String queueName) throws UnreachableModuleException {
        try {
            Channel ch = connection.createChannel();
            ch.queuePurge(queueName);
            ch.close();
        } catch (Exception e) {
            throw new UnreachableModuleException("Could not purge queue!");
        }
    }

    private void checkHostOrDie(String host) throws ConnectionException {
        boolean live = false;
        try {
            //ping to host machine
            live = InetAddress.getByName(host).isReachable(2000);
        } catch (Exception ex) {
            live = false;
        }
        if (!live) {
            throw new ConnectionException("RabbitMQ server machine is offline!");
        }
    }
}
