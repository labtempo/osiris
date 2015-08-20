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

    private ResponseAccumulator accumulator;

    public FlowRateByRealCollectorTestSuite(boolean silent, boolean verbose) {
        this.testName = "flow-rate-by-real-collector";
        this.sensorId = "1";
        this.totalMessagesToSend = 10;
        this.multiplier = 10;
        this.delayLimitInSeconds = 1;
        this.testLoop = 100;
        this.delayBetweenTestsInSeconds = 2;
        this.silent = silent;
        this.verbose = verbose;
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
        OmcpClient client = factory.getClient();
        OmcpService service;

        if (silent) {
            service = factory.getSilentService();
        } else {
            service = factory.getService();
        }

        VirtualSensorNetDataBuilder vsnDataBuilder = new VirtualSensorNetDataBuilder(client);
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
            System.out.println("Criando link");
            vsnDataBuilder.createLink(testName, sensorId);
            System.out.println("Iniciando service");
            wrapper.start();
            System.out.println("Enviando dados");

            while (lastDelayInSeconds < delayLimitInSeconds) {
                String name = String.valueOf(totalMessagesToSend);
                File folder = fileManager.createFolder(testFolder, name);
                List<Process> processes = new ArrayList<>();

                for (int i = 1; i <= testLoop; i++) {
                    printer = fileManager.getFilePrinter(totalMessagesToSend + "_" + String.format("%03d", i) + ".txt", folder, true);
                    printer.println(VirtualSensorPrintFormat.getHeaders());
                    if (verbose) {
                        System.out.print(totalMessagesToSend + "(x" + i + "): ");
                    }

                    ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

                    accumulator = new ResponseAccumulator(totalMessagesToSend);
                    //quantidade de mensagens enviadas

                    List<Callable<Integer>> callables = new ArrayList<>();
                    for (int j = 1; j <= totalMessagesToSend; j++) {
                        ProcessBuilder builder = dataBuilder.getCommand(j);
                        Proc proc = new Proc(builder);
                        callables.add(proc);
                    }

                    List<Future<Integer>> futures = es.invokeAll(callables);
                    for (Future<Integer> future : futures) {
                        future.get();
                    }

                    while (!accumulator.isComplete()) {
                        Thread.sleep(200);
                        System.out.println(accumulator);
                    }
                    accumulator.print(printer);

                    if (verbose) {
                        System.out.println("ok");
                    }

                }
                if (totalMessagesToSend == 100) {
                    totalMessagesToSend = 300;
                    continue;
                }

                if (totalMessagesToSend == 300) {
                    totalMessagesToSend = 400;
                    continue;
                }

                if (totalMessagesToSend == 400) {
                    totalMessagesToSend = 100;
                }

                totalMessagesToSend *= multiplier;

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
            System.out.println("received:" +format.getId());
            accumulator.add(format);
            long delay = format.getEndToEndInSeconds();
            if (delay > lastDelayInSeconds) {
                lastDelayInSeconds = delay;
            }
        } catch (Exception e) {
        }
    }

    private boolean isAlive(Process process) {
        if (process == null) {
            return false;
        }
        try {
            process.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private class CommandString {

        private final String delimiter = ":";

        private final String credentials;
        private final String identifiers;
        private final String path;

        public CommandString(Properties config, String path) {
            this.credentials = generateCredentials(config);
            this.identifiers = generateIdenrifiers(DataBase.NETWORK_ID, testName, sensorId);
            this.path = path;
        }

        public ProcessBuilder getCommand(int messageId) {

            return generateCommand(String.valueOf(messageId));
        }

        private String generateCredentials(Properties config) {
            String ip = config.getProperty("rabbitmq.server.ip");
            String user = config.getProperty("rabbitmq.user.name");
            String pass = config.getProperty("rabbitmq.user.pass");
            return implode(delimiter, ip, user, pass);

        }

        private ProcessBuilder generateCommand(String messageId) {
            String command = "java";
            String jar = "-jar";
            String tuple = implode(delimiter, DataBase.DATA_NAME, messageId, DataBase.DATA_UNIT, DataBase.DATA_SYMBOL);
            ProcessBuilder builder = new ProcessBuilder(command, jar, path, credentials, identifiers, tuple);
            return builder;
        }

        private String generateIdenrifiers(String networkId, String collectorId, String sensorId) {
            return implode(delimiter, networkId, collectorId, sensorId);
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

        private ProcessBuilder builder;

        public Proc(ProcessBuilder builder) {
            this.builder = builder;
        }

        @Override
        public Integer call() throws Exception {
            Process p;
            try {
                p = builder.start();
                setInputStream(p.getInputStream());
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
