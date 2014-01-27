package br.uff.labtempo.osiris.remotestorage;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Storage storage =  new  Storage();
        Server server = new Server(storage);
        server.start();
    }
}
