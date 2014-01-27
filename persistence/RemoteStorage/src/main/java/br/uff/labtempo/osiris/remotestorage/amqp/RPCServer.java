package br.uff.labtempo.osiris.remotestorage.amqp;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;

public class RPCServer {

    private final String RPC_QUEUE_NAME = "rpc_queue";
    private Connection connection;
    private OnCallListener listener;

    public void start() {
        connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

            channel.basicQos(1);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

            while (true) {
                String response = null;

                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                BasicProperties props = delivery.getProperties();
                BasicProperties replyProps = new BasicProperties.Builder()
                        .correlationId(props.getCorrelationId())
                        .build();

                String message = new String(delivery.getBody(), "UTF-8");
                response = send(message);

                channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes("UTF-8"));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public void setOnCallListener(OnCallListener listener) {
        this.listener = listener;
    }

    public void close() throws Exception {
        connection.close();
    }

    private String send(String message) {
        if (listener != null) {
            return listener.onCall(message);
        } else {
            return "";
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
