/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.thirdparty.announcer;

import static br.uff.labtempo.osiris.sensornet.controller.ControllerPath.*;
import br.uff.labtempo.osiris.sensornet.model.jpa.Consumable;
import br.uff.labtempo.osiris.sensornet.model.jpa.Rule;
import br.uff.labtempo.osiris.sensornet.model.util.ConsumableInfo;
import br.uff.labtempo.osiris.sensornet.persistence.AnnouncerDao;
import br.uff.labtempo.osiris.thirdparty.announcement.Announcer;
import br.uff.labtempo.osiris.to.notification.Notification;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.List;

/**
 *
 * @author Felipe
 */
public class AnnouncementController implements AnnouncerDao {

    private final Announcer annoucer;
    private final String moduleName;

    AnnouncementController(Announcer announcer, String moduleName) {
        this.annoucer = announcer;
        this.moduleName = moduleName;
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
        String uri = getHost() +  objTo.getId();
        return uri;
    }

    private String resolveUri(CollectorSnTo objTo) {
        String uri = getHost() +  objTo.getNetworkId() + COLLECTOR_UNIT + objTo.getId();
        return uri;
    }

    private String resolveUri(SensorSnTo obj) {
        String uri = getHost() +  obj.getNetworkId() + COLLECTOR_UNIT + obj.getCollectorId() + SENSOR_UNIT + obj.getId();
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
        String path = getPathBase(obj.getNetworkId()) + COLLECTOR_UNIT + obj.getCollectorId() + SENSOR_UNIT + obj.getId();
        annoucer.announce(obj, path);
    }

    private void publishToUpdate(CollectorSnTo obj) {
        // omcp://update.messagegroup/sensornet/{networkid}/collector/{collctorid} 
        String path = getPathBase(obj.getNetworkId()) + COLLECTOR_UNIT + obj.getId();
        annoucer.announce(obj, path);
    }

    private void publishToUpdate(NetworkSnTo obj) {
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
        String path = PROTOCOL.toString() + NOTIFICATION_MESSAGEGROUP + SEPARATOR + obj.getLevel();
        annoucer.announce(obj, path);
    }

    private String getPathBase(String networkId) {
        // omcp://update.messagegroup/sensornet/{networkid}
        String path = PROTOCOL.toString() + UPDATE_MESSAGEGROUP + SEPARATOR + moduleName + SEPARATOR + networkId;
        return path;
    }

    private String getHost() {
        // omcp://sensornet/
        String path = PROTOCOL.toString() + moduleName + SEPARATOR;
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
