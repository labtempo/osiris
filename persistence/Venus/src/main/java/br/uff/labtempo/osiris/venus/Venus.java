/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.venus;


import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.components.conn.OnMessageListener;
import br.uff.labtempo.osiris.util.components.conn.Publisher;
import br.uff.labtempo.osiris.util.components.conn.Subscriber;
import java.util.Scanner;


/**
 *
 * @author Felipe
 */
public class Venus extends Module implements OnMessageListener{   
    
    public Venus(){
        super("Venus");
    }

    @Override
    protected void beforeBoot() throws ComponentInitializationException {
        try {
            Subscriber sub = new Subscriber(new String[]{"marte","saturno"},"localhost",this);
            addRequire(sub);
            
        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }
        super.beforeBoot(); 
    }

    public void onReceiveMessage(String message, String subject) {
        System.out.println("Received: "+message);
    }
    
}
