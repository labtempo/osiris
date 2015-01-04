/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.controller;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.RequestMethod;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorLink;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.model.util.SensorCoWrapper;
import br.uff.labtempo.osiris.virtualsensornet.persistence.LinkDao;
import java.util.List;

/**
 *
 * @author Felipe
 */
public class NotifyController extends Controller {
    
    private final String UNIQUE_SENSOR = ControllerPath.SENSOR_BY_ID.toString();
    private final DaoFactory factory;
    
    public NotifyController(DaoFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        
        if (request.getMethod() == RequestMethod.NOTIFY) {
            if (request.getModule().contains(ControllerPath.COLLECTOR_MESSAGEGROUP.toString())) {
                
                SampleCoTo sample = request.getContent(SampleCoTo.class);
                analyzeCollector(sample);
                return new ResponseBuilder().buildNull();
                
            } else if (request.getModule().contains(ControllerPath.UPDATE_MESSAGEGROUP.toString())) {
                
                if (match(request.getResource(), UNIQUE_SENSOR)) {
                    SensorSnTo sensor = request.getContent(SensorSnTo.class);
                    analyzeSensorNet(sensor);
                    return new ResponseBuilder().buildNull();
                }
            }
        }
        return null;
    }
    
    private void analyzeCollector(SampleCoTo sample) {
        SensorCoWrapper wrapper = new SensorCoWrapper(sample);
        updateLink(wrapper);
    }
    
    private void analyzeSensorNet(SensorSnTo sensor) {
        
    }
    
    private void updateLink(SensorCoWrapper wrapper) {
        LinkDao lDao = factory.getLinkDao();
        
        String sensorId = wrapper.getSensorId();
        String networkId = wrapper.getNetworkId();
        String collectorId = wrapper.getCollectorId();
        
        List<VirtualSensorLink> links = lDao.getAll(networkId, collectorId, sensorId);
        
        for (VirtualSensorLink link : links) {
            if (link != null) {
                link.update(wrapper);
                lDao.update(link);
            }
        }       
    }
    
}
