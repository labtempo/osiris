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
package br.uff.labtempo.osiris.sensornet.thirdparty.announcer;

import br.uff.labtempo.osiris.sensornet.model.Sensor;
import br.uff.labtempo.osiris.sensornet.model.util.ConsumableInfo;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.List;


/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public interface AnnouncerAgent {

    void broadcastIt(SensorSnTo objTo);

    void broadcastIt(CollectorSnTo objTo);

    void broadcastIt(NetworkSnTo objTo);

    void notifyBrokenConsumableRule(List<ConsumableInfo> highlightedConsumables, SensorSnTo objTo);

    void notifyDeactivation(NetworkSnTo objTo);

    void notifyDeactivation(CollectorSnTo objTo);

    void notifyDeactivation(SensorSnTo objTo);

    void notifyMalfunction(SensorSnTo objTo);

    void notifyMalfunction(CollectorSnTo objTo);

    void notifyMalfunction(NetworkSnTo objTo);

    void notifyNew(NetworkSnTo objTo);

    void notifyNew(CollectorSnTo objTo);

    void notifyNew(SensorSnTo objTo);

    void notifyReactivation(NetworkSnTo objTo);

    void notifyReactivation(CollectorSnTo objTo);

    void notifyReactivation(SensorSnTo objTo);
}
