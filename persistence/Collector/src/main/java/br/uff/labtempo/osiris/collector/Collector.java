/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector;

import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.components.conn.Publisher;

/**
 *
 * @author Felipe
 */
public class Collector extends Module {

    private Publisher publisher;
    private final String ID = "virtual";
    private final String RESOURCE_REFERENCE = "network." + ID;

    public Collector() {
        super("Collector");
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            publisher = new Publisher(RESOURCE_REFERENCE, "localhost");
            addRequire(publisher);
            addProvide(new Sensor(231, 6000, publisher, ID));
            addProvide(new Sensor(232, 6000, publisher, ID,231));
            addProvide(new Sensor(233, 6000, publisher, ID,231));
            addProvide(new Sensor(234, 6000, publisher, ID,233));
            addProvide(new Sensor(235, 6000, publisher, ID,233));

        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }
    }

}
