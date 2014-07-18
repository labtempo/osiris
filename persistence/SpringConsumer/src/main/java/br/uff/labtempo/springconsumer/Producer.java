/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.springconsumer;

/**
 *
 * @author Felipe
 */
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Producer {

    public static void main(String[] args) throws Exception {
        new AnnotationConfigApplicationContext(ProducerConfiguration.class);
    }
}
