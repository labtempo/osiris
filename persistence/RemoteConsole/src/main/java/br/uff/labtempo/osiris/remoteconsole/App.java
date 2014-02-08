package br.uff.labtempo.osiris.remoteconsole;

import br.uff.labtempo.osiris.util.connection.client.RemoteCall;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        try {
            Console console;
            Storage storage = null;
            int count = 0;
            while (count < 5) {
                try {
                    System.out.println("Tentando conectar para o servidor");
                    storage = RemoteCall.build("localhost", Storage.class, "storage");
                } catch (Exception e) {
                    System.out.println("Servidor indisponível");
                    System.out.println("Reconexão em 10 segundos...");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                    }
                    count++;
                    continue;
                }
                count = 0;
                System.out.println("Conectado");
                break;
            }

            if (count < 5) {
                console = new Console(storage);
                console.start();
            }
            RemoteCall.closeAll();
            
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
