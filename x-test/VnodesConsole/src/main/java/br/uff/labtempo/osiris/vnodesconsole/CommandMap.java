/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.vnodesconsole;

import br.uff.labtempo.osiris.util.components.Component;
import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Service;
import br.uff.labtempo.osiris.util.interfaces.Network;
import br.uff.labtempo.osiris.util.interfaces.VSensor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class CommandMap {

    private VSensor vnode;
    private Network sensornet;

    public CommandMap(VSensor vnode, Network sensornet) {
        this.vnode = vnode;
        this.sensornet = sensornet;
    }

    public void execute(String command) throws Exception {
        String[] parts = command.split(" ");

        switch (parseCommand(parts[0])) {
            case LS:
                printArray(vnode.getSensors());
                break;
            case BIND:
                if ((parts[1] != null && parts[1].length() > 0)  &&
                        (parts[2] != null && parts[2].length() > 0)) {
                    vnode.bind(parts[1], parts[2]);
                }
                break;
            case NODE:
                if (parts[1] != null && parts[1].length() > 0)
                    System.out.println(sensornet.getNode(parts[1]));
                break;
            case EXIT:
                throw new Exception("Exit");
            default:
        }

    }

    private Command parseCommand(String command) {
        return Command.valueOf(command.toUpperCase());
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

    private enum Command {

        LS, BIND, EXIT, NODE
    }

}
