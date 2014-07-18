/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.springconsumer;

import br.uff.labtempo.springconsumer.osiris.Router;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.ReceiveAndReplyCallback;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Felipe
 */
@Configuration
public class HelloWorldConfiguration {

    protected final String helloWorldQueueName = "hello.world.queue";


  
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("192.168.0.7");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        //The routing key is set to the name of the queue by the broker for the default exchange.
        template.setRoutingKey(this.helloWorldQueueName);
        //Where we will synchronously receive messages from
        template.setQueue(this.helloWorldQueueName);
        //template.receiveAndReply(this.<String,String>receiveAndReply());
        return template;
    }
    
    @Bean
    public <R,S> ReceiveAndReplyCallback<R,S> receiveAndReply() {
        return new Router<R,S>();
    }

    @Bean
    // Every queue is bound to the default direct exchange
    public Queue helloWorldQueue() {
        return new Queue(this.helloWorldQueueName);
    }

    /*
     @Bean
     public Binding binding() {
     return declare(new Binding(helloWorldQueue(), defaultDirectExchange()));
     }*/

    /*
     @Bean
     public TopicExchange helloExchange() {
     return declare(new TopicExchange("hello.world.exchange"));
     }*/

    /*
     public Queue declareUniqueQueue(String namePrefix) {
     Queue queue = new Queue(namePrefix + "-" + UUID.randomUUID());
     rabbitAdminTemplate().declareQueue(queue);
     return queue;
     }
     // if the default exchange isn't configured to your liking....
     @Bean Binding declareP2PBinding(Queue queue, DirectExchange exchange) {
     return declare(new Binding(queue, exchange, queue.getName()));
     }
     @Bean Binding declarePubSubBinding(String queuePrefix, FanoutExchange exchange) {
     return declare(new Binding(declareUniqueQueue(queuePrefix), exchange));
     }
     @Bean Binding declarePubSubBinding(UniqueQueue uniqueQueue, TopicExchange exchange) {
     return declare(new Binding(uniqueQueue, exchange));
     }
     @Bean Binding declarePubSubBinding(String queuePrefix, TopicExchange exchange, String routingKey) {
     return declare(new Binding(declareUniqueQueue(queuePrefix), exchange, routingKey));
     }*/
}
