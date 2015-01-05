/* 
 * Copyright 2015 Felipe Santos <fralph at ic.uff.br>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import br.uff.labtempo.osiris.to.common.data.FieldTo;
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
 * @author Felipe Santos <fralph at ic.uff.br>
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

        List<? extends FieldTo> fieldsTo = linkTo.getFields();
        List<Field> fields = getFields(fieldsTo);
        //not able to fields with no entries
        if (fields.size() > 0) {
            VirtualSensorLink link = new VirtualSensorLink(networkId, collectorId, sensorId, fields);
            LinkDao lDao = factory.getLinkDao();
            lDao.save(link);
        }
    }

    private List<Field> getFields(List<? extends FieldTo> fieldsTo) {
        DataTypeDao dtDao = factory.getDataTypeDao();
        ConverterDao cDao = factory.getConverterDao();
        List<Field> list = new ArrayList<>();
        for (FieldTo fieldTo : fieldsTo) {

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
