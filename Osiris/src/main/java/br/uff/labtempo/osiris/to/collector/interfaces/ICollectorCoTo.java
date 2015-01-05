/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.collector.interfaces;

import br.uff.labtempo.osiris.to.common.data.InfoTo;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe
 */
public interface ICollectorCoTo {

    String getId();

    long getInterval();

    TimeUnit getTimeUnit();

    void addInfo(Map<String, String> infos);

    void addInfo(String infoKeyName, String infoDescription);

    Map<String, String> getInfo();

    List<? extends InfoTo> getInfoTo();

}
