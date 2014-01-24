package com.rabbit.sender;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println("Sender is working now. Enter 'exit' to close!");

        Scanner scan = new Scanner(System.in);
        
        while (scan.hasNext()) {
            String message = scan.nextLine();
            if (message.equalsIgnoreCase("exit")) {
                break;
            } else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
        }

        channel.close();
        connection.close();
    }

}
