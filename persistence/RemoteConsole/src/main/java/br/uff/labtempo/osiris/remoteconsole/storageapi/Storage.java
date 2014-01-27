/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.remoteconsole.storageapi;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class Storage {

    private final String RESOURCE_DELIMITER = "?";
    private final String PARAM_DELIMITER = "&";
    private final String ARRAY_DELIMITER = ";";
    private final String NULLED_RETURN = "<<null>>";

    private RPCClient connection;

    public Storage() throws Exception {
        connection = new RPCClient();
    }

    public void createRepository(String repository) {
        call("createRepository", repository);
    }

    public String[] listRepositories() {
        String msg = call("listRepositories");
        if (msg != null && msg.length() > 0) {
            return msg.split(ARRAY_DELIMITER);
        }
        return null;
    }

    //need fix the nulled retun
    public boolean hasRepository(String repository) {
        String msg = call("hasRepository", repository);
        if (msg != null) {
            return Boolean.parseBoolean(msg);
        }
        return false;
    }

    public String getRepository(String repository) {
        String msg = call("getRepository", repository);
        if (msg != null) {
            return msg;
        }
        return null;
    }

    public boolean removeRepository(String repository) {
        String msg = call("removeRepository", repository);
        if (msg != null) {
            return Boolean.parseBoolean(msg);
        }
        return false;
    }

    public String[] listRepositoryKeys(String repository) {
        String msg = call("listRepositoryKeys", repository);
        if (msg != null  && msg.length() > 0) {
            return msg.split(ARRAY_DELIMITER );
        }
        return null;
    }

    public void addEntry(String repository, String key, String value) {
        call("addEntry", repository, key, value);
    }

    public String getEntryContent(String repository, String key) {
        String msg = call("getEntryContent", repository, key);
        if (msg != null) {
            return msg;
        }
        return null;
    }

    public boolean removeEntry(String repository, String key) {
        String msg = call("removeEntry", repository, key);
        if (msg != null) {
            return Boolean.parseBoolean(msg);
        }
        return false;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private String call(String methodName, String... params) {
        StringBuilder message = new StringBuilder(methodName);
        message.append(RESOURCE_DELIMITER);

        for (String param : params) {
            message.append(param);
            message.append(PARAM_DELIMITER);
        }

        try {
            String result = connection.call(message.toString());
            if (result.contains(NULLED_RETURN)) {
                return null;
            }
            return result;
        } catch (Exception ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
