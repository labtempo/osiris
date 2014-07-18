/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.springconsumer.osiris;

import java.util.Date;
import org.springframework.amqp.core.ReceiveAndReplyCallback;

/**
 *
 * @author Felipe
 */
public class Router<R,S> implements ReceiveAndReplyCallback{

    public S make(R packet) {
        RequestBuilder rb = new RequestBuilder();
        
        rb.addPacket((String) packet);
        
        OsirisRequest request = rb.build();
        
        if(request.getMethod().equals("POST")){
            return (S) doPost(request);
        }
        if(request.getMethod().equals("GET")){
            return (S) doGet(request);
        }
        return (S) "";
        
    }
    
    public String doPost(OsirisRequest request){
        System.out.println("POST: "+request.getContent());
        return "";
    }
    public String doGet(OsirisRequest request){
        System.out.println("GET");
        return "retornado em " + (new Date());
    }

    
    @Override
    public Object handle(Object r) {
        return make((R) r);
    }
    
    public S handleMessage(R text){
        return make(text);
    }
}
