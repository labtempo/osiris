/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public abstract class Module extends Component {

    private List<Component> requires;
    private List<Component> provides;
    private List<Thread> services;
    private String name;
    private boolean close;

    public Module(String name) {
        this.name = name;
        requires = new ArrayList<Component>();
        provides = new ArrayList<Component>();
        services = new ArrayList<Thread>();
    }

    /* basic */
    public void addRequire(Component content) {
        requires.add(content);
    }

    public void addProvide(Component content) {
        provides.add(content);
    }

    public void addRequire(Service content) {
        requires.add((Component) content);
        services.add(content.getThread());
    }

    public void addProvide(Service content) {
        provides.add((Component) content);
        services.add(content.getThread());
    }

    @Override
    protected void beforeBoot() throws ComponentInitializationException {
        System.out.println("Control + C para terminar o " + name);
        final Thread thread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (close = !close) {
                    close();
                    try {
                        thread.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Module.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        for (Component component : requires) {
            component.start();
        }
    }

    @Override
    protected void afterBoot() throws ComponentInitializationException {
        for (Component component : provides) {
            component.start();
        }

        for (Thread thread : services) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
        System.out.println(name + " desligado!");
        
        if ((close = !close)) {
            close();
        }

    }

    @Override
    protected void beforeShutdown() {
        for (Component widget : requires) {
            widget.close();
        }
    }

    @Override
    protected void afterShutdown() {
        for (Component widget : provides) {
            widget.close();
        }
    }

    private void autoclose() {
        (new Thread() {
            public void run() {
                System.out.println("autoclose on!");
                try {
                    sleep(6000);
                    System.exit(-1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Module.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
}
