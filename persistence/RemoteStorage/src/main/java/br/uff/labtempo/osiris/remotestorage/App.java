package br.uff.labtempo.osiris.remotestorage;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        MemoryStorage storage =  new  MemoryStorage();
        Server server = new Server(storage);
        server.start();
    }
}
