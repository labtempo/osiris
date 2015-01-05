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
package br.uff.labtempo.osiris.virtualsensornet.persistence.jpa;

import br.uff.labtempo.osiris.virtualsensornet.persistence.AnnouncerDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.ConverterDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.LinkDao;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer.AnnouncementBootstrap;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class JpaDaoFactory implements DaoFactory, AutoCloseable {

    private static JpaDaoFactory instance;

    private DataManager data;
    private AnnouncementBootstrap announcementConfig;

    private JpaDaoFactory(String ip, String usr, String pwd, String moduleName) throws Exception {
        try {
            announcementConfig = new AnnouncementBootstrap(ip, usr, pwd, moduleName);
            data = new DataManager();
        } catch (Exception ex) {
            close();
            throw ex;
        }
    }

    public static JpaDaoFactory newInstance(String ip, String usr, String pwd, String moduleName) throws Exception {
        if (instance == null) {
            instance = new JpaDaoFactory(ip, usr, pwd, moduleName);
        }
        return instance;
    }

    public static JpaDaoFactory getInstance() {
        if (instance == null) {
            throw new RuntimeException("Factory need be created as a new instance!");
        }
        return instance;
    }

    @Override
    public void close() throws Exception {
        try {
            announcementConfig.close();
        } catch (Exception e) {
        }
        try {
            data.close();
        } catch (Exception e) {
        }
    }

    @Override
    public LinkDao getLinkDao() {
        return new LinkJpa(data);
    }

    @Override
    public ConverterDao getConverterDao() {
        return new ConverterJpa(data);
    }

    @Override
    public DataTypeDao getDataTypeDao() {
        return new DataTypeJpa(data);
    }

    @Override
    public AnnouncerDao getAnnouncerDao() {
        return announcementConfig.getAnnouncer();
    }

}
