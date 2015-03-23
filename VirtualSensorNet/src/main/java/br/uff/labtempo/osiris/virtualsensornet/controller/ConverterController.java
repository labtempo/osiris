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
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.DataConverter;
import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.persistence.ConverterDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ConverterController extends Controller {

    private final DaoFactory factory;

    public ConverterController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
        try {
            return routing(request);
        } finally {
            factory.clear();
        }
    }

    public Response routing(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
        String contentType = request.getContentType();
        if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_CONVERTER_ALL.toString())) {
            switch (request.getMethod()) {
                case GET:
                    List<ConverterVsnTo> all = getAll();
                    Response response = new ResponseBuilder().ok(all, contentType).build();
                    return response;
                case POST:
                    ConverterVsnTo to = request.getContent(ConverterVsnTo.class);
                    long id = create(to);
                    // /converter/{id}
                    String uri = Path.SEPARATOR.toString() + Path.NAMING_RESOURCE_CONVERTER.toString() + Path.SEPARATOR + String.valueOf(id);
                    response = new ResponseBuilder().created(uri).build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        } else if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_CONVERTER_BY_ID.toString())) {
            Map<String, String> map = extract(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_CONVERTER_BY_ID.toString());
            String urlId = map.get(Path.ID1.toString());
            switch (request.getMethod()) {
                case GET:
                    long id = Long.valueOf(urlId);
                    ConverterVsnTo to = get(id);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                case PUT:
                    id = Long.valueOf(urlId);
                    to = request.getContent(ConverterVsnTo.class);
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

    public ConverterVsnTo get(long id) throws NotFoundException, InternalServerErrorException {
        ConverterDao cDao;
        try {
            cDao = factory.getConverterDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        DataConverter dataConverter = cDao.getById(id);
        if (dataConverter == null) {
            throw new NotFoundException("Selected Converter not found!");
        }
        long usedBy = cDao.countUseInField(dataConverter);
        return dataConverter.getTransferObject(usedBy);
    }

    public List<ConverterVsnTo> getAll() throws InternalServerErrorException {
        ConverterDao cDao;
        try {
            cDao = factory.getConverterDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        List<DataConverter> converters = cDao.getAll();
        List<ConverterVsnTo> cvts = new ArrayList<>();
        for (DataConverter vs : converters) {
            long usedBy = cDao.countUseInField(vs);
            cvts.add(vs.getTransferObject(usedBy));
        }
        return cvts;
    }

    public synchronized long create(ConverterVsnTo converterTo) throws NotFoundException, BadRequestException, InternalServerErrorException {
        long inputDataTypeId;
        long outputDataTypeId;
        String displayName;
        String expression;

        try {
            inputDataTypeId = converterTo.getInputDataTypeId();
            outputDataTypeId = converterTo.getOutputDataTypeId();
            displayName = converterTo.getDisplayName().trim();
            expression = converterTo.getExpression().trim();
        } catch (NullPointerException e) {
            throw new BadRequestException("Params cannot be null!");
        }

        DataTypeDao dtDao;
        ConverterDao cDao;
        DataType inputDataType;
        DataType outputDataType;

        try {
            dtDao = factory.getDataTypeDao();
            cDao = factory.getConverterDao();
            inputDataType = dtDao.getById(inputDataTypeId);
            outputDataType = dtDao.getById(outputDataTypeId);
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        if (inputDataType == null || outputDataType == null) {
            throw new BadRequestException("Selected DataType not found!");
        }

        DataConverter converter;
        try {
            converter = new DataConverter(displayName, expression, inputDataType, outputDataType);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        cDao.save(converter);
        return converter.getId();
    }

    public synchronized boolean update(long id, ConverterVsnTo converterTo) throws NotFoundException, BadRequestException, InternalServerErrorException {
        long inputDataTypeId;
        long outputDataTypeId;
        String displayName;
        String expression;

        try {
            inputDataTypeId = converterTo.getInputDataTypeId();
            outputDataTypeId = converterTo.getOutputDataTypeId();
            displayName = converterTo.getDisplayName().trim();
            expression = converterTo.getExpression().trim();
        } catch (NullPointerException e) {
            throw new BadRequestException("Params cannot be null!");
        }

        DataTypeDao dtDao;
        ConverterDao cDao;

        try {
            dtDao = factory.getDataTypeDao();
            cDao = factory.getConverterDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        DataConverter converter = cDao.getById(id);

        if (converter == null) {
            throw new BadRequestException("Selected Converter not found!");
        }

        if (converter.getInputDataType().getId() != inputDataTypeId || converter.getOutputDataType().getId() != outputDataTypeId) {
            //change datatypes only if converter is not used
            long count = cDao.countUseInField(converter);
            if (count > 0) {
                //throw new BadRequestException("Converter is in using by one or more Fields. It cannot be deleted!");
                throw new BadRequestException("Converter is in using by one or more Fields. It's locked to change critical properties!");
            }

            if (converter.getInputDataType().getId() != inputDataTypeId) {
                DataType dataType = dtDao.getById(inputDataTypeId);
                if (dataType == null) {
                    throw new BadRequestException("Selected InputDataType not found!");
                }
                converter.setInputDataType(dataType);
            }
            if (converter.getOutputDataType().getId() != outputDataTypeId) {
                DataType dataType = dtDao.getById(outputDataTypeId);
                if (dataType == null) {
                    throw new BadRequestException("Selected OutputDataType not found!");
                }
                converter.setOutputDataType(dataType);
            }
        }

        converter.setExpression(expression);
        converter.setDisplayName(displayName);
        cDao.update(converter);

        return true;
    }

    public boolean delete(long id) throws NotFoundException, InternalServerErrorException, BadRequestException {
        ConverterDao cDao;
        try {
            cDao = factory.getConverterDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        DataConverter converter = cDao.getById(id);
        if (converter == null) {
            throw new NotFoundException("Selected Converter not found!");
        }

        long count = cDao.countUseInField(converter);
        if (count > 0) {
            throw new BadRequestException("Converter is in using by one or more Fields. It cannot be deleted!");
        }

        try {
            cDao.delete(converter);
            return true;
        } catch (Exception e) {
            throw new InternalServerErrorException("Converter couldn't removed!");
        }
    }
}
