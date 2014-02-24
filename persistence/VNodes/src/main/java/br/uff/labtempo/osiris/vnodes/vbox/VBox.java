/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.vnodes.vbox;

import br.uff.labtempo.osiris.vnodes.conn.RPCConnection;
import br.uff.labtempo.osiris.util.interfaces.Storage;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class VBox {

    private static VBox instance;

    private List<VNode> nodes;
    private Map<Integer, Integer> mapID;
    private Map<String, Integer> mapDataSource;

    private VBox() {
    }

    public static VBox getInstance() {
        if (instance == null) {
            instance = new VBox();
        }
        return instance;
    }

    public void loadDataFromStorage() throws Exception {
        Storage storage = RPCConnection.getInstance().getStorage();
        String schema = System.getProperty("module.data.schema.name");

        try {
            Map<String, String> data = storage.getRepositoryKeysAndEntry(schema);
            if (data == null) {
                throw new Exception("schema unknown");
            }
        } catch (ClassCastException e) {
            System.out.println("rep vazio");
        }

    }

    public void close() {
        instance = null;
    }

    public void createNewStorageDataSchema() throws Exception {
        Storage storage = RPCConnection.getInstance().getStorage();
        String schema = System.getProperty("module.data.schema.name");
        String defItem =  System.getProperty("module.data.default.item");

        boolean isCreated = storage.createRepository(schema);        

        if (!isCreated) {
            throw new Exception("schema could not be created");
        }
        
        storage.addEntry(schema, defItem, "{}");
    }

    public void newMeasure(String message) {
        System.out.println(message);
    }

}
