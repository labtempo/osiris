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
package br.uff.labtempo.osiris.virtualsensornet.model.util;

import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer.AnnouncerAgent;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class AnnouncerWrapper implements AnnouncerAgent {

    private final AnnouncerAgent announcer;

    public AnnouncerWrapper(AnnouncerAgent announcer) {
        this.announcer = announcer;
    }

    @Override
    public void broadcastIt(VirtualSensorVsnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.broadcastIt(objTo);
    }

    @Override
    public void notifyDeactivation(VirtualSensorVsnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyDeactivation(objTo);

    }

    @Override
    public void notifyMalfunction(VirtualSensorVsnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyMalfunction(objTo);
    }

    @Override
    public void notifyNew(VirtualSensorVsnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyNew(objTo);
    }

    @Override
    public void notifyReactivation(VirtualSensorVsnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyReactivation(objTo);
    }

}