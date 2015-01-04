/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet;

import br.uff.labtempo.omcp.common.utils.Serializer;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.omcp.server.OmcpServer;
import br.uff.labtempo.omcp.server.rabbitmq.RabbitServer;
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.controller.ConverterController;
import br.uff.labtempo.osiris.virtualsensornet.controller.DataTypeController;
import br.uff.labtempo.osiris.virtualsensornet.controller.NotifyController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorLinkController;
import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorLink;
import br.uff.labtempo.osiris.virtualsensornet.persistence.jpa.JpaDaoFactory;

/**
 *
 * @author Felipe
 */
public class Bootstrap implements AutoCloseable {

    private final String pwd;
    private final String usr;
    private final String ip;
    private final String moduleName;

    private JpaDaoFactory factory;

    private OmcpServer server;

    public Bootstrap() throws Exception {
        ip = "192.168.0.7";
        usr = "admin";
        pwd = "admin";
        moduleName = "virtualsensornet";

        try {
            server = new RabbitServer(moduleName, ip, usr, pwd);

            factory = JpaDaoFactory.newInstance(ip, usr, pwd, moduleName);

            NotifyController nCtrl = new NotifyController(factory);
            VirtualSensorLinkController lCtrl = new VirtualSensorLinkController(factory);
            DataTypeController dtCtrl = new DataTypeController(factory);
            ConverterController cCtrl = new ConverterController(factory);

            nCtrl.setNext(lCtrl);
            lCtrl.setNext(dtCtrl);
            dtCtrl.setNext(cCtrl);
            
            insertDataType(dtCtrl);
            insertConverter(cCtrl);
            insertLink(lCtrl);

            server.setHandler(nCtrl);

            server.addReference("omcp://collector.messagegroup/");
        } catch (Exception ex) {
            close();
            throw ex;
        }
        //server.addReference("omcp://test.ex/warning/");
    }

    public void start() {
        server.start();
    }

    @Override
    public void close() {
        try {
            server.close();
        } catch (Exception e) {
        }
        try {
            factory.close();
        } catch (Exception e) {
        }

    }

    public void insertDataType(DataTypeController dtCtrl) {
        DataTypeVsnTo d1, d2, d3, d4, d5;

        d1 = new DataTypeVsnTo("temperature", "number", "celsius", "°C");

        d2 = new DataTypeVsnTo("voltage", "number", "volt", "V");

        d3 = new DataTypeVsnTo("luminosity", "number", "candela", "cd");

        d4 = new DataTypeVsnTo("time", "number", "second", "s");

        d5 = new DataTypeVsnTo("temperature", "number", "fahrenheit", "°F");

        dtCtrl.create(d1);
        dtCtrl.create(d2);
        dtCtrl.create(d3);
        dtCtrl.create(d4);
        dtCtrl.create(d5);
    }

    public void insertConverter(ConverterController cCtrl) {
        ConverterVsnTo c1, c2;

        c1 = new ConverterVsnTo("CelsiusToFahrenheit", "value = value * 9 / 5 + 32;", 5);

        c2 = new ConverterVsnTo("MunutesToSeconds", "value = value*60", 4);

        cCtrl.create(c1);
        cCtrl.create(c2);
    }

    public void insertLink(VirtualSensorLinkController lCtrl) {
        LinkVsnTo l1, l2;

        l1 = new LinkVsnTo("1", "labpos", "labtempo");
        l2 = new LinkVsnTo("1", "labpos", "labtempo");

        l1.createField("temperature", 1);
        l1.createField("temperature", 1, 1);        
        l1.createField("luminosity", 3);        
        l1.createField("battery", 2);
        
        l2.createField("temperature", 1);
        l2.createField("temperature", 1, 1);        
        l2.createField("luminosity", 3);        
        l2.createField("battery", 2);
        
        Serializer se = new Serializer();
        
        System.out.println(se.toJson(l1));
        System.out.println(se.toJson(l2));
        
        lCtrl.create(l1);
        lCtrl.create(l2);

    }
}