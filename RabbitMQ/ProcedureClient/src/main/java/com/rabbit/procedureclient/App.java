package com.rabbit.procedureclient;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] argv) {
        RPCClient fibonacciRpc = null;
        String response = null;
        try {
            fibonacciRpc = new RPCClient();

            System.out.println("Sender is working now. Enter 'exit' to close!");

            Scanner scan = new Scanner(System.in);

            while (scan.hasNext()) {
                String message = scan.nextLine();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                } else {
                    try {
                        Integer.parseInt(message);
                        System.out.println(" [x] Requesting fib(" +message+")");
                        response = fibonacciRpc.call(message);
                        System.out.println(" [.] Got '" + response + "'");
                    } catch (Exception e) {
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fibonacciRpc != null) {
                try {
                    fibonacciRpc.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

}
