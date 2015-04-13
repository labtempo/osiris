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
package br.uff.labtempo.osiris.utils.scheduling.core;

import br.uff.labtempo.osiris.utils.scheduling.Scheduler;
import br.uff.labtempo.osiris.utils.scheduling.SchedulerItem;
import br.uff.labtempo.osiris.utils.scheduling.Scheduling;
import br.uff.labtempo.osiris.utils.scheduling.SchedulingCallback;
import br.uff.labtempo.osiris.utils.scheduling.SchedulingStorage;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SchedulingManagerTest {

    public SchedulingManagerTest() {

    }

    @Test
    public void testSomeMethod() {
        Scheduling scheduling = new SchedulingManager(new Storage(), new Callback(), 1, TimeUnit.SECONDS);
        scheduling.initialize();

        Scheduler scheduler = scheduling.getScheduler();

        scheduler.schedule(new Container(new Item(1)));
        scheduler.schedule(new Container(new Item(2)));
        scheduler.schedule(new Container(new Item(3)));
        scheduler.schedule(new Container(new Item(4)));
        scheduler.schedule(new Container(new Item(5)));
        scheduler.schedule(new Container(new Item(6)));
        scheduler.schedule(new Container(new Item(7)));
        scheduler.schedule(new Container(new Item(8)));
        scheduler.schedule(new Container(new Item(9)));
        scheduler.schedule(new Container(new Item(10)));
        scheduler.schedule(new Container(new Item(11)));
        scheduler.schedule(new Container(new Item(12)));

        try {
            Thread.sleep(300);
            System.out.println("closing");
            scheduling.close();
        } catch (Exception ex) {
            Logger.getLogger(SchedulingManagerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class Item {

        private long id;

        public Item(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

    }

    class Container implements SchedulerItem {

        private Item item;
        private long id;

        public Container(Item id) {
            this.item = item;
        }

        @Override
        public long getTimeToNextUpdate() {
            System.out.println("Pegou o tempo da proxima atualização");
            return 1000;
        }

        @Override
        public long getObjectId() {
            return 0;
        }

        @Override
        public void updateTimeToNextUpdate(long timeToNextUpdate) {
            System.out.println("Atualização do tempo");
        }

        @Override
        public long getIntervalInMillis() {
            System.out.println("Pegou o intervalo de atualização");
            return TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES) / 60;
        }

        @Override
        public boolean isRemovable() {
            return true;
        }

        @Override
        public void updateIntervalInMillis(long intervalInMillis) {
            System.out.println("Atualização do intervalo");
        }
    }

    class Storage implements SchedulingStorage {

        @Override
        public SchedulerItem getItemByObjectId(long id) {
            return new Container(new Item(id));
        }

        @Override
        public List<? extends SchedulerItem> getAllByTimeLimit(long timeLimitInMillis) {
            System.out.println("Pegou todos os itens!");
            return null;
        }

        @Override
        public void save(SchedulerItem o) {
            System.out.println("Salvo: " + o.getObjectId());
        }

        @Override
        public void update(SchedulerItem o) {
            System.out.println("Atualizou o item!");
        }

        @Override
        public void delete(SchedulerItem o) {
            System.out.println("Removeu o item!");
        }
    }

    class Callback implements SchedulingCallback {

        @Override
        public void callback(List<? extends SchedulerItem> items) throws Exception {
            System.out.println("Executou o Callback");
        }
    }
}
