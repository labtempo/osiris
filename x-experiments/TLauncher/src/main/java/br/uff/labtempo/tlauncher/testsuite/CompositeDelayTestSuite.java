/*
 * Copyright 2015 Felipe Santos <fralph at ic.uff.br>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.uff.labtempo.tlauncher.testsuite;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.service.OmcpService;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;
import br.uff.labtempo.tlauncher.data.CollectorMultiClientDataBuilder;
import br.uff.labtempo.tlauncher.data.DataBase;
import br.uff.labtempo.tlauncher.data.DataBuilder;
import br.uff.labtempo.tlauncher.data.UpdateListener;
import br.uff.labtempo.tlauncher.data.UpdateServiceWrapper;
import br.uff.labtempo.tlauncher.data.VirtualSensorNetDataBuilder;
import br.uff.labtempo.tlauncher.data.VirtualSensorNetUpdateService;
import br.uff.labtempo.tlauncher.data.VirtualSensorPrintFormat;
import br.uff.labtempo.tlauncher.model.ConnectionFactory;
import br.uff.labtempo.tlauncher.model.ConsoleCommand;
import br.uff.labtempo.tlauncher.model.ModuleManager;
import br.uff.labtempo.tlauncher.persistence.FileManager;
import br.uff.labtempo.tlauncher.persistence.FilePrinter;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class CompositeDelayTestSuite implements UpdateListener<VirtualSensorVsnTo>, TestSuite {

    private String testName;
    private String sensorId;
    private final Object monitor = new Object();
    private boolean waiting;
    private FilePrinter printer;
    private int totalMessagesToSend;
    private final int multiplier;
    private final long delayLimitInSeconds;
    private long lastDelayInSeconds;
    private final int testLoop;
    private boolean silent;
    private boolean verbose;
    private final int delayBetweenTestsInSeconds;

    //composite config
    private List<Long> ids;
    private Set<Long> tempIds;
    private final int totalComposite;

    public CompositeDelayTestSuite(boolean silent, boolean verbose) {
        this.testName = "composite-delay";
        this.sensorId = "1";
        this.totalMessagesToSend = 10;
        this.multiplier = 10;
        this.delayLimitInSeconds = 1;
        this.testLoop = 100;
        this.delayBetweenTestsInSeconds = 2;

        //omcp config
        this.silent = silent;
        this.verbose = verbose;

        //composite config
        this.totalComposite = 100;
    }

    public CompositeDelayTestSuite() {
        this(false, false);
    }

    public CompositeDelayTestSuite(boolean verbose) {
        this(true, verbose);
    }

    @Override
    public void start() {
        //folder and file
        FileManager fileManager = new FileManager();
        //commands
        ConsoleCommand command = new ConsoleCommand("sudo service rabbitmq-server restart");
        ModuleManager manager;
        if (silent) {
            manager = new ModuleManager("..\\..\\VirtualSensorNet\\target\\VirtualSensorNet-1.0-SNAPSHOT-jar-with-dependencies.jar silent");
        } else {
            manager = new ModuleManager("..\\..\\VirtualSensorNet\\target\\VirtualSensorNet-1.0-SNAPSHOT-jar-with-dependencies.jar");
        }

        ConnectionFactory factory = manager.getConfig();
        OmcpClient client = factory.getClient();
        OmcpService service;

        if (silent) {
            service = factory.getSilentService();
        } else {
            service = factory.getService();
        }

        VirtualSensorNetDataBuilder vsnDataBuilder = new VirtualSensorNetDataBuilder(client);
        DataBuilder dataBuilder = new CollectorMultiClientDataBuilder(factory, testName);
        VirtualSensorNetUpdateService updateService = new VirtualSensorNetUpdateService(service, this);
        UpdateServiceWrapper wrapper = new UpdateServiceWrapper(updateService);

        try {
            //create folder
            File mainFolder = fileManager.createFolder(DataBase.RESULT_FOLDER);
            String folderName = fileManager.getTimestampedFileName(testName);
            File testFolder = fileManager.createFolder(mainFolder, folderName, true);

            //commands
            //command.execute();
            manager.start();
            System.out.println("Criando link");
            long linkId = vsnDataBuilder.createLink(testName, sensorId);

            Thread.sleep(1000);
            System.out.println("Criando composite");
            List<Long> sensorIds = vsnDataBuilder.createNestedComposite(linkId, totalComposite);
            sensorIds.add(linkId);

            ids = sensorIds;

            System.out.println("Iniciando service");
            wrapper.start();
            System.out.println("Enviando dados");

            //x vezes cada teste
            for (int i = 1; i <= testLoop; i++) {
                printer = fileManager.getFilePrinter(String.format("%03d", i) + ".txt", testFolder, true);
                printer.println(VirtualSensorPrintFormat.getHeaders());
                if (verbose) {
                    System.out.print(i + "(x" + testLoop + "): ");
                }

                //carrega ids para a comparação
                tempIds = new HashSet<>();
                tempIds.addAll(ids);

                //envia apenas uma mensagem
                dataBuilder.publishSample(sensorId, i);

                waiting = true;
                synchronized (monitor) {
                    while (waiting) {
                        monitor.wait();
                        if (verbose) {
                            System.out.println("ok");
                        }
                        Thread.sleep(delayBetweenTestsInSeconds * 1000);
                    }
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(FlowRateByCollectorTestSuite.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                wrapper.close();
            } catch (Exception e) {
            }
            try {
                printer.close();
            } catch (Exception e) {
            }

            try {
                manager.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void updateReceived(VirtualSensorVsnTo object, long unpackingTimestamp) {
        try {
            VirtualSensorPrintFormat format = new VirtualSensorPrintFormat(object, unpackingTimestamp);
            if (tempIds.contains(object.getId())) {

                tempIds.remove(object.getId());

                printer.println(format);
                int id = Integer.valueOf(format.getId());
                long delay = format.getEndToEndInSeconds();

                if (delay > lastDelayInSeconds) {
                    lastDelayInSeconds = delay;
                }
            }

            if (tempIds.isEmpty()) {
                try {
                    printer.close();
                } catch (Exception ex) {
                    Logger.getLogger(FlowRateByCollectorTestSuite.class.getName()).log(Level.SEVERE, null, ex);
                }

                synchronized (monitor) {
                    waiting = false;
                    monitor.notify();
                }

            }
        } catch (IOException ex) {
        }

    }
}
