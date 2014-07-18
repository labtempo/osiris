/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.consumerspring;

/**
 *
 * @author Felipe
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
 
import com.rabbitmq.client.Channel;
 
public class Consumer implements ChannelAwareMessageListener {
 
   private static Log log = LogFactory.getLog(Consumer.class);
 
   public Consumer() {
 
   }
 
   public void onMessage(Message message, Channel channel) throws Exception {
 
      log.debug("subscription listener message received"+ new String(message.getBody()));
 
      String msg = new String(message.getBody());
 
      channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
 
   }
 
}
