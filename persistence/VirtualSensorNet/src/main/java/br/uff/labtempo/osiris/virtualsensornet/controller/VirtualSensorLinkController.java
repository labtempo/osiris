/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.controller;

import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.virtualsensornet.model.Converter;
import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorLink;
import br.uff.labtempo.osiris.virtualsensornet.persistence.ConverterDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.LinkDao;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe
 */
public class VirtualSensorLinkController extends Controller {

    private final DaoFactory factory;

    public VirtualSensorLinkController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void create(LinkVsnTo linkTo) {
        String sensorId = linkTo.getSensorId();
        String collectorId = linkTo.getCollectorId();
        String networkId = linkTo.getNetworkId();

        List<LinkVsnTo.FieldTo> fieldsTo = linkTo.getFields();
        List<Field> fields = getFields(fieldsTo);
        //TODO: nao pode field com tamanho 0
        VirtualSensorLink link = new VirtualSensorLink(networkId, collectorId, sensorId, fields);
        
        LinkDao lDao = factory.getLinkDao();
        lDao.save(link);

    }

    private List<Field> getFields(List<LinkVsnTo.FieldTo> fieldsTo) {
        DataTypeDao dtDao = factory.getDataTypeDao();
        ConverterDao cDao = factory.getConverterDao();
        List<Field> list = new ArrayList<>();
        for (LinkVsnTo.FieldTo fieldTo : fieldsTo) {

            DataType dataType = dtDao.get(fieldTo.getDataTypeId());
            Converter converter = cDao.get(fieldTo.getConverterId());

            if (dataType != null) {
                Field field = new Field(fieldTo.getName(), dataType);
                if (converter != null) {
                    field.setConverter(converter);
                }
                list.add(field);
            } else {
                throw new RuntimeException("Selected DataType not found!");
            }

        }
        return list;
    }
}
