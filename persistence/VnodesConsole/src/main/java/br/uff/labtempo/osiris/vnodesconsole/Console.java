/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.vnodesconsole;

import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.components.conn.JSONRpcClient;
import br.uff.labtempo.osiris.util.interfaces.Network;
import br.uff.labtempo.osiris.util.interfaces.Storage;
import br.uff.labtempo.osiris.util.interfaces.VSensor;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class Console extends Module {

    private CommandMap map;
    private JSONRpcClient vnodes;
    private JSONRpcClient sensornet;

    public Console() {
        super("VNodes Console");

    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            vnodes = new JSONRpcClient("vnodes", "localhost", VSensor.class);
            sensornet = new JSONRpcClient("sensornet", "localhost", Network.class); 
            addRequire(vnodes);
            addRequire(sensornet);
        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }
    }

    @Override
    public void onStart() throws ComponentInitializationException {
        
        map = new CommandMap(vnodes.<VSensor>getProxy(),sensornet.<Network>getProxy());
        
        System.out.println("Enter the command:");
        Scanner scan = new Scanner(System.in);

        while (scan.hasNext()) {
            String command = scan.nextLine();
            try {
                map.execute(command);
            } catch (Exception ex) {
                Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
        scan.close();
    }

}
