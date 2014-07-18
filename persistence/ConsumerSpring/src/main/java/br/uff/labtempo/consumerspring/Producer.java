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
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
 
public class Producer {
 
   @Autowired
   RabbitTemplate rabbitTemplate;
   
  public void sendMessage(String msg)
         throws Exception {
 
      MessageProperties props = new MessageProperties();
 
      Message message = new Message(msg.getBytes(), props);
 
      rabbitTemplate.convertAndSend(message);
   }
 
}
