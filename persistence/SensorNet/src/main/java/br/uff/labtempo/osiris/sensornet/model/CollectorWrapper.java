/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model;

import br.uff.labtempo.osiris.sensornet.model.state.Model;
import br.uff.labtempo.osiris.collector.temp.Collector;
import java.util.Objects;

/**
 *
 * @author Felipe
 */
public class CollectorWrapper extends Model<CollectorWrapper>{

    private Collector collector;

    public CollectorWrapper(Collector collector) {
        this.collector = collector;
    }
    
    public String getId() {
        return collector.getId();
    }

    @Override
    public void update(CollectorWrapper wrapper) {
        if (!equalsInfo(wrapper)) {
            this.collector = wrapper.collector;
            super.update();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return collector.equals(obj);
    }

    @Override
    public int hashCode() {
        return collector.hashCode();
    }

    public boolean equalsInfo(CollectorWrapper wrapper) {
        if (equals(wrapper.collector) && collector.getInfo().equals(wrapper.collector.getInfo())) {
            return true;
        }
        return false;
    }
    
    public Collector getContent(){
        return collector;
    }

}
