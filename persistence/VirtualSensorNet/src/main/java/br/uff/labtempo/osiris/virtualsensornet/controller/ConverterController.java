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
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.Converter;
import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.persistence.ConverterDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;

/**
 *
 * @author Felipe
 */
public class ConverterController extends Controller {

    private final DaoFactory factory;

    public ConverterController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void create(ConverterVsnTo converterTo) {
        DataTypeDao dtDao = factory.getDataTypeDao();
        ConverterDao cDao = factory.getConverterDao();

        long dataTypeId = converterTo.getDataTypeId();
        String name = converterTo.getName();
        String expression = converterTo.getExpression();

        DataType dataType = dtDao.get(dataTypeId);

        if (dataType != null) {
            Converter converter = new Converter(name, dataType, expression);
            cDao.save(converter);
        } else {
            throw new RuntimeException("Selected DataType not found!");
        }

    }
}
