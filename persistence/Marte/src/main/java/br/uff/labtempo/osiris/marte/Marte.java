/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.marte;

import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.components.conn.Publisher;
import java.util.Scanner;

/**
 *
 * @author Felipe
 */
public class Marte extends Module {

    Publisher publisher;

    public Marte() {
        super("Marte");         
    }

    
    
    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            publisher = new Publisher("marte", "localhost");
            addRequire(publisher);

        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }
    }

    @Override
    protected void onStart() throws ComponentInitializationException {

        Scanner scan = new Scanner(System.in);

        boolean loop = true;
        while (loop && scan.hasNextLine()) {

            String command = scan.nextLine();
            if ("exit".equalsIgnoreCase(command)) {
                break;
            }
            if (publisher.publish(command)) {
                System.out.println("sent: " + command);
            }
        }
        scan.close();    
    }
}
