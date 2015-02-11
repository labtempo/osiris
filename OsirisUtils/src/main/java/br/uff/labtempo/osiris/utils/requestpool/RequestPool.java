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
package br.uff.labtempo.osiris.utils.requestpool;

import br.uff.labtempo.omcp.common.Request;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class RequestPool implements AutoCloseable {

    private final BlockingQueue<Request> queue;
    private Thread thread;
    private RequestHandler handler;

    public RequestPool() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public void start() {
        thread = new Thread(new Consumer(), "Request consumer");
        thread.setDaemon(true);
        thread.setPriority(2);
        thread.start();
        
        
    }
    
    public void add(Request request){
        queue.add(request);
    }

    @Override
    public void close() throws Exception {
        queue.add(null);
        if (thread != null) {
            thread.join();
        }
    }

    private void deliver(Request request) {
        if (handler != null) {
            try {
                handler.handle(request);
            } catch (Exception ex) {
                Logger.getLogger(RequestPool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Request request = queue.take();
                    if (request != null) {                        
                            deliver(request);
                    } else {
                        break;
                    }
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }
    }

    public void setHandler(RequestHandler handler) {
        this.handler = handler;
    }
}
