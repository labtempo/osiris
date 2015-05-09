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
package br.uff.labtempo.osiris.sensornet.controller;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.sensornet.model.Revision;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.RevisionDao;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.to.sensornet.RevisionSnTo;
import br.uff.labtempo.osiris.utils.date.NlpDateParser;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RevisionController extends Controller {

    private final DaoFactory factory;

    public RevisionController(DaoFactory factory) {
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
        if (match(request.getResource(), Path.RESOURCE_SENSORNET_SENSOR_REVISIONS_BY_ID.toString())) {
            Map<String, String> params = extractParams(request.getResource(), Path.RESOURCE_SENSORNET_SENSOR_REVISIONS_BY_ID.toString());
            String networkId = params.get(Path.ID1.toString());
            String collectorId = params.get(Path.ID2.toString());
            String sensorId = params.get(Path.ID3.toString());
            switch (request.getMethod()) {
                case GET:
                    List<RevisionSnTo> to = get(networkId, collectorId, sensorId, params);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        }
        return null;
    }

    private List<RevisionSnTo> get(String networkId, String collectorId, String sensorId, Map<String, String> params) throws InternalServerErrorException, NotFoundException, BadRequestException {
        String from = params.get(Path.NAMING_URLPARAM_REVISIONS_FROM_DATE.toString());
        String to = params.get(Path.NAMING_URLPARAM_REVISIONS_TO_DATE.toString());
        String limit = params.get(Path.NAMING_URLPARAM_REVISIONS_LIMIT.toString());

        List<Revision> revisions = getAll(networkId, collectorId, sensorId, from, to, stringToLimit(limit));

        List<RevisionSnTo> tos = new ArrayList<>();
        for (Revision revision : revisions) {
            RevisionSnTo revisionVsnTo = revision.getTransferObject();
            tos.add(revisionVsnTo);
        }

        return tos;
    }

    private List<Revision> getAll(String networkId, String collectorId, String sensorId, String from, String to, int limit) throws InternalServerErrorException, NotFoundException, BadRequestException {
        Calendar fromDate = stringToCalendar(from);
        Calendar toDate = stringToCalendar(to);

        if (fromDate != null && toDate != null) {
            return getFromTo(networkId, collectorId, sensorId, fromDate, toDate, limit);
        } else if (fromDate == null && toDate != null) {
            return getTodayTo(networkId, collectorId, sensorId, toDate, limit);
        } else if (fromDate != null && toDate == null) {
            return getFrom(networkId, collectorId, sensorId, fromDate, limit);
        } else {
            return getToday(networkId, collectorId, sensorId, limit);
        }
    }

    private List<Revision> getFromTo(String networkId, String collectorId, String sensorId, Calendar from, Calendar to, int limit) throws InternalServerErrorException, NotFoundException {
        RevisionDao dao = getDao();
        checkVirtualSensorOrThrowsException(dao, networkId, collectorId, sensorId);
        List<Revision> revisions = dao.getFromTo(networkId, collectorId, sensorId, from, to, limit);
        return revisions;
    }

    private List<Revision> getTodayTo(String networkId, String collectorId, String sensorId, Calendar to, int limit) throws InternalServerErrorException, NotFoundException {
        RevisionDao dao = getDao();
        checkVirtualSensorOrThrowsException(dao, networkId, collectorId, sensorId);
        List<Revision> revisions = dao.getTodayTo(networkId, collectorId, sensorId, to, limit);
        return revisions;
    }

    private List<Revision> getFrom(String networkId, String collectorId, String sensorId, Calendar from, int limit) throws InternalServerErrorException, NotFoundException {
        RevisionDao dao = getDao();
        checkVirtualSensorOrThrowsException(dao, networkId, collectorId, sensorId);
        List<Revision> revisions = dao.getFrom(networkId, collectorId, sensorId, from, limit);
        return revisions;
    }

    private List<Revision> getToday(String networkId, String collectorId, String sensorId, int limit) throws NotFoundException, InternalServerErrorException {
        RevisionDao dao = getDao();
        checkVirtualSensorOrThrowsException(dao, networkId, collectorId, sensorId);
        List<Revision> revisions = dao.getToday(networkId, collectorId, sensorId, limit);
        return revisions;
    }

    private Calendar stringToCalendar(String date) {
        try {
            date.trim();
            String d = URLDecoder.decode(date, "UTF-8");
            Calendar calendar = NlpDateParser.parser(d);
            return calendar;
        } catch (Exception e) {
            return null;
        }
    }

    private int stringToLimit(String limit) {
        try {
            return Integer.valueOf(limit);
        } catch (Exception e) {
        }
        return 0;
    }

    private RevisionDao getDao() throws InternalServerErrorException {
        try {
            RevisionDao dao = factory.getRevisionDao();
            return dao;
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
    }

    private void checkVirtualSensorOrThrowsException(RevisionDao dao, String networkId, String collectorId, String sensorId) throws NotFoundException {
        if (!dao.hasVirtualSensor(networkId, collectorId, sensorId)) {
            throw new NotFoundException("Sensor not found!");
        }
    }
}
