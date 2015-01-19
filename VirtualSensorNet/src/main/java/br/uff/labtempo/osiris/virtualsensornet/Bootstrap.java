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
import br.uff.labtempo.osiris.virtualsensornet.controller.AnnouncementController;
import br.uff.labtempo.osiris.virtualsensornet.controller.ConverterController;
import br.uff.labtempo.osiris.virtualsensornet.controller.DataTypeController;
import br.uff.labtempo.osiris.virtualsensornet.controller.NotifyController;
import br.uff.labtempo.osiris.virtualsensornet.controller.SchedulerController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorLinkController;
import br.uff.labtempo.osiris.virtualsensornet.persistence.jpa.JpaDaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer.AnnouncementBootstrap;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.scheduler.SchedulerBootstrap;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Bootstrap implements AutoCloseable {

    private final String pwd;
    private final String usr;
    private final String ip;
    private final String moduleName;

    private JpaDaoFactory factory;

    private OmcpServer omcpServer;
    private String persistenceUnitName;
    private final SchedulerBootstrap schedulerBootstrap;
    private final OmcpClient omcpClient;
    private final AnnouncementBootstrap announcementBootstrap;

    public Bootstrap() throws Exception {

        ip = "192.168.0.7";
        usr = "admin";
        pwd = "admin";
        moduleName = "virtualsensornet";
        persistenceUnitName = "production";
        
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
            factory = JpaDaoFactory.newInstance(emf);

            NotifyController nc = new NotifyController(factory);
            VirtualSensorLinkController vslc = new VirtualSensorLinkController(factory);
            DataTypeController dtc = new DataTypeController(factory);
            ConverterController cc = new ConverterController(factory);

            SchedulerController schedulerController = new SchedulerController(factory);
            schedulerBootstrap = new SchedulerBootstrap(factory.getSchedulerDao(), schedulerController);

            omcpClient = new OmcpClientBuilder().host(ip).user(usr, pwd).source(moduleName).build();
            announcementBootstrap = new AnnouncementBootstrap(omcpClient);
            AnnouncementController announcementController = new AnnouncementController(announcementBootstrap.getAnnouncer());

            schedulerController.setAnnouncerAgent(announcementController);
            nc.setAnnouncerAgent(announcementController);
            nc.setSchedulerAgent(schedulerBootstrap.getScheduler());

            omcpServer = new RabbitServer(moduleName, ip, usr, pwd);

            nc.setNext(vslc);
            vslc.setNext(dtc);
            dtc.setNext(cc);

            omcpServer.setHandler(nc);

            omcpServer.addReference("omcp://collector.messagegroup/#");
            omcpServer.addReference("omcp://update.messagegroup/sensornet/#");

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
}
