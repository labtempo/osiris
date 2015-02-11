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
package br.uff.labtempo.osiris.sensornet.model.util;

import br.uff.labtempo.osiris.sensornet.thirdparty.announcer.AnnouncerAgent;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.List;

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
    public void broadcastIt(SensorSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.broadcastIt(objTo);
    }

    @Override
    public void broadcastIt(CollectorSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.broadcastIt(objTo);
    }

    @Override
    public void broadcastIt(NetworkSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.broadcastIt(objTo);
    }

    @Override
    public void notifyBrokenConsumableRule(List<ConsumableInfo> highlightedConsumables, SensorSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyBrokenConsumableRule(highlightedConsumables, objTo);
    }

    @Override
    public void notifyDeactivation(NetworkSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyDeactivation(objTo);
    }

    @Override
    public void notifyDeactivation(CollectorSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyDeactivation(objTo);
    }

    @Override
    public void notifyDeactivation(SensorSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyDeactivation(objTo);
    }

    @Override
    public void notifyMalfunction(SensorSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyMalfunction(objTo);
    }

    @Override
    public void notifyMalfunction(CollectorSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyMalfunction(objTo);
    }

    @Override
    public void notifyMalfunction(NetworkSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyMalfunction(objTo);
    }

    @Override
    public void notifyNew(NetworkSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyNew(objTo);
    }

    @Override
    public void notifyNew(CollectorSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyNew(objTo);
    }

    @Override
    public void notifyNew(SensorSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyNew(objTo);
    }

    @Override
    public void notifyReactivation(NetworkSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyReactivation(objTo);
    }

    @Override
    public void notifyReactivation(CollectorSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyReactivation(objTo);
    }

    @Override
    public void notifyReactivation(SensorSnTo objTo) {
        if (announcer == null) {
            return;
        }
        announcer.notifyReactivation(objTo);
    }
}
