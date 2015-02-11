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

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class DataTypeController extends Controller {

    private final DaoFactory factory;

    public DataTypeController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
        String contentType = request.getContentType();
        if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_DATATYPE_ALL.toString())) {
            switch (request.getMethod()) {
                case GET:
                    List<DataTypeVsnTo> all = getAll();
                    Response response = new ResponseBuilder().ok(all, contentType).build();
                    return response;
                case POST:
                    DataTypeVsnTo to = request.getContent(DataTypeVsnTo.class);
                    long id = create(to);
                    // /datatype/{id}
                    String uri = Path.SEPARATOR.toString() +Path.NAMING_RESOURCE_DATATYPE.toString() + Path.SEPARATOR + String.valueOf(id);
                    response = new ResponseBuilder().created(uri).build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        } else if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_DATATYPE_BY_ID.toString())) {
            Map<String, String> map = extract(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_DATATYPE_BY_ID.toString());
            String urlId = map.get(Path.ID1.toString());
            switch (request.getMethod()) {
                case GET:
                    long id = Long.valueOf(urlId);
                    DataTypeVsnTo to = get(id);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                case PUT:
                    id = Long.valueOf(urlId);
                    to = request.getContent(DataTypeVsnTo.class);
                    update(id, to);
                    response = new ResponseBuilder().ok().build();
                    return response;
                case DELETE:
                    id = Long.valueOf(urlId);
                    delete(id);
                    response = new ResponseBuilder().ok().build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        }
        return null;
    }

    public DataTypeVsnTo get(long id) throws NotFoundException, InternalServerErrorException {
        DataTypeDao dDao;
        try {
            dDao = factory.getDataTypeDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        DataType dataType = dDao.getById(id);
        if (dataType == null) {
            throw new NotFoundException("Selected DataType not found!");
        }
        long usedBy = dDao.countIndirectUseInFields(dataType);
        return dataType.getTransferObject(usedBy);
    }

    public List<DataTypeVsnTo> getAll() throws InternalServerErrorException {
        DataTypeDao dDao = null;
        try {
            dDao = factory.getDataTypeDao();
        } catch (Exception e) {
            new InternalServerErrorException("Data query error!");
        }

        List<DataType> dataTypes = dDao.getAll();
        List<DataTypeVsnTo> dtvts = new ArrayList<>();
        for (DataType vs : dataTypes) {
            long usedBy = dDao.countIndirectUseInFields(vs);
            dtvts.add(vs.getTransferObject(usedBy));
        }
        return dtvts;
    }

    public synchronized boolean update(long id, DataTypeVsnTo dataTypeTo) throws MethodNotAllowedException, NotFoundException, BadRequestException, InternalServerErrorException {
        String displayName;
        String unit;
        String symbol;
        ValueType valueType;
        try {
            displayName = dataTypeTo.getDisplayName().trim();
            unit = dataTypeTo.getUnit().trim();
            symbol = dataTypeTo.getSymbol().trim();
            valueType = dataTypeTo.getType();

            DataType tempDataType = new DataType(displayName, valueType, unit, symbol);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

        DataTypeDao dDao;
        try {
            dDao = factory.getDataTypeDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        DataType dataType = dDao.getById(id);

        if (dataType == null) {
            throw new BadRequestException("Selected DataType not found!");
        }

        if (!dataType.getSymbol().equals(symbol) || !dataType.getValueType().equals(valueType) || !dataType.getUnit().equals(unit)) {
            //change datatypes only if it is not used field, direct or indirectly
            long countUseInFields = dDao.countIndirectUseInFields(dataType);
            if (countUseInFields > 0) {
                throw new BadRequestException("DataType is in using by one or more Fields, directly(DataType) or indirectly(Converter). It's locked to change critical properties!");
            }
            if (valueType != null && unit != null && symbol != null) {
                dataType.setSymbol(symbol);
                dataType.setType(valueType);
                dataType.setUnit(unit);
            }
        }
        dataType.setDisplayName(displayName);
        dDao.update(dataType);
        return true;
    }

    public synchronized long create(DataTypeVsnTo dataTypeTo) throws BadRequestException, InternalServerErrorException {
        DataType dataType;
        try {
            String displayName = dataTypeTo.getDisplayName().trim();
            ValueType type = dataTypeTo.getType();
            String unit = dataTypeTo.getUnit().trim();
            String symbol = dataTypeTo.getSymbol().trim();
            dataType = new DataType(displayName, type, unit, symbol);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

        DataTypeDao dDao;
        try {
            dDao = factory.getDataTypeDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
        dDao.save(dataType);

        return dataType.getId();
    }

    public boolean delete(long id) throws NotFoundException, InternalServerErrorException, BadRequestException {
        DataTypeDao dDao;
        try {
            dDao = factory.getDataTypeDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        DataType dataType = dDao.getById(id);
        if (dataType == null) {
            throw new NotFoundException("Selected DataType not found!");
        }

        long countUseInFields = dDao.countIndirectUseInFields(dataType);
        if (countUseInFields > 0) {
            throw new BadRequestException("DataType is in using by one or more Fields, directly(DataType) or indirectly(Converter). It cannot be deleted!");
        }

        try {
            dDao.delete(dataType);
            return true;
        } catch (Exception e) {
            throw new InternalServerErrorException("DataType couldn't removed!");
        }
    }
}
