package br.uff.labtempo.osiris.datastorage;

import br.uff.labtempo.osiris.util.connection.server.RemoteListener;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        MemoryStorage storage =  new  MemoryStorage();
        RemoteListener<Storage> server = new RemoteListener<Storage>("localhost", "storage", storage, Storage.class);
        server.start();
    }
}
