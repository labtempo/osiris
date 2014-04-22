/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.osiris.controller;

import br.uff.labtempo.osiris.data.DataManager;
import br.uff.labtempo.osiris.model.Mote;
import br.uff.labtempo.osiris.model.Sample;
import br.uff.labtempo.osiris.model.VirtualSensor;
import br.uff.labtempo.osiris.util.components.Component;
import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.logging.Log;
import java.util.List;

/**
 *
 * @author Felipe
 */
public class ClientController extends Component{
    private DataManager data;

    public ClientController(DataManager data) {
        this.data = data;
    }
    
    public void createVSensor(String name){
        VirtualSensor vsensor = new VirtualSensor(name);
        data.save(vsensor);
    }
    public void bind(String vsensorId, String moteId){
        Mote mote = data.get(Mote.class, moteId);
        
        if(mote == null)
            return;
        
        VirtualSensor vsensor = data.get(VirtualSensor.class, vsensorId);
        
        if(vsensor == null)
            return;
        
        vsensor.setMote(mote);
        
        data.update(vsensor);
    
    }
    public void unbind(String vsensorId){
        VirtualSensor vsensor = data.get(VirtualSensor.class, vsensorId);
        
        if(vsensor == null)
            return;
        
        vsensor.setMote(null);
        
        data.update(vsensor);
    }
    public void getFreeItems(){}
    public void getBoundItems(){}
    public void getSamples(String vsensorId){
        VirtualSensor vsensor = data.get(VirtualSensor.class, vsensorId);
        
        if(vsensor == null)
            return;
        
        List<Sample> samples = vsensor.getSamples();
    }    

    @Override
    protected void onCreate() throws ComponentInitializationException {
    }
    
    @Override
    protected void onStop() {
        Log.D("Closing ClientController");
    }
}
