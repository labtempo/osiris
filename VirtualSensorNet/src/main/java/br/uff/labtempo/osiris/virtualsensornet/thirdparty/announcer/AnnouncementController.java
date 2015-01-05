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
package br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer;

import br.uff.labtempo.osiris.thirdparty.announcement.Announcer;
import br.uff.labtempo.osiris.to.notification.Notification;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;
import static br.uff.labtempo.osiris.virtualsensornet.controller.ControllerPath.NOTIFICATION_MESSAGEGROUP;
import static br.uff.labtempo.osiris.virtualsensornet.controller.ControllerPath.PROTOCOL;
import static br.uff.labtempo.osiris.virtualsensornet.controller.ControllerPath.SEPARATOR;
import static br.uff.labtempo.osiris.virtualsensornet.controller.ControllerPath.UPDATE_MESSAGEGROUP;
import static br.uff.labtempo.osiris.virtualsensornet.controller.ControllerPath.VIRTUAL_SENSOR_ALL;
import br.uff.labtempo.osiris.virtualsensornet.persistence.AnnouncerDao;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class AnnouncementController implements AnnouncerDao {

    private final Announcer annoucer;
    private final String moduleName;

    AnnouncementController(Announcer announcer, String moduleName) {
        this.annoucer = announcer;
        this.moduleName = moduleName;
    }

    @Override
    public void broadcastIt(VirtualSensorVsnTo objTo) {
        publishToUpdate(objTo);
    }

    @Override
    public void notifyNew(VirtualSensorVsnTo objTo) {
        Notification.Level level = Notification.Level.INFO;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.VIRTUAL_SENSOR.toString() + AnnouncementView.ITEM_NEW;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyMalfunction(VirtualSensorVsnTo objTo) {
        Notification.Level level = Notification.Level.WARNING;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.VIRTUAL_SENSOR.toString() + AnnouncementView.ITEM_MALFUNCTION;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyReactivation(VirtualSensorVsnTo objTo) {
        Notification.Level level = Notification.Level.INFO;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.VIRTUAL_SENSOR.toString() + AnnouncementView.ITEM_REACTIVATED;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyDeactivation(VirtualSensorVsnTo objTo) {
        Notification.Level level = Notification.Level.CRITICAL;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.VIRTUAL_SENSOR.toString() + AnnouncementView.ITEM_DISABLED;
        genericNotifyToStateChange(level, uri, title);
    }

    private String resolveUri(VirtualSensorVsnTo objTo) {
//        String resource = null;
//
//        switch (objTo.getSensorType()) {
//            case LINK:
//                resource = VS_LINK_ALL.toString();
//                break;
//            case COMPOSITE:
//                resource = VS_COMPOSITE_ALL.toString();
//                break;
//            case BLENDING:
//                resource = VS_BLENDING_ALL.toString();
//                break;
//        }

        String uri = getHost() + VIRTUAL_SENSOR_ALL + objTo.getId();
        return uri;
    }

    private void genericNotifyToStateChange(Notification.Level level, String uri, String title) {
        String msg = "At " + moduleName + ": " + uri;
        String origin = moduleName;
        notifyIt(level, uri, title, msg, origin);
    }

    private void notifyIt(Notification.Level level, String uri, String title, String message, String origin) {
        Notification notif = new Notification();
        notif.setTitle(title);
        notif.setMessage(message);
        notif.setUri(uri);
        notif.setOrigin(origin);
        notif.setLevel(level);
        
        notifyActivity(notif);
    }

    private void publishToUpdate(VirtualSensorVsnTo obj) {
        // omcp://update.messagegroup/virtualsensornet/vsensor/{vsensorid}
        String path = getPathBase() + VIRTUAL_SENSOR_ALL + obj.getId();
        annoucer.announce(obj, path);
    }

    /**
     * sensor-collector-network down
     *
     * network malfunction
     *
     * sensor-collector-network new
     *
     * consumable
     */
    private void notifyActivity(Notification obj) {
        // omcp://notification.messagegroup/{level}
        String path = PROTOCOL.toString() + NOTIFICATION_MESSAGEGROUP + SEPARATOR + obj.getLevel();
        annoucer.announce(obj, path);
    }

    private String getPathBase() {
        // omcp://update.messagegroup/virtualsensornet/
        String path = PROTOCOL.toString() + UPDATE_MESSAGEGROUP + SEPARATOR + moduleName;
        return path;
    }

    private String getHost() {
        // omcp://virtualsensornet/
        String path = PROTOCOL.toString() + moduleName ;
        return path;
    }
}

//aplicativo para publicar anuncios
/*
 * TMON
 * 
 * New measure(updated)
 * 
 * Alert
 *      1 Anomaly (virtual sensor app)   
 *      2 Forecast (virtual sensor app)    
 *      3 Low Battery(consumable)
 *      4 Mote Down(no signal)
 *      5 WSN Down(no signal from all collectors)
 *      6 Battery Replaced(reactivated)
 *      7 Mote Boot(new node found)
 *      8 *Mote Desynchronized(not implemented yet)
 * 
 * Maintenance(not support)
 * 
 * New Forecast(virtual sensor updated)
 * 
 */
/**
 * object uri in sensonet
 *
 * omcp://update.messagegroup/sensornet/networks/{networkid}
 * omcp://update.messagegroup/sensornet/networks/{networkid}/collector/{collctorid}
 * omcp://update.messagegroup/sensornet/networks/{networkid}/collector/{collctorid}/sensors/{sensorid}
 *
 * omcp://update.messagegroup/virtualsensornet/vsensors/{vsensorid}
 *
 */
