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
import br.uff.labtempo.osiris.to.virtualsensornet.RevisionVsnTo;
import br.uff.labtempo.osiris.utils.date.NlpDateParser;
import br.uff.labtempo.osiris.virtualsensornet.model.Revision;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.RevisionDao;
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
        if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_REVISIONS_BY_ID.toString())) {
            Map<String, String> params = extractParams(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_REVISIONS_BY_ID.toString());
            String urlId = params.get(Path.ID1.toString());
            switch (request.getMethod()) {
                case GET:
                    long id = Long.valueOf(urlId);
                    List<RevisionVsnTo> to = get(id, params);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        }
        return null;
    }

    private List<RevisionVsnTo> get(long id, Map<String, String> params) throws InternalServerErrorException, NotFoundException, BadRequestException {
        String from = params.get(Path.NAMING_URLPARAM_REVISIONS_FROM_DATE.toString());
        String to = params.get(Path.NAMING_URLPARAM_REVISIONS_TO_DATE.toString());
        String limit = params.get(Path.NAMING_URLPARAM_REVISIONS_LIMIT.toString());

        List<Revision> revisions = getAll(id, from, to, stringToLimit(limit));

        List<RevisionVsnTo> tos = new ArrayList<>();
        for (Revision revision : revisions) {
            RevisionVsnTo revisionVsnTo = revision.getTransferObject();
            tos.add(revisionVsnTo);
        }

        return tos;
    }

    private List<Revision> getAll(long id, String from, String to, int limit) throws InternalServerErrorException, NotFoundException, BadRequestException {
        Calendar fromDate = stringToCalendar(from);
        Calendar toDate = stringToCalendar(to);

        if (fromDate != null && toDate != null) {
            return getFromTo(id, fromDate, toDate, limit);
        } else if (fromDate == null && toDate != null) {
            return getTodayTo(id, toDate, limit);
        } else if (fromDate != null && toDate == null) {
            return getFrom(id, fromDate, limit);
        } else {
            return getToday(id, limit);
        }
    }

    private List<Revision> getFromTo(long id, Calendar from, Calendar to, int limit) throws InternalServerErrorException, NotFoundException {
        RevisionDao dao = getDao();
        checkVirtualSensorOrThrowsException(dao, id);
        List<Revision> revisions = dao.getFromTo(id, from, to, limit);
        return revisions;
    }

    private List<Revision> getTodayTo(long id, Calendar to, int limit) throws InternalServerErrorException, NotFoundException {
        RevisionDao dao = getDao();
        checkVirtualSensorOrThrowsException(dao, id);
        List<Revision> revisions = dao.getTodayTo(id, to, limit);
        return revisions;
    }

    private List<Revision> getFrom(long id, Calendar from, int limit) throws InternalServerErrorException, NotFoundException {
        RevisionDao dao = getDao();
        checkVirtualSensorOrThrowsException(dao, id);
        List<Revision> revisions = dao.getFrom(id, from, limit);
        return revisions;
    }

    private List<Revision> getToday(long id, int limit) throws NotFoundException, InternalServerErrorException {
        RevisionDao dao = getDao();
        checkVirtualSensorOrThrowsException(dao, id);
        List<Revision> revisions = dao.getToday(id, limit);
        return revisions;
    }

    private Calendar stringToCalendar(String date){
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

    private void checkVirtualSensorOrThrowsException(RevisionDao dao, long sensorId) throws NotFoundException {
        if (!dao.hasVirtualSensor(sensorId)) {
            throw new NotFoundException("VirtualSensor not found!");
        }
    }
}
