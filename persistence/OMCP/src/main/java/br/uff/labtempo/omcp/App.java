package br.uff.labtempo.omcp;

import br.uff.labtempo.omcp.server.RabbitServer;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws Exception {
        RabbitServer server = new RabbitServer();
        server.start();
    }
}

/**
 * Apenas o protocolo Pacotes - request e response Métodos - get, put, post,
 * delete e notify
 *
 */
