package br.uff.labtempo.osiris.remoteconsole;

import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.components.conn.rpc.RemoteCall;
import br.uff.labtempo.osiris.util.interfaces.Client;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        Module app= new Application();
        app.start();
    }
}
