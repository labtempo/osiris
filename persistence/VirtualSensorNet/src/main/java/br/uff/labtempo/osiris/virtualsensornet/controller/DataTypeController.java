/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.controller;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;

/**
 *
 * @author Felipe
 */
public class DataTypeController extends Controller {

    private final DaoFactory factory;

    public DataTypeController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void create(DataTypeVsnTo dataTypeTo) {
        String displayName = dataTypeTo.getDisplayName();
        String type = dataTypeTo.getType();
        String unit = dataTypeTo.getUnit();
        String symbol = dataTypeTo.getSymbol();

        DataType dataType = new DataType(type, unit, symbol);
        dataType.setDisplayName(displayName);
        
        factory.getDataTypeDao().save(dataType);
    }
}
