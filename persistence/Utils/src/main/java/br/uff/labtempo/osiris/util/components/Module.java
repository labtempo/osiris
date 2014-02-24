/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components;

import br.uff.labtempo.osiris.util.logging.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public abstract class Module extends Component {

    private Map<Class<?>, Component> requires;
    private Map<Class<?>, Component> provides;
    private List<Future> services;
    private String name;
    private boolean closing;

    public Module(String name) {
        this.name = name;
        requires = new HashMap<Class<?>, Component>();
        provides = new HashMap<Class<?>, Component>();
        services = new ArrayList<Future>();
    }

    /* basic */
    public void addRequire(Component content) {
        requires.put(Component.class, content);
    }

    public void addProvide(Component content) {
        provides.put(Component.class, content);
    }

    public void addRequire(Service content) {
        requires.put(Service.class, content);
    }

    public void addProvide(Service content) {
        provides.put(Service.class, content);
    }

    @Override
    public void start() throws ComponentInitializationException {
        Log.D("Component is initializing...");
        try {
            onCreate();
            onRequiredStart();
            onStart();
            onProvidedStart();
        } catch (Exception e) {
            Log.D("Initialization aborted!");
            closing = true;
            finish();
            Logger.getLogger(Module.class.getName()).log(Level.SEVERE, null, e);            
            throw new ComponentInitializationException(e);            
        }
    }

    @Override
    public void finish() {
        Log.D("Finishing process started");
        onPause();        
        onProvidedFinish();
        onStop();        
        onRequiredFinish();
        onDestroy();
        Log.D("Finishing process is complete");
    }

    protected void onRequiredStart() throws ComponentInitializationException {
        shutdownHook();
        ExecutorService executor = Executors.newCachedThreadPool();
        for (Entry<Class<?>, Component> entry : requires.entrySet()) {

            Component component = entry.getValue();
            component.start();

            if (entry.getKey().equals(Service.class)) {
                services.add(executor.submit((Service) component));
            }
        }
    }

    protected void onProvidedStart() throws ComponentInitializationException {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (Entry<Class<?>, Component> entry : provides.entrySet()) {
            Component component = entry.getValue();
            component.start();

            if (entry.getKey().equals(Service.class)) {
                services.add(executor.submit((Service) component));
            }
        }

        for (Future f : services) {
            try {
                f.get();
            } catch (InterruptedException ex) {
                Logger.getLogger(Module.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(Module.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!closing) {
            closing = true;
            System.exit(0);
        }
    }

    protected void onRequiredFinish() {
        for (Entry<Class<?>, Component> entry : requires.entrySet()) {
            Component component = entry.getValue();
            component.finish();
        }
    }

    protected void onProvidedFinish() {
        for (Entry<Class<?>, Component> entry : provides.entrySet()) {
            Component component = entry.getValue();
            component.finish();
        }
    }

    private void autoclose() {
        Log.D("Autoclose activated!");
        (new Thread() {
            public void run() {
                try {
                    Thread.sleep(6000);
                    System.exit(0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Module.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    private void shutdownHook() {
        System.out.println("Control + C to terminate " + name);
        final Thread thread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Log.D("ShutdownHook is running...");
                thread.setName("Shutdown hook");
                try {
                    if (!closing) {
                        closing = true;
                        thread.join();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Module.class.getName()).log(Level.SEVERE, null, ex);
                }
                Log.D("ShutdownHook end");
            }
        });
    }

}
