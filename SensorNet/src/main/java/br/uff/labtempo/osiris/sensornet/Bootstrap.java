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
package br.uff.labtempo.osiris.sensornet;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.omcp.server.OmcpServer;
import br.uff.labtempo.omcp.server.rabbitmq.RabbitServer;
import br.uff.labtempo.osiris.sensornet.controller.AnnouncementController;
import br.uff.labtempo.osiris.sensornet.controller.CollectorController;
import br.uff.labtempo.osiris.sensornet.controller.NetworkController;
import br.uff.labtempo.osiris.sensornet.controller.NotifyController;
import br.uff.labtempo.osiris.sensornet.controller.SchedulerController;
import br.uff.labtempo.osiris.sensornet.controller.SensorController;
import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistence;
import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistenceCommitBySecond;
import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistenceAutoCommitted;
import br.uff.labtempo.osiris.utils.requestpool.RequestPool;
import br.uff.labtempo.osiris.sensornet.persistence.jpa.JpaDaoFactory;
import br.uff.labtempo.osiris.sensornet.thirdparty.announcer.AnnouncementBootstrap;
import br.uff.labtempo.osiris.sensornet.thirdparty.scheduler.SchedulerBootstrap;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Bootstrap implements AutoCloseable {

    private JpaDaoFactory factory;
    private BatchPersistence persistence;
    private RequestPool requestPool;
    private OmcpServer omcpServer;
    private final OmcpClient omcpClient;
    private SchedulerBootstrap schedulerBootstrap;
    private final AnnouncementBootstrap announcementBootstrap;

    public Bootstrap(Properties properties) throws Exception {
        String ip = properties.getProperty("rabbitmq.server.ip");
        String user = properties.getProperty("rabbitmq.user.name");
        String pass = properties.getProperty("rabbitmq.user.pass");

        //virtualsensornet
        String moduleName = Path.NAMING_MODULE_SENSORNET.toString();

        String persistenceUnitName = "postgres";

        try {
            Properties persistenceProperties = overrideProperties(properties);
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName, persistenceProperties);
            factory = JpaDaoFactory.newInstance(emf);
            persistence = new BatchPersistenceAutoCommitted(emf.createEntityManager());
            requestPool = new RequestPool();
            
            NotifyController notifyController = new NotifyController(factory,requestPool);
            SensorController sensorController = new SensorController(factory);
            NetworkController networkController = new NetworkController(factory);
            CollectorController collectorController = new CollectorController(factory);
            
            SchedulerController schedulerController = new SchedulerController(factory);
            schedulerBootstrap = new SchedulerBootstrap(factory.getSchedulerDao(), schedulerController);
            
            omcpClient = new OmcpClientBuilder().host(ip).user(user, pass).source(moduleName).build();
            announcementBootstrap = new AnnouncementBootstrap(omcpClient);
            AnnouncementController announcementController = new AnnouncementController(announcementBootstrap.getAnnouncer());
            
            schedulerController.setAnnouncerAgent(announcementController);
            notifyController.setAnnouncerAgent(announcementController);
            notifyController.setSchedulerAgent(schedulerBootstrap.getScheduler());
            
            notifyController.setNext(sensorController);
            sensorController.setNext(networkController);
            networkController.setNext(collectorController);
            
            omcpServer = new RabbitServer(moduleName, ip, user, pass);
            omcpServer.setHandler(notifyController);

            omcpServer.addReference(Path.MESSAGEGROUP_COLLECTOR_ALL.toString());
        } catch (Exception ex) {
            close();
            throw ex;
        }
    }

    public void start() {
        try {
            requestPool.start();
            announcementBootstrap.start();
            schedulerBootstrap.start();
            omcpServer.start();
        } catch (Exception ex) {
            close();
            throw ex;
        }
    }

    @Override
    public void close() {
        try {
            omcpServer.close();
        } catch (Exception e) {
        }
        try {
            schedulerBootstrap.close();
        } catch (Exception e) {
        }
        try {
            announcementBootstrap.close();
        } catch (Exception e) {
        }
        try {
            omcpClient.close();
        } catch (Exception e) {
        }
        try {
            factory.close();
        } catch (Exception e) {
        }
        try {
            requestPool.close();
        } catch (Exception e) {
        }
    }

    private Properties overrideProperties(Properties properties) {
        Properties persistenceProperties = new Properties();

        String ip = properties.getProperty("postgres.server.ip");
        String port = properties.getProperty("postgres.server.port");
        String user = properties.getProperty("postgres.user.name");
        String pass = properties.getProperty("postgres.user.pass");
        String db = properties.getProperty("postgres.server.db");

        persistenceProperties.setProperty("javax.persistence.jdbc.url", "jdbc:postgresql://" + ip + ":" + port + "/" + db);
        persistenceProperties.setProperty("javax.persistence.jdbc.user", user);
        persistenceProperties.setProperty("javax.persistence.jdbc.password", pass);

        return persistenceProperties;
    }
    
    
}
