package br.uff.labtempo.osiris.storage;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void main(String[] args) {
        Storage storage = new Storage();
        
        Console console = new Console(storage);
        
        console.start();
    }
}
