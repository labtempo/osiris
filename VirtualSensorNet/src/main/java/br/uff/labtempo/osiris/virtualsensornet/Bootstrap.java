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
package br.uff.labtempo.osiris.virtualsensornet;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.omcp.server.OmcpServer;
import br.uff.labtempo.omcp.server.rabbitmq.RabbitServer;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.virtualsensornet.controller.AnnouncementController;
import br.uff.labtempo.osiris.virtualsensornet.controller.ConverterController;
import br.uff.labtempo.osiris.virtualsensornet.controller.DataTypeController;
import br.uff.labtempo.osiris.virtualsensornet.controller.NotifyController;
import br.uff.labtempo.osiris.virtualsensornet.controller.SchedulerController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorLinkController;
import br.uff.labtempo.osiris.virtualsensornet.persistence.jpa.JpaDaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer.AnnouncementBootstrap;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.scheduler.SchedulerBootstrap;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Bootstrap implements AutoCloseable {

    private JpaDaoFactory factory;
    private OmcpServer omcpServer;
    private final OmcpClient omcpClient;
    private final SchedulerBootstrap schedulerBootstrap;
    private final AnnouncementBootstrap announcementBootstrap;

    public Bootstrap(Properties properties) throws Exception {

        String ip = properties.getProperty("rabbitmq.server.ip");
        String user = properties.getProperty("rabbitmq.user.name");
        String pass = properties.getProperty("rabbitmq.user.pass");

        //virtualsensornet
        String moduleName = Path.NAMING_MODULE_VIRTUALSENSORNET.toString();

        String persistenceUnitName = "postgres";

        try {
            Properties persistenceProperties = overrideProperties(properties);
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName, persistenceProperties);
            factory = JpaDaoFactory.newInstance(emf);

            NotifyController nc = new NotifyController(factory);
            VirtualSensorController vsc = new VirtualSensorController(factory);
            VirtualSensorLinkController vslc = new VirtualSensorLinkController(factory);
            DataTypeController dtc = new DataTypeController(factory);
            ConverterController cc = new ConverterController(factory);

            SchedulerController schedulerController = new SchedulerController(factory);
            schedulerBootstrap = new SchedulerBootstrap(factory.getSchedulerDao(), schedulerController);

            omcpClient = new OmcpClientBuilder().host(ip).user(user, pass).source(moduleName).build();
            announcementBootstrap = new AnnouncementBootstrap(omcpClient);
            AnnouncementController announcementController = new AnnouncementController(announcementBootstrap.getAnnouncer());

            schedulerController.setAnnouncerAgent(announcementController);
            nc.setAnnouncerAgent(announcementController);
            nc.setSchedulerAgent(schedulerBootstrap.getScheduler());
            vslc.setAnnouncerAgent(announcementController);

            nc.setNext(vsc);
            vsc.setNext(vslc);
            vslc.setNext(dtc);
            dtc.setNext(cc);

            omcpServer = new RabbitServer(moduleName, ip, user, pass);
            omcpServer.setHandler(nc);

            // omcp://collector.messagegroup/#
            omcpServer.addReference(Path.MESSAGEGROUP_COLLECTOR_ALL.toString());
            // omcp://update.messagegroup/sensornet/#
            omcpServer.addReference(Path.MESSAGEGROUP_UPDATE + "sensornet/#");

        } catch (Exception ex) {
            close();
            throw ex;
        }
    }

    public void start() {
        try {
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
