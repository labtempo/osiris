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
package br.uff.labtempo.osiris.thirdparty.announcement.core;
import br.uff.labtempo.osiris.thirdparty.announcement.Announcer;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class AnnouncementProducer  implements Announcer{

    private final BlockingQueue<AnnouncementItem> queue;

    AnnouncementProducer(BlockingQueue<AnnouncementItem> queue) {
        this.queue = queue;
    }

  
    @Override
    public void announce(Object obj, String address) {        
        try {
            queue.put(new AnnouncementItem(obj, address));
        } catch (InterruptedException ex) {
            Logger.getLogger(AnnouncementProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
