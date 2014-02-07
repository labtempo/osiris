package br.uff.labtempo.osiris.remoteconsole;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        try {
            Client client = new Client();
            Console console = new Console(client.getStorageInstance());
            console.start();
            client.close();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
