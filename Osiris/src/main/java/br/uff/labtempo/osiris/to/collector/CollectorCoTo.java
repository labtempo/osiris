/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.collector;

import br.uff.labtempo.osiris.to.collector.interfaces.ICollectorCoTo;
import br.uff.labtempo.osiris.to.common.base.CollectorToBase;
import br.uff.labtempo.osiris.to.common.definitions.State;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe
 */
public class CollectorCoTo extends CollectorToBase implements ICollectorCoTo {

    public CollectorCoTo(String id, long interval, TimeUnit timeUnit) {
        super(id, State.NEW, interval, timeUnit);
    }

    @Override
    public void addInfo(Map<String, String> infos) {
        super.addInfo(infos);
    }

    @Override
    public void addInfo(String infoKeyName, String infoDescription) {
        super.addInfo(infoKeyName, infoDescription);
    }

    @Override
    public Map<String, String> getInfo() {
        return super.getInfo();
    }

}
