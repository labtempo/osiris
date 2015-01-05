/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components.conn;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 *
 * @author Felipe
 */
public class Config {
    public static Properties getProperties() throws Exception {
        Properties prop = new Properties();
        InputStream input = Config.class.getClassLoader().getResourceAsStream("config/connection.xml");
        prop.loadFromXML(input);
        input.close();
        return prop;
    }
}
