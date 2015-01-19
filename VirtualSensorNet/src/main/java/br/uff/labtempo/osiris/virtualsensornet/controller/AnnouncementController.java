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
package br.uff.labtempo.osiris.virtualsensornet.controller;

import br.uff.labtempo.osiris.thirdparty.announcement.Announcer;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.to.notification.Notification;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer.AnnouncerAgent;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class AnnouncementController implements AnnouncerAgent {

    private final Announcer annoucer;
    private final String moduleName;

    public AnnouncementController(Announcer announcer) {
        this.annoucer = announcer;
        this.moduleName = Path.NAMING_MODULE_VIRTUALSENSORNET.toString();
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
        //omcp://virtualsensornet/vsensor/{vsensorid}
        String uri = getHost() + Path.NAMING_RESOURCE_VIRTUALSENSOR + Path.SEPARATOR + objTo.getId();
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
        String path = getPathBase() + Path.NAMING_RESOURCE_VIRTUALSENSOR + Path.SEPARATOR + obj.getId();
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
        String path = Path.MESSAGEGROUP_NOTIFICATION.toString() + obj.getLevel();
        annoucer.announce(obj, path);
    }

    private String getPathBase() {
        // omcp://update.messagegroup/virtualsensornet/
        String path = Path.MESSAGEGROUP_UPDATE.toString() + Path.NAMING_RESOURCE_VIRTUALSENSOR + Path.SEPARATOR;
        return path;
    }

    private String getHost() {
        // omcp://virtualsensornet/
        String path = Path.MODULE_VIRTUALSENSORNET.toString();
        return path;
    }

    private enum AnnouncementView {

        VIRTUAL_SENSOR("VirtualSensor"),
        ITEM_REACTIVATED(" reactivate!"),
        ITEM_DISABLED(" disabled!"),
        ITEM_NEW(" discovered!"),
        ITEM_MALFUNCTION(" is working incorrect or partially!");

        private AnnouncementView(String content) {
            this.content = content;
        }

        private final String content;

        @Override
        public String toString() {
            return content;
        }
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
