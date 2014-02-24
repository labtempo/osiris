/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.logging;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 *
 * @author Felipe
 */
public class Log {

    static Logger log;

    static {

        ConsoleAppender console = new ConsoleAppender(); //create appender
        //configure the appender
        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        console.setLayout(new PatternLayout(PATTERN));
        console.setThreshold(Level.DEBUG);
        console.activateOptions();
        //add appender to any Logger (here is root)       
        log = Logger.getRootLogger();
        log.addAppender(console);       
    }

    public static void D(String message) {
        log.debug(message);
    }

    public static void I(String message) {
        log.info(message);
    }

    public static void T(String message) {
        log.trace(message);
    }

    public static void E(String message) {
        log.error(message);
    }

    public static void F(String message) {
        log.fatal(message);
    }
}
