package br.uff.labtempo.osiris.remotestorage.amqp;

import br.uff.labtempo.osiris.remotestorage.MemoryStorage;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.tools.jsonrpc.JsonRpcServer;

public class JSONRpcServer<T>  implements RPCServer {

    private final String RPC_QUEUE_NAME = "rpc_queue";
    private Connection connection;
    private Channel channel;
    private T instance;
    private Class klass;
    private String host;
    private JsonRpcServer server;

    public JSONRpcServer(T instance, Class<?> klass, String host) {
        this.instance = instance;
        this.klass = klass;
        this.host = host;
    }

    @Override
    public void start() {       
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);

            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME, false, true, true, null);            

            server = new JsonRpcServer(channel, RPC_QUEUE_NAME,klass,instance);

            server.mainloop();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void close() throws Exception {
        server.close();
        channel.close();
        connection.close();
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


