package br.uff.labtempo.osiris.util.connection.server;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.tools.jsonrpc.JsonRpcServer;

public class JSONRpcServer<T> implements RPCServer {

    private Connection connection;
    private Channel channel;
    private JsonRpcServer server;

    private String host;
    private String exchange;
    private String exchangeType;
    private String queue;
    private String routingKey;
    private Class klass;
    private T instance;

    public JSONRpcServer(String host, String exchange, String exchangeType, String queue, String routingKey, T instance, Class<?> klass) {
        this.host = host;
        this.exchange = exchange;
        this.exchangeType = exchangeType;
        this.queue = queue;
        this.routingKey = routingKey;
        this.instance = instance;
        this.klass = klass;
    }

    @Override
    public void start() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(exchange, exchangeType);
            channel.queueDeclare(queue, false, true, true, null);
            channel.queueBind(queue, exchange, routingKey);
            server = new JsonRpcServer(channel, queue, klass, instance);
            server.mainloop();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void close() throws Exception {
        if (server != null) {
            server.close();
        }
        if (channel != null) {
            channel.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }
}
