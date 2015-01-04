/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.collector;

import br.uff.labtempo.osiris.to.collector.interfaces.INetworkCoTo;
import br.uff.labtempo.osiris.to.common.base.NetworkToBase;
import br.uff.labtempo.osiris.to.common.definitions.State;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class NetworkCoTo extends NetworkToBase implements INetworkCoTo {

    public NetworkCoTo(String id) {
        super(id, State.NEW);
    }

    @Override
    public void addInfo(String infoKeyName, String infoDescription) {
        super.addInfo(infoKeyName, infoDescription);
    }

    @Override
    public void addInfo(Map<String, String> infos) {
        super.addInfo(infos);
    }

    @Override
    public Map<String, String> getInfo() {
        return super.getInfo();
    }

}
