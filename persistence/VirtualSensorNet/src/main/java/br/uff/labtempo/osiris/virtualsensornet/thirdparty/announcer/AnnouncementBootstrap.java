/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.osiris.thirdparty.announcement.Announcement;
import br.uff.labtempo.osiris.thirdparty.announcement.core.AnnouncementManager;
import br.uff.labtempo.osiris.virtualsensornet.persistence.AnnouncerDao;

/**
 *
 * @author Felipe
 */
public class AnnouncementBootstrap implements AutoCloseable {

    private final Announcement manager;
    private final String moduleName;

    public AnnouncementBootstrap(String ip, String usr, String pwd, String moduleName) {
        this.moduleName = moduleName;
        OmcpClient client = new OmcpClientBuilder().host(ip).user(usr, pwd).source(moduleName).build();
        this.manager = new AnnouncementManager(client);
        this.manager.initialize();
    }

    @Override
    public void close() throws Exception {
        manager.close();
    }
    
    public AnnouncerDao getAnnouncer(){
        AnnouncementController sensornetAnnounce = new AnnouncementController(manager.getAnnouncer(), moduleName);
        return sensornetAnnounce;
    }
}
