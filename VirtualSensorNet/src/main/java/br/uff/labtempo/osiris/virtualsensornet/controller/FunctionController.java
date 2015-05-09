/*
 * Copyright 2015 Felipe Santos <feliperalph at hotmail.com>.
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
import br.uff.labtempo.osiris.to.common.definitions.FunctionOperation;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.to.function.InterfaceFnTo;
import br.uff.labtempo.osiris.to.function.ParamFnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.FunctionVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.Function;
import br.uff.labtempo.osiris.virtualsensornet.model.FunctionParam;
import br.uff.labtempo.osiris.virtualsensornet.model.util.FunctionUtils;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.FunctionDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class FunctionController extends Controller {

    private final DaoFactory factory;

    public FunctionController(DaoFactory factory) {
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
        if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_FUNCTION_ALL.toString())) {
            switch (request.getMethod()) {
                case GET:
                    List<FunctionVsnTo> all = getAll();
                    Response response = new ResponseBuilder().ok(all, contentType).build();
                    return response;
                case POST:
                    InterfaceFnTo to = request.getContent(InterfaceFnTo.class);
                    long id = create(to);
                    // /function/{id}
                    String uri = Path.SEPARATOR.toString() + Path.NAMING_RESOURCE_FUNCTION.toString() + Path.SEPARATOR + String.valueOf(id);
                    response = new ResponseBuilder().created(uri).build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        } else if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_FUNCTION_BY_ID.toString())) {
            Map<String, String> map = extractParams(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_FUNCTION_BY_ID.toString());
            String urlId = map.get(Path.ID1.toString());
            switch (request.getMethod()) {
                case GET:
                    long id = Long.valueOf(urlId);
                    FunctionVsnTo to = get(id);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                case PUT:
                    id = Long.valueOf(urlId);
                    to = request.getContent(InterfaceFnTo.class);
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

    public FunctionVsnTo get(long id) throws NotFoundException, InternalServerErrorException {
        FunctionDao functionDao;
        try {
            functionDao = factory.getFunctionDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        Function function = functionDao.getById(id);
        if (function == null) {
            throw new NotFoundException("Function not found!");
        }
        FunctionVsnTo to = function.getTransferObject();
        return to;
    }

    public List<FunctionVsnTo> getAll() throws InternalServerErrorException {
        FunctionDao functionDao;
        try {
            functionDao = factory.getFunctionDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        List<Function> functions = functionDao.getAll();
        List<FunctionVsnTo> functionFnTos = new ArrayList<>();
        for (Function vs : functions) {
            functionFnTos.add(vs.getTransferObject());
        }
        return functionFnTos;
    }

    public synchronized long create(InterfaceFnTo interfaceFnTo) throws InternalServerErrorException, BadRequestException {
        String name;
        String description;
        String address;
        List<FunctionOperation> operations;
        List<ParamFnTo> requestParams;
        List<ParamFnTo> responseParams;

        try {
            name = interfaceFnTo.getName().trim();
            description = interfaceFnTo.getDescription();
            address = interfaceFnTo.getAddress().trim();
            operations = interfaceFnTo.getOperations();
            requestParams = interfaceFnTo.getRequestParams();
            responseParams = interfaceFnTo.getResponseParams();
            //check nullpointer           
            operations.size();
            requestParams.size();
            responseParams.size();
        } catch (Exception e) {
            throw new BadRequestException("Params cannot be null!");
        }

        if (operations.isEmpty() || responseParams.isEmpty()) {
            throw new BadRequestException("Declared Fields cannot be empty!");
        }

        FunctionUtils utils = new FunctionUtils();
        List<FunctionParam> requestFunctionParams = utils.convertTo(requestParams);
        List<FunctionParam> responseFunctionParams = utils.convertTo(responseParams);

        Function function = new Function(name, description, address, requestFunctionParams, responseFunctionParams, operations);

        FunctionDao functionDao;
        try {
            functionDao = factory.getFunctionDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        functionDao.save(function);
        return function.getId();
    }

    public synchronized boolean update(long id, InterfaceFnTo interfaceFnTo) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, BadRequestException {
        FunctionDao functionDao;
        try {
            functionDao = factory.getFunctionDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        Function function = functionDao.getById(id);
        if (function == null) {
            throw new NotFoundException("Selected Function not found!");
        }

        long countUse = functionDao.countUse(function);
        boolean isUpdated = false;

        String name;
        String description;
        String address;
        List<FunctionOperation> operations;
        List<ParamFnTo> requestParams;
        List<ParamFnTo> responseParams;

        try {
            name = interfaceFnTo.getName().trim();
            description = interfaceFnTo.getDescription();
            address = interfaceFnTo.getAddress().trim();
            operations = interfaceFnTo.getOperations();
            requestParams = interfaceFnTo.getRequestParams();
            responseParams = interfaceFnTo.getResponseParams();
            //check nullpointer           
            operations.size();
            requestParams.size();
            responseParams.size();
        } catch (Exception e) {
            throw new BadRequestException("Params cannot be null!");
        }

        if (operations.isEmpty() || responseParams.isEmpty()) {
            throw new BadRequestException("Declared Fields cannot be empty!");
        }

        //extract lists
        FunctionUtils utils = new FunctionUtils();
        List<FunctionParam> requestFunctionParams = utils.convertTo(requestParams);
        List<FunctionParam> responseFunctionParams = utils.convertTo(responseParams);

        //update name
        if (!function.getName().equals(name)) {
            function.setName(name);
            isUpdated = true;
        }

        //update description
        if (!function.getDescription().equals(description)) {
            function.setDescription(description);
            isUpdated = true;
        }

        //update address
        if (!function.getAddress().equals(address)) {
            function.setAddress(address);
            isUpdated = true;
        }

        //update function operation
        if (!function.getOperations().equals(operations)) {
            function.setOperations(operations);
            isUpdated = true;
        }

        //update request params
        if (!function.getRequestParams().equals(requestFunctionParams)) {
            if (countUse > 0) {
                throw new BadRequestException("Function is in using by one or more Blendings. It cannot be deleted!");
            }
            function.setRequestParams(requestFunctionParams);
            isUpdated = true;
        }

        //update response params
        if (!function.getResponseParams().equals(responseFunctionParams)) {
            if (countUse > 0) {
                throw new BadRequestException("Function is in using by one or more Blendings. It cannot be deleted!");
            }
            function.setRequestParams(responseFunctionParams);
            isUpdated = true;
        }

        //commit changes
        if (isUpdated) {
            //persist changes
            functionDao.update(function);
        }
        return isUpdated;
    }

    public boolean delete(long id) throws NotFoundException, InternalServerErrorException, BadRequestException {
        FunctionDao functionDao;
        try {
            functionDao = factory.getFunctionDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        Function function = functionDao.getById(id);
        if (function == null) {
            throw new NotFoundException("Selected Function not found!");
        }

        long countUse = functionDao.countUse(function);
        if (countUse > 0) {
            throw new BadRequestException("Function is in using by one or more Blendings. It cannot be deleted!");
        }

        try {
            functionDao.delete(function);
            return true;
        } catch (Exception e) {
            throw new InternalServerErrorException("Function couldn't removed!");
        }
    }
}
