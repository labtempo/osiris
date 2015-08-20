/*
 * Copyright 2015 Felipe.
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
package br.uff.labtempo.osiris.utils.persistence.jpa.batch;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class BatchPersistenceCommitBySecond extends AbstractBatchPersistence {

    private final EntityTransaction et;
    private int count;
    private Timer timer;

    public BatchPersistenceCommitBySecond(EntityManager em) {
        super(em);
        this.et = em.getTransaction();
        setTask(1);
        et.begin();
    }

    @Override
    public synchronized void save(Object o) {
        EntityManager em = getEntityManager();
        count++;
        em.persist(o);
    }

    @Override
    public synchronized void update(Object o) {
        EntityManager em = getEntityManager();
        count++;
        em.merge(o);
    }

    @Override
    public synchronized void delete(Object o) {
        EntityManager em = getEntityManager();
        count++;
        em.remove(em.contains(o) ? o : em.merge(o));
    }

    private synchronized void commit() throws Exception {
        try {
            et.commit();
        } catch (Exception ex) {
            Logger.getLogger(BatchPersistenceCommitBySecond.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            super.getEntityManager().clear();
        } catch (Exception ex) {
        }
        et.begin();
    }

    @Override
    public void close() throws Exception {
        try {
            et.commit();
        } catch (Exception e) {
        }

        try {
            super.close();
        } catch (Exception e) {
        }

        try {
            timer.cancel();
        } catch (Exception e) {
        }

        try {
            timer.purge();
        } catch (Exception e) {
        }

    }

    private void setTask(int second) {
        this.timer = new Timer("Auto Commit");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (count > 0) {
                    try {
                        commit();
                        count = 0;
                    } catch (Exception ex) {
                        Logger.getLogger(BatchPersistenceCommitBySecond.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        long millis = TimeUnit.SECONDS.toMillis(second);
        timer.scheduleAtFixedRate(task, millis, millis);
    }

    @Override
    public synchronized void forceCommit() {
        try {
            et.commit();
        } catch (Exception ex) {
        }
        et.begin();
    }
}
