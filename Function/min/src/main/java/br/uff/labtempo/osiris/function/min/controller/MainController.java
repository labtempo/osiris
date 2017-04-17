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
package br.uff.labtempo.osiris.function.min.controller;

import br.uff.labtempo.omcp.client.rabbitmq.RabbitClient;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import br.uff.labtempo.omcp.common.exceptions.*;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.function.min.configuration.AppConfig;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.to.common.definitions.FunctionOperation;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.function.*;

import java.net.URI;
import java.util.*;

/**
 * Controller class that extends Controller and implements the Function rules
 * @see Controller
 * @author Felipe Santos <fralph at ic.uff.br>
 * @author andre.ghigo
 * @version 1.0
 * @since 1.8
 */
public class MainController extends Controller {

    private final String RESPONSE_ERROR_MEDIATYPE = "application/json";
    private final RabbitClient client;

    public MainController(RabbitClient omcpClient) {
        this.client = omcpClient;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
        Response response = null;
        try {
            return routing(request);
        } catch (Exception e) {
            System.out.println("Error in process: " + e.getMessage());
        } finally {
            return new Response(AppConfig.PROTOCOL_VERSION, StatusCode.INTERNAL_SERVER_ERROR, Calendar.getInstance(), AppConfig.MODULE_NAME, null, 0, RESPONSE_ERROR_MEDIATYPE, null, "Failed to process the request");
        }
    }

    public Response routing(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
        String contentType = request.getContentType();
        if (match(request.getResource(), Path.RESOURCE_FUNCTION_REQUEST.toString())) {
            Map<String, String> urlParams = super.extractParams(request.getResource(), Path.RESOURCE_FUNCTION_REQUEST.toString());
            //omcp://average.function/
            switch (request.getMethod()) {
                case GET:
                    ParameterizedRequestFn prf = new ParameterizedRequestFn(urlParams);
                    RequestFnTo to = prf.getRequestFnTo();
                    ResponseFnTo responseFnTo = execute(to, request.getDate());
                    Response response = new ResponseBuilder().ok(responseFnTo, contentType).build();
                    return response;
                case POST:
                    to = request.getContent(RequestFnTo.class);
                    long id = create(to, request.getDate());
                    // /spool/{id}
                    String uri = Path.SEPARATOR.toString() + Path.NAMING_RESOURCE_SPOOL.toString() + Path.SEPARATOR + String.valueOf(id);
                    response = new ResponseBuilder().created(uri).build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        } else if (match(request.getResource(), Path.RESOURCE_FUNCTION_SPOOL.toString())) {
            //omcp://average.function/spool/
            switch (request.getMethod()) {
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        } else if (match(request.getResource(), Path.RESOURCE_FUNCTION_SPOOL_ITEM.toString())) {
            Map<String, String> map = extractParams(request.getResource(), Path.RESOURCE_FUNCTION_SPOOL_ITEM.toString());
            String urlId = map.get(Path.ID1.toString());
            //omcp://average.function/spool/{id}
            switch (request.getMethod()) {
                case DELETE:
                    long id = Long.valueOf(urlId);
                    deleteItem(id);
                    Response response = new ResponseBuilder().ok().build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        } else if (match(request.getResource(), Path.RESOURCE_FUNCTION_INTERFACE.toString())) {
            //omcp://average.function/interface/
            switch (request.getMethod()) {
                case GET:
                    InterfaceFnTo to = getInterface();
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        }
        return null;
    }

    public synchronized ResponseFnTo execute(RequestFnTo requestFnTo, Calendar calendar) throws NotFoundException, InternalServerErrorException, BadRequestException {
        List<Double> values = getValuesFromRequest(requestFnTo);
        Double total = calculate(values);
        return createResponseFnTo(total, calendar);
    }

    private double calculate(List<Double> values) throws BadRequestException {
        if(values == null || values.isEmpty()) throw new BadRequestException("Failed to calculate the minimum value: empty/null list of values");
        if(values.size() == 1) return values.get(0);
        Double min = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            if(min > values.get(i)) {
                min = values.get(i);
            }
        }
        return min;
    }

    public synchronized long create(RequestFnTo requestFnTo, Calendar calendar) throws NotFoundException, BadRequestException, InternalServerErrorException {
        String responseTo = requestFnTo.getResponseTo();
        if (responseTo == null || responseTo.isEmpty()) {
            throw new BadRequestException("Async mode needs address to response message!");
        } else {
            try {
                URI uri = new URI(responseTo);
            } catch (Exception e) {
                throw new BadRequestException("Address to response message is wrong!");
            }
        }
        List<Double> doubles = getValuesFromRequest(requestFnTo);
        Double total = calculate(doubles);
        ResponseFnTo responseFnTo = createResponseFnTo(total, calendar);

        try {
            client.doPost(responseTo, responseFnTo);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cannot response the request!");
        }

        return 0;
    }

    public InterfaceFnTo getInterface() {
        String address = getContext().getHost();
        List<FunctionOperation> operations = new ArrayList<>();
        operations.add(FunctionOperation.SYNCHRONOUS);
        operations.add(FunctionOperation.ASYNCHRONOUS);

        List<ParamFnTo> requestParamFnTos = new ArrayList<>();
        requestParamFnTos.add(new ParamFnTo(AppConfig.REQUEST_PARAM, ValueType.NUMBER, true));

        List<ParamFnTo> responseParamFnTos = new ArrayList<>();
        responseParamFnTos.add(new ParamFnTo(AppConfig.RESPONSE_PARAM, ValueType.NUMBER));

        InterfaceFnTo interfaceFnTo = new InterfaceFnTo(AppConfig.FUNCTION_NAME, AppConfig.FUNCTION_DESCRIPTION, address, operations, requestParamFnTos, responseParamFnTos);
        return interfaceFnTo;
    }

    public boolean deleteItem(long id) {
        return false;
    }

    private ResponseFnTo createResponseFnTo(Double value, Calendar calendar) {
        List<ValueFnTo> list = new ArrayList<>();
        list.add(new SingleValueFnTo(AppConfig.RESPONSE_PARAM, String.valueOf(value)));
        ResponseFnTo responseFnTo = new ResponseFnTo(calendar.getTimeInMillis(), System.currentTimeMillis(), list);
        return responseFnTo;
    }

    private List<Double> getValuesFromUriParams(Map<String, String> params) throws BadRequestException {
        final String key = AppConfig.REQUEST_PARAM;
        try {
            if (params.containsKey(key)) {
                String paramValue = params.get(key);
                if (paramValue.contains("[") && paramValue.contains("]") && paramValue.contains(",")) {
                    String[] values = paramValue.replace("[", "").replace("]", "").split(",");
                    List<String> stringValues = Arrays.asList(values);
                    List<Double> list = getDoubleValues(stringValues);
                    return list;
                }
            }
            throw new RuntimeException();
        } catch (RuntimeException re) {
            throw new BadRequestException("Params cannot be null!");
        }
    }

    private List<Double> getValuesFromRequest(RequestFnTo requestFnTo) throws BadRequestException {
        try {
            List<ValueFnTo> valueFnTos = requestFnTo.getValues();
            ValueFnTo valueFnTo = valueFnTos.get(0);
            List<String> stringValues = valueFnTo.getValues();
            List<Double> values = getDoubleValues(stringValues);
            return values;
        } catch (RuntimeException ex) {
            throw new BadRequestException("Params cannot be null!");
        }
    }

    private List<Double> getDoubleValues(List<String> values) {
        List<Double> list = new ArrayList<>();
        for (String value : values) {
            list.add(Double.valueOf(value.trim()));
        }
        return list;
    }
}
