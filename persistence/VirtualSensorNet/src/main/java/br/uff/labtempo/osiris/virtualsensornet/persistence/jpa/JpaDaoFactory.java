/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.persistence.jpa;

import br.uff.labtempo.osiris.virtualsensornet.persistence.AnnouncerDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.ConverterDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.LinkDao;

/**
 *
 * @author Felipe
 */
public class JpaDaoFactory implements DaoFactory, AutoCloseable {

    private static JpaDaoFactory instance;

    private DataManager data;

    private JpaDaoFactory(String ip, String usr, String pwd, String moduleName) throws Exception {
        try {
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}