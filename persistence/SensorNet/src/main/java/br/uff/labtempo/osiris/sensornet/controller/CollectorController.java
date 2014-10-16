/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.controller;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.RequestMethod;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.collector.Collector;
import br.uff.labtempo.osiris.sensornet.model.CollectorWrapper;
import br.uff.labtempo.osiris.sensornet.model.NetworkWrapper;
import br.uff.labtempo.osiris.sensornet.persistence.CollectorDao;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class CollectorController extends Controller {
    
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        
        String networkId = null;
        String collectorId = null;
        
        String pathA ="/:network/collectors/";
        if(match(request.getResource(),pathA)){
            Map<String,String> map = extract(request.getResource(),pathA);
            networkId = map.get(":network");
            
            if(request.getMethod() != RequestMethod.GET)
                throw getNotImplementedException("Action not implemented");
            
            return getAll(networkId); 
        }
        
        String pathB = "/:network/collectors/:collector/";
        if(match(request.getResource(),pathB)){
            Map<String,String> map = extract(request.getResource(),pathB);
            networkId = map.get(":network");
            collectorId = map.get(":collector");
            
            if(request.getMethod() != RequestMethod.GET)
                throw getNotImplementedException("Action not implemented");
            
            return getById(networkId,collectorId);
        }
        return goToNext(request);
    }

    private Response getAll(String networkId) throws NotFoundException {
        CollectorDao cdao = new CollectorDao();
        NetworkDao ndao = new NetworkDao();        
        
        NetworkWrapper nw = ndao.getById(networkId);
        
        if(nw == null)
            throw getNotFoundException("Network not exists");
        
        List<CollectorWrapper> list = cdao.getAll(nw);

        List<Collector> collectors = new ArrayList<>();

        for (CollectorWrapper cw : list) {
            collectors.add(cw.getContent());
        }
        
        Response response = new ResponseBuilder().ok(super.toJson(collectors)).build();
        return response;
    }

    private Response getById(String networkId, String collectorId) throws NotFoundException {
        NetworkDao ndao = new NetworkDao();        
        
        NetworkWrapper nw = ndao.getById(networkId);
        
        if(nw == null)
            throw getNotFoundException("Network not exists");
        
        CollectorDao cdao = new CollectorDao();

        CollectorWrapper cw = cdao.getById(collectorId,nw);
              
        Response response = new ResponseBuilder().ok(super.toJson(cw.getContent())).build();
        return response;
    }
    
    private NotFoundException getNotFoundException(String msg){
        return new NotFoundException(msg);
    }
    private NotImplementedException getNotImplementedException(String msg){
        return new NotImplementedException(msg);
    }
}
