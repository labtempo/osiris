/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components.conn.rpc;

import br.uff.labtempo.osiris.util.components.conn.Config;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 *
 * @author Felipe
 */
public class RemoteCall {

    private static Map<String, RPCClient> table = new HashMap<String, RPCClient>();
    private static Properties prop;

    public static <T> T build(String host, Class<?> klass, String resource) throws Exception {
        if (table.containsKey(resource)) {
            return (T) table.get(resource).createProxy();
        } else {
            RPCClient<T> client = RemoteCall.<T>makeConnection(host, klass, resource);
            table.put(resource, client);
            return (T) client.createProxy();
        }
    }

    public static boolean close(String resource) {
        if (table.containsKey(resource)) {
            table.remove(resource);
            return true;
        }
        return false;
    }

    public static void closeAll() {
        try {
            for (Entry<String, RPCClient> entry : table.entrySet()) {
                entry.getValue().close();
            }
        } catch (Exception e) {
        }
    }

    private static <T> RPCClient makeConnection(String host, Class<?> klass, String resource) throws Exception {
        
        if (prop == null) {
            loadProperties();
        }
        
        String serverQueue = prop.getProperty("amqp.rpc.exchange");
        String routingKey = prop.getProperty("amqp.rpc.routing.key") + resource;
        RPCClient<T> client = new JSONRpcClient<T>(host, serverQueue, routingKey, klass);
        return client;
    }

    private static void loadProperties() throws Exception {
        prop = Config.getProperties();
    }

}
