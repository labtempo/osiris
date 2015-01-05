/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.model;

import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
import java.util.List;

/**
 *
 * @author Felipe
 */
/**
 * Post to create
 *
 * Put to update value
 *
 * Delete to remove BlendingVSensor from virtualsensornet
 */
public class VirtualSensorBlending extends VirtualSensor {

    private List<VirtualSensor> sources;
    private Function function;

    public VirtualSensorBlending(List<Field> fields) {
        super(VirtualSensorType.BLENDING, fields);
    }
}
