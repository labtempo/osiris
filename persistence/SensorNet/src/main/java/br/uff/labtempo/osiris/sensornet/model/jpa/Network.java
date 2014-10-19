/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.jpa;

import br.uff.labtempo.osiris.sensornet.model.state.Model;
import br.uff.labtempo.osiris.sensornet.to.NetworkTo;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Felipe
 */
@Entity
public class Network extends Model<Network> {

    @Id
    private String id;

    @OneToMany(mappedBy = "network", cascade = CascadeType.ALL)
    private List<Collector> collectors;

    @OneToMany(mappedBy = "network", cascade = CascadeType.ALL)
    private List<Sensor> sensors;

    public String getId() {
        return id;
    }

    public List<Collector> getCollectors() {
        return collectors;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public NetworkTo getTransferObject() {
        return new NetworkTo(id, getLastModifiedDate().getTimeInMillis(), id, collectors.size(), sensors.size(), getInfo());
    }

}
