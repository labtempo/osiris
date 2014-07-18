/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.remoteconsole;

import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Service;
import br.uff.labtempo.osiris.util.interfaces.Client;
import br.uff.labtempo.osiris.util.interfaces.Storage;
import br.uff.labtempo.osiris.util.interfaces.Teste;
import br.uff.labtempo.osiris.util.logging.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class Console extends Service {

    private final String PROMPT_NAME = "Osiris";
    private final String PROMPT_CLOSURE = ">";
    private Client osiris;
    private Scanner scan;

    public Console() {
        super("Osiris Console");
        this.osiris = osiris;
        this.scan = new Scanner(System.in);
    }

    private void printPrompt() {
        StringBuilder prompt = new StringBuilder(PROMPT_NAME);

        prompt.append(PROMPT_CLOSURE);

        System.out.print(prompt.toString());

    }

    private void printArray(List<String> items, String detail) {
        for (String item : items) {
            if (detail.length() > 0) {
                String format = "\t %-40s %s\n";
                System.out.printf(format, item, detail);
            } else {
                System.out.println(item);
            }
        }
    }

    private void printArray(List<String> items) {
        printArray(items, "");
    }

    private void printItem(String item) {
        List<String> list = new ArrayList<String>();
        list.add(item);
        printArray(list);
    }

    private void printItem(String item, String detail) {
        List<String> list = new ArrayList<String>();
        list.add(item);
        printArray(list, detail);
    }

    private boolean executeCommand(String command) {

        String[] parts = command.split(" ");

        try {

            switch (parseCommand(parts[0])) {
                case CREATE:
                    if (parts[1] != null && parts[1].length() > 0) {
                        Object obj = osiris.createVSensor(parts[1]);
                        //System.out.println(teste.getTeste());
                    }
                    break;
                case FREE:
                    Map<String, List<String>> free = osiris.getFreeItems();

                    for (Entry<String, List<String>> entry : free.entrySet()) {
                        printItem(entry.getKey(), String.valueOf(entry.getValue().size()));
                        printArray(entry.getValue());
                    }

                    break;
                case BOUND:
                    Map<String, List<String>> bound = osiris.getBoundItems();

                    for (Entry<String, List<String>> entry : bound.entrySet()) {
                        printItem(entry.getKey(), String.valueOf(entry.getValue().size()));
                        printArray(entry.getValue());
                    }

                    break;
                case BIND:
                    if ((parts[1] != null && parts[1].length() > 0)
                            && (parts[2] != null && parts[2].length() > 0)) {
                        osiris.bind(parts[1], parts[2]);
                    }
                    break;
                case UNBIND:
                    if (parts[1] != null && parts[1].length() > 0) {
                        osiris.unbind(parts[1]);
                    }
                    break;
                case SAMPLES:
                    if (parts[1] != null && parts[1].length() > 0) {
                        printArray(osiris.getSamples(parts[1]));
                    }
                    break;
                case EXIT:
                    return false;

            }
        } catch (Exception e) {
            Logger.getLogger(Console.class.getName()).log(Level.WARNING, null, e);
        }
        return true;
    }

    private Command parseCommand(String command) {
        return Command.valueOf(command.toUpperCase());
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
    }

    @Override
    protected void onLoop() throws ComponentInitializationException {
        try {

            printPrompt();

            boolean loop = true;
            while (loop && scan.hasNextLine()) {
                String command = scan.nextLine();
                synchronized (this) {
                    loop = executeCommand(command);
                    if (loop) {
                        printPrompt();
                    }
                }
            }
        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }

    }

    @Override
    protected void onStop() {
        Log.D("Closing Console");
        this.scan.close();
    }

    void setClient(Client proxy) {
        this.osiris = proxy;
    }

    private enum Command {

        EXIT, CREATE, FREE, BOUND, BIND, UNBIND, SAMPLES
    }

}
