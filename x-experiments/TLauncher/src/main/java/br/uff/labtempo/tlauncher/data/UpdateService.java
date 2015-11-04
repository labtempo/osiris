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
package br.uff.labtempo.tlauncher.data;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.service.EventHandler;
import br.uff.labtempo.omcp.service.OmcpService;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public abstract class UpdateService<T> implements AutoCloseable, EventHandler {

    private OmcpService service;
    private final UpdateListener<T> listener;
    private final Class<?> klass;
    private final String address;

    public UpdateService(OmcpService service, UpdateListener<T> listener, Class<?> klass, String address) {
        this.service = service;
        this.listener = listener;
        this.klass = klass;
        this.address = address;
    }

    public void start() {
        service.addReference(address);
        service.setHandler(this);
        service.start();
    }

    @Override
    public void close() throws Exception {
        service.close();
    }

    @Override
    public void handle(Request request) {
        try {
            T object = request.getContent(klass);
            long unpackingTimestamp = System.currentTimeMillis();
            listener.updateReceived(object, unpackingTimestamp);
        } catch (BadRequestException ex) {
        }
    }

    public boolean isStarted() {
        return service.isStarted();
    }
}
