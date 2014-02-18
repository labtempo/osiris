package br.uff.labtempo.osiris.virtualstorage;

import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.interfaces.Storage;
import br.uff.labtempo.osiris.util.components.conn.rpc.RemoteListener;



/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {       
        Module virtualStorage = new VirtualStorage();
        virtualStorage.start();
    }
}
