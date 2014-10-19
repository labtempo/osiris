/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.jpa;

import br.uff.labtempo.osiris.sensornet.model.state.Model;
import br.uff.labtempo.osiris.sensornet.to.CollectorTo;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Felipe
 */
@Entity
public class Collector extends Model<Collector> {

    @Id
    private String id;

    @ManyToOne
    private Network network;

    @OneToMany(mappedBy = "collector", cascade = CascadeType.ALL)
    private List<Sensor> sensors;

    public String getId() {
        return id;
    }

    public Network getNetwork() {
        return network;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public CollectorTo getTransferObject() {
        return new CollectorTo(id, getLastModifiedDate().getTimeInMillis(), id, network.getId(), sensors.size(), getInfo());
    }
}
