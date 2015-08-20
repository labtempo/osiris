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
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;
import br.uff.labtempo.tlauncher.data.CollectorDataBuilder;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class FlowRateByRealCollectorTestSuite implements UpdateListener<VirtualSensorVsnTo>, TestSuite {

    private String testName;
    private FilePrinter printer;
    private int totalMessagesToSend;
    private final int multiplier;
    private final long delayLimitInSeconds;
    private long lastDelayInSeconds;
    private final int testLoop;
    private boolean silent;
    private boolean verbose;
    private final int delayBetweenTestsInSeconds;
    private final List<String> linkIds;

    private ResponseAccumulator accumulator;

    public FlowRateByRealCollectorTestSuite(boolean silent, boolean verbose) {
        this.testName = "flow-rate-by-real-collector";
        this.totalMessagesToSend = 10;
        this.multiplier = 10;
        this.delayLimitInSeconds = 60;
        this.testLoop = 100;//100
        this.delayBetweenTestsInSeconds = 0;
        this.silent = silent;
        this.verbose = verbose;
        this.linkIds = new ArrayList<>();
    }

    public FlowRateByRealCollectorTestSuite() {
        this(false, false);
    }

    public FlowRateByRealCollectorTestSuite(boolean verbose) {
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
        OmcpService service;

        if (silent) {
            service = factory.getSilentService();
        } else {
            service = factory.getService();
        }

        CommandString dataBuilder = new CommandString(manager.getProperties(), "..\\ThinCollector\\target\\ThinCollector-1.0-SNAPSHOT-jar-with-dependencies.jar");
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
            
            System.out.println("Iniciando service");
            wrapper.start();
            System.out.println("Enviando dados");

            while (lastDelayInSeconds < delayLimitInSeconds) {
                String name = String.valueOf(totalMessagesToSend);
                File folder = fileManager.createFolder(testFolder, name);
                addNewLinks(factory);
                for (int i = 1; i <= testLoop; i++) {
                    printer = fileManager.getFilePrinter(totalMessagesToSend + "_" + String.format("%03d", i) + ".txt", folder, true);
                    printer.println(VirtualSensorPrintFormat.getHeaders());
                    if (verbose) {
                        System.out.print(totalMessagesToSend + "(x" + i + "): ");
                    }

                    //ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

                    accumulator = new ResponseAccumulator(totalMessagesToSend);
                    //quantidade de mensagens enviadas

                    //List<Callable<Integer>> callables = new ArrayList<>();
                    List<Process> list = new ArrayList<>();
                    for (int j = 1; j <= totalMessagesToSend; j++) {
                        //Proc proc = new Proc(dataBuilder, linkIds.get(j - 1), j);
                        ProcessBuilder builder = dataBuilder.getCommand(linkIds.get(j - 1), j);
                        Process p = builder.start();
                        list.add(p);
                    }
                    for (Process process : list) {
                        process.waitFor();
                    }

//                    List<Future<Integer>> futures = es.invokeAll(callables);
//                    for (Future<Integer> future : futures) {
//                        future.get();
//                    }                    
                    while (!accumulator.isComplete()) {
                        Thread.sleep(200);
                    }
                    accumulator.print(printer);

                    if (verbose) {
                        System.out.println("ok");
                    }

                    Thread.sleep(delayBetweenTestsInSeconds * 1000);

                }                

                totalMessagesToSend += multiplier;

            }
        } catch (Exception ex) {
            Logger.getLogger(FlowRateByRealCollectorTestSuite.class.getName()).log(Level.SEVERE, null, ex);
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
            String id = format.getId();
            if (id != null) {
                accumulator.add(format);
                long delay = format.getEndToEndInSeconds();
                if (delay > lastDelayInSeconds) {
                    lastDelayInSeconds = delay;
                }
            }
        } catch (Exception e) {
        }
    }

    private void addNewLinks(ConnectionFactory factory) throws Exception {
        OmcpClient client = factory.getClient();
        VirtualSensorNetDataBuilder vsnDataBuilder = new VirtualSensorNetDataBuilder(client);
        int total = totalMessagesToSend - linkIds.size();
        System.out.println("Adicionando novos links(" + totalMessagesToSend + " - " + linkIds.size() + " = " + total + ")");
        List<String> newIds = createLinkIds(linkIds.size(), totalMessagesToSend);
        vsnDataBuilder.createLink(testName, newIds);
        linkIds.addAll(newIds);
        client.close();
        Thread.sleep(delayBetweenTestsInSeconds * 1000);
    }

    private List<String> createLinkIds(int total) {
        return createLinkIds(1, total);
    }

    private List<String> createLinkIds(int from, int to) {
        List<String> list = new ArrayList<>();
        for (int i = from + 1; i <= to; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    private class CommandString {

        private final String delimiter = ":";

        private final String credentials;
        private final String preIdentifiers;
        private final String path;

        public CommandString(Properties config, String path) {
            this.credentials = generateCredentials(config);
            this.preIdentifiers = generateIdenrifiers(DataBase.NETWORK_ID, testName);
            this.path = path;
        }

        public ProcessBuilder getCommand(String sensorId, int messageId) {
            return generateCommand(sensorId, String.valueOf(messageId));
        }

        private String generateCredentials(Properties config) {
            String ip = config.getProperty("rabbitmq.server.ip");
            String user = config.getProperty("rabbitmq.user.name");
            String pass = config.getProperty("rabbitmq.user.pass");
            return implode(delimiter, ip, user, pass);

        }

        private ProcessBuilder generateCommand(String sensorId, String messageId) {
            String command = "java";
            String jar = "-jar";
            String identifiers = implode(delimiter, preIdentifiers, sensorId);
            String tuple = implode(delimiter, DataBase.DATA_NAME, messageId, DataBase.DATA_UNIT, DataBase.DATA_SYMBOL);
            ProcessBuilder builder = new ProcessBuilder(command, jar, path, credentials, identifiers, tuple);
            return builder;
        }

        private String generateIdenrifiers(String networkId, String collectorId) {
            return implode(delimiter, networkId, collectorId);
        }

        public String implode(String separator, String... data) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length - 1; i++) {
                if (!data[i].matches(" *")) {
                    sb.append(data[i]);
                    sb.append(separator);
                }
            }
            sb.append(data[data.length - 1].trim());
            return sb.toString();
        }
    }

    private class ResponseAccumulator {

        private Map<Long, VirtualSensorPrintFormat> map;
        private long total;

        public ResponseAccumulator(long total) {
            this.total = total;
            this.map = new HashMap<>();
        }

        public void add(VirtualSensorPrintFormat format) {
            long id = Integer.valueOf(format.getId());
            if (map.containsKey(id)) {
                throw new RuntimeException("duplicated entry!");
            }
            map.put(id, format);
        }

        public boolean isComplete() {
            return map.size() == total;
        }

        public void print(FilePrinter printer) throws IOException, Exception {
            for (Map.Entry<Long, VirtualSensorPrintFormat> entrySet : map.entrySet()) {
                Long key = entrySet.getKey();
                VirtualSensorPrintFormat value = entrySet.getValue();
                printer.println(value);
            }
            printer.close();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<Long, VirtualSensorPrintFormat> entrySet : map.entrySet()) {
                Long key = entrySet.getKey();
                builder.append(key);
                builder.append(" ");
            }
            return builder.toString();
        }

    }

    private class Proc implements Callable<Integer> {

        private CommandString command;
        private final String sensorId;
        private final int messageId;

        public Proc(CommandString command, String sensorId, int messageId) {
            this.command = command;
            this.sensorId = sensorId;
            this.messageId = messageId;
        }

        @Override
        public Integer call() throws Exception {
            Process p;
            try {
                ProcessBuilder builder = command.getCommand(sensorId, messageId);
                p = builder.start();
                p.waitFor();
            } catch (Exception ex) {

            }
            return 1;
        }

        public void setInputStream(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "CP857"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

    }
}
