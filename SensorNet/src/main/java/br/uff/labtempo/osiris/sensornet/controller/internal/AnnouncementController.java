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
package br.uff.labtempo.osiris.sensornet.controller.internal;

import br.uff.labtempo.osiris.sensornet.model.Consumable;
import br.uff.labtempo.osiris.sensornet.model.Rule;
import br.uff.labtempo.osiris.sensornet.model.util.ConsumableInfo;
import br.uff.labtempo.osiris.sensornet.thirdparty.announcer.AnnouncerAgent;
import br.uff.labtempo.osiris.utils.announcement.Announcer;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.to.notification.Notification;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class AnnouncementController implements AnnouncerAgent {

    private final Announcer annoucer;
    private final String moduleName;

    public AnnouncementController(Announcer announcer) {
        this.annoucer = announcer;
        this.moduleName = Path.NAMING_MODULE_SENSORNET.toString();
    }

    @Override
    public void broadcastIt(SensorSnTo objTo) {
        publishToUpdate(objTo);
    }

    @Override
    public void broadcastIt(CollectorSnTo objTo) {
        publishToUpdate(objTo);
    }

    @Override
    public void broadcastIt(NetworkSnTo objTo) {
        publishToUpdate(objTo);
    }

    @Override
    public void notifyBrokenConsumableRule(List<ConsumableInfo> highlightedConsumables, SensorSnTo objTo) {
        StringBuilder sb = new StringBuilder();
        if (highlightedConsumables != null && highlightedConsumables.size() > 0) {
            sb.append(AnnouncementView.CONSUMABLES_BROKEN)
                    .append(String.valueOf(highlightedConsumables.size()))
                    .append("\n");
            for (ConsumableInfo info : highlightedConsumables) {
                Consumable consumable = info.getConsumable();
                List<Rule> brokenRules = info.getBrokenRules();
                sb.append("\t")
                        .append(consumable.getName())
                        .append(" - ")
                        .append(AnnouncementView.RULES_BROKEN)
                        .append(brokenRules.size())
                        .append("\n");
                for (Rule rule : brokenRules) {
                    sb.append("\t\t")
                            .append(rule.getName())
                            .append(": ")
                            .append(consumable.getValue())
                            .append(" ")
                            .append(rule.getOperator())
                            .append(" ")
                            .append(rule.getValue())
                            .append("\n");
                }
            }
            sb.append("\n");
            notifyIt(Notification.Level.CRITICAL, resolveUri(objTo), AnnouncementView.BROKEN_CONSUMABLE_RULE.toString(), sb.toString(), moduleName);
        }
    }

    @Override
    public void notifyNew(NetworkSnTo objTo) {
        Notification.Level level = Notification.Level.INFO;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.NETWORK.toString() + AnnouncementView.ITEM_NEW;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyNew(CollectorSnTo objTo) {
        Notification.Level level = Notification.Level.INFO;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.COLLECTOR.toString() + AnnouncementView.ITEM_NEW;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyNew(SensorSnTo objTo) {
        Notification.Level level = Notification.Level.INFO;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.SENSOR.toString() + AnnouncementView.ITEM_NEW;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyMalfunction(SensorSnTo objTo) {
        Notification.Level level = Notification.Level.WARNING;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.SENSOR.toString() + AnnouncementView.ITEM_MALFUNCTION;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyMalfunction(CollectorSnTo objTo) {
        Notification.Level level = Notification.Level.WARNING;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.COLLECTOR.toString() + AnnouncementView.ITEM_MALFUNCTION;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyMalfunction(NetworkSnTo objTo) {
        Notification.Level level = Notification.Level.WARNING;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.NETWORK.toString() + AnnouncementView.ITEM_MALFUNCTION;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyReactivation(NetworkSnTo objTo) {
        Notification.Level level = Notification.Level.INFO;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.NETWORK.toString() + AnnouncementView.ITEM_REACTIVATED;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyReactivation(CollectorSnTo objTo) {
        Notification.Level level = Notification.Level.INFO;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.COLLECTOR.toString() + AnnouncementView.ITEM_REACTIVATED;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyReactivation(SensorSnTo objTo) {
        Notification.Level level = Notification.Level.INFO;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.SENSOR.toString() + AnnouncementView.ITEM_REACTIVATED;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyDeactivation(NetworkSnTo objTo) {
        Notification.Level level = Notification.Level.CRITICAL;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.NETWORK.toString() + AnnouncementView.ITEM_DISABLED;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyDeactivation(CollectorSnTo objTo) {
        Notification.Level level = Notification.Level.CRITICAL;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.COLLECTOR.toString() + AnnouncementView.ITEM_DISABLED;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyDeactivation(SensorSnTo objTo) {
        Notification.Level level = Notification.Level.CRITICAL;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.SENSOR.toString() + AnnouncementView.ITEM_DISABLED;
        genericNotifyToStateChange(level, uri, title);
    }

    private String resolveUri(NetworkSnTo objTo) {
        String uri = getHost() + objTo.getId();
        return uri;
    }

    private String resolveUri(CollectorSnTo objTo) {
        String uri = getHost() + objTo.getNetworkId() + Path.SEPARATOR + Path.NAMING_RESOURCE_COLLECTOR + Path.SEPARATOR + objTo.getId();
        return uri;
    }

    private String resolveUri(SensorSnTo obj) {
        String uri = getHost() + obj.getNetworkId() + Path.SEPARATOR + Path.NAMING_RESOURCE_COLLECTOR + Path.SEPARATOR + obj.getCollectorId() + Path.SEPARATOR + Path.NAMING_RESOURCE_SENSOR + Path.SEPARATOR + obj.getId();
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

    private void publishToUpdate(SensorSnTo obj) {
        // omcp://update.messagegroup/sensornet/{networkid}/collector/{collctorid}/sensors/{sensorid}
        String path = getPathBase(obj.getNetworkId()) + Path.SEPARATOR + Path.NAMING_RESOURCE_COLLECTOR + Path.SEPARATOR + obj.getCollectorId() + Path.SEPARATOR + Path.NAMING_RESOURCE_SENSOR + Path.SEPARATOR + obj.getId();
        annoucer.announce(obj, path);
    }

    private void publishToUpdate(CollectorSnTo obj) {
        // omcp://update.messagegroup/sensornet/{networkid}/collector/{collctorid} 
        String path = getPathBase(obj.getNetworkId()) + Path.SEPARATOR + Path.NAMING_RESOURCE_COLLECTOR + Path.SEPARATOR + obj.getId();
        annoucer.announce(obj, path);
    }

    private void publishToUpdate(NetworkSnTo obj) {
        // omcp://update.messagegroup/sensornet/{networkid}        
        String path = getPathBase(obj.getId());
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

    private String getPathBase(String networkId) {
        // omcp://update.messagegroup/sensornet/{networkid}
        String path = Path.MESSAGEGROUP_UPDATE.toString() + Path.NAMING_MODULE_SENSORNET + Path.SEPARATOR + networkId;
        return path;
    }

    private String getHost() {
        // omcp://sensornet/
        String path = Path.MODULE_SENSORNET.toString();
        return path;
    }

    private enum AnnouncementView {

        NETWORK("Network"),
        SENSOR("Sensor"),
        COLLECTOR("Collector"),
        ITEM_REACTIVATED(" reactivate!"),
        ITEM_DISABLED(" disabled!"),
        ITEM_NEW(" discovered!"),
        ITEM_MALFUNCTION(" is working incorrect or partially!"),
        BROKEN_CONSUMABLE_RULE("Consumable broken some rules"),
        CONSUMABLES_BROKEN("Consumables broken: "),
        RULES_BROKEN("Rules broken: ");

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
