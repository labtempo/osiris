/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.data;

import br.uff.labtempo.osiris.util.components.Component;
import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.logging.Log;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author Felipe
 */
public class DataManager extends Component {

    private EntityManagerFactory EMF;
    private EntityManager EM;

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            EMF = Persistence.createEntityManagerFactory("Standalone");
            EM = EMF.createEntityManager();
        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }
    }

    @Override
    protected void onStop() {
        if (EMF.isOpen()) {
            Log.D("Closing DataManager");
            EM.close();
            EMF.close();
        }
    }

    public void save(Object o) {
        EntityTransaction et = EM.getTransaction();
        et.begin();
        EM.persist(o);
        et.commit();
    }

    public <T> T get(Class<T> entityType, Object key) {
        return EM.find(entityType, key);
    }

    public <T> T getReference(Class<T> entityType, Object key) {
        return EM.getReference(entityType, key);
    }

    public void update(Object o) {
        EntityTransaction et = EM.getTransaction();
        et.begin();
        EM.merge(o);
        et.commit();
    }

    public void delete(Object o) {
        EntityTransaction et = EM.getTransaction();
        et.begin();
        EM.remove(o);
        et.commit();
    }
}
