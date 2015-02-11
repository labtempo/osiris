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
package br.uff.labtempo.osiris.sensornet.thirdparty.announcer;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.osiris.utils.announcement.Announcement;
import br.uff.labtempo.osiris.utils.announcement.Announcer;
import br.uff.labtempo.osiris.utils.announcement.core.AnnouncementManager;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class AnnouncementBootstrap implements AutoCloseable {

    private final Announcement manager;

    public AnnouncementBootstrap(OmcpClient client) {
        this.manager = new AnnouncementManager(client);
    }

    public void start() {
        this.manager.initialize();
    }

    @Override
    public void close() throws Exception {
        manager.close();
    }

    public Announcer getAnnouncer() {
        return manager.getAnnouncer();
    }

}
