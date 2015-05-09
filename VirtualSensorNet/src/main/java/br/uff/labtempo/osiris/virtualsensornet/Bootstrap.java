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
import br.uff.labtempo.osiris.utils.requestpool.RequestPool;
import br.uff.labtempo.osiris.virtualsensornet.controller.internal.AnnouncementController;
import br.uff.labtempo.osiris.virtualsensornet.controller.ConverterController;
import br.uff.labtempo.osiris.virtualsensornet.controller.DataTypeController;
import br.uff.labtempo.osiris.virtualsensornet.controller.FunctionController;
import br.uff.labtempo.osiris.virtualsensornet.controller.NotifyController;
import br.uff.labtempo.osiris.virtualsensornet.controller.RevisionController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorBlendingController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorCompositeController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorLinkController;
import br.uff.labtempo.osiris.virtualsensornet.controller.internal.AggregatesCheckerController;
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
    private final OmcpClient omcpClientAnnouncer;
    private final OmcpClient omcpClientBlending;
    private final SchedulerBootstrap schedulerBootstrap;
    private final AnnouncementBootstrap announcementBootstrap;
    private final AggregatesCheckerController checkerController;
    private RequestPool requestPool;

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
            requestPool = new RequestPool();
            checkerController = new AggregatesCheckerController(factory);

            //external controllers
            NotifyController nc = new NotifyController(factory, requestPool, checkerController);

            VirtualSensorController vsc = new VirtualSensorController(factory);
            VirtualSensorLinkController vslc = new VirtualSensorLinkController(factory);
            VirtualSensorCompositeController vscc = new VirtualSensorCompositeController(factory);
            VirtualSensorBlendingController vsbc = new VirtualSensorBlendingController(factory, checkerController);

            DataTypeController dtc = new DataTypeController(factory);
            ConverterController cc = new ConverterController(factory);
            FunctionController fc = new FunctionController(factory);            
            RevisionController rc = new RevisionController(factory);

            //scheduling
            schedulerBootstrap = new SchedulerBootstrap(factory.getSchedulerDao(), vsbc);
            //announcement
            omcpClientAnnouncer = new OmcpClientBuilder().host(ip).user(user, pass).source(moduleName).build();
            omcpClientBlending = new OmcpClientBuilder().host(ip).user(user, pass).source(moduleName).build();
            announcementBootstrap = new AnnouncementBootstrap(omcpClientAnnouncer);
            AnnouncementController announcementController = new AnnouncementController(announcementBootstrap.getAnnouncer());

            //setting extra components to the controllers
            nc.setAnnouncerAgent(announcementController);
            vslc.setAnnouncerAgent(announcementController);
            vscc.setAnnouncerAgent(announcementController);
            vsbc.setAnnouncerAgent(announcementController);
            checkerController.setAnnouncerAgent(announcementController);
            vsbc.setSchedulerAgent(schedulerBootstrap.getScheduler());
            vsbc.setClient(omcpClientBlending);

            //chain of responsibility
            nc.setNext(vsc);
            vsc.setNext(vslc);
            vslc.setNext(vscc);
            vscc.setNext(vsbc);
            vsbc.setNext(dtc);
            dtc.setNext(cc);
            cc.setNext(fc);
            fc.setNext(rc);

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
            omcpClientAnnouncer.close();
        } catch (Exception e) {
        }
        try {
            omcpClientBlending.close();
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
