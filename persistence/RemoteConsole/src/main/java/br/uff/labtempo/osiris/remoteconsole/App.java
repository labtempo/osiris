package br.uff.labtempo.osiris.remoteconsole;

import br.uff.labtempo.osiris.remoteconsole.storageapi.Storage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        try {
            Storage storage = new Storage();
            Console console = new Console(storage);
            console.start();
            storage.close();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
