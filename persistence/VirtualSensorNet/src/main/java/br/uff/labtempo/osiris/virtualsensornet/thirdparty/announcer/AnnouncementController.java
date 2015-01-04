/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer;

import br.uff.labtempo.osiris.thirdparty.announcement.Announcer;
import br.uff.labtempo.osiris.to.notification.Notification;
import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import static br.uff.labtempo.osiris.virtualsensornet.controller.ControllerPath.COLLECTOR_UNIT;
import static br.uff.labtempo.osiris.virtualsensornet.controller.ControllerPath.NOTIFICATION_MESSAGEGROUP;
import static br.uff.labtempo.osiris.virtualsensornet.controller.ControllerPath.PROTOCOL;
import static br.uff.labtempo.osiris.virtualsensornet.controller.ControllerPath.SENSOR_UNIT;
import static br.uff.labtempo.osiris.virtualsensornet.controller.ControllerPath.SEPARATOR;
import static br.uff.labtempo.osiris.virtualsensornet.controller.ControllerPath.UPDATE_MESSAGEGROUP;
import br.uff.labtempo.osiris.virtualsensornet.persistence.AnnouncerDao;

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
    public void broadcastIt(LinkVsnTo objTo) {
        publishToUpdate(objTo);
    }

    @Override
    public void notifyNew(LinkVsnTo objTo) {
        Notification.Level level = Notification.Level.INFO;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.NETWORK.toString() + AnnouncementView.ITEM_NEW;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyMalfunction(LinkVsnTo objTo) {
        Notification.Level level = Notification.Level.WARNING;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.SENSOR.toString() + AnnouncementView.ITEM_MALFUNCTION;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyReactivation(LinkVsnTo objTo) {
        Notification.Level level = Notification.Level.INFO;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.NETWORK.toString() + AnnouncementView.ITEM_REACTIVATED;
        genericNotifyToStateChange(level, uri, title);
    }

    @Override
    public void notifyDeactivation(LinkVsnTo objTo) {
        Notification.Level level = Notification.Level.CRITICAL;
        String uri = resolveUri(objTo);
        String title = AnnouncementView.COLLECTOR.toString() + AnnouncementView.ITEM_DISABLED;
        genericNotifyToStateChange(level, uri, title);
    }

    private String resolveUri(LinkVsnTo objTo) {
        String uri = getHost() + objTo.getId();
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

    private void publishToUpdate(LinkVsnTo obj) {
        // omcp://update.messagegroup/virtualsensornet/vsensor/{vsensorid}
        String path = getPathBase(obj.getNetworkId()) + COLLECTOR_UNIT + obj.getCollectorId() + SENSOR_UNIT + obj.getId();
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
