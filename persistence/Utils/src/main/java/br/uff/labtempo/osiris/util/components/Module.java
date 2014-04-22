/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components;

import br.uff.labtempo.osiris.util.logging.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    private List<Component> requires;
    private List<Component> provides;
    private List<Service> requiresServices;
    private List<Service> providesServices;
    private List<Future> runningServices;

    private String name;
    private boolean closing;

    public Module(String name) {
        this.name = name;

        requires = new ArrayList<>();
        provides = new ArrayList<>();
        requiresServices = new ArrayList<>();
        providesServices = new ArrayList<>();
        runningServices = new ArrayList<>();
    }

    /* basic */
    public void addRequire(Component content) {
        requires.add(content);
    }

    public void addProvide(Component content) {
        provides.add(content);
    }

    public void addRequire(Service content) {
        requiresServices.add(content);
    }

    public void addProvide(Service content) {
        providesServices.add(content);
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
        startComponents(requires);
        runningServices.addAll(startServices(requiresServices));
    }

    protected void onProvidedStart() throws ComponentInitializationException {
        startComponents(provides);
        runningServices.addAll(startServices(providesServices));

        for (Future future : runningServices) {
            try {
                future.get();
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
        finishComponents(requiresServices);
        finishComponents(requires);
    }

    protected void onProvidedFinish() {
        finishComponents(providesServices);
        finishComponents(provides);
    }

    private void startComponents(List<Component> list) {
        for (Component component : list) {
            component.start();
        }
    }

    private void finishComponents(List<? extends Component> list) {
        Collections.reverse(list);
        for (Component component : list) {
            component.finish();
        }
    }

    private List<Future> startServices(List<Service> list) {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future> result = new ArrayList<>();
        for (Service service : list) {
            service.start();
            result.add(executor.submit((Service) service));
        }
        return result;
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
                        finish();
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
