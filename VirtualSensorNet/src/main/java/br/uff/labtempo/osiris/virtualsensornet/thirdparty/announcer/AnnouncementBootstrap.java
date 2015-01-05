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
package br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.osiris.thirdparty.announcement.Announcement;
import br.uff.labtempo.osiris.thirdparty.announcement.core.AnnouncementManager;
import br.uff.labtempo.osiris.virtualsensornet.persistence.AnnouncerDao;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
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
