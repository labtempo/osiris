
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.omcp;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.omcp.server.Context;
import br.uff.labtempo.omcp.server.RequestHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Felipe
 */
public abstract class Controller implements RequestHandler {

    private Controller nextController;
    private Context context;

    public void setNext(Controller controller) {
        this.nextController = controller;
        controller.setContext(context);
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public abstract Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException;

    @Override
    public Response handler(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        Response response = process(request);
        if (response == null) {
            return goToNext(request);
        }
        return response;
    }

    protected Response goToNext(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        if (nextController != null) {
            return nextController.handler(request);
        }
        throw new NotFoundException(request.getResource() + " not found!");
    }

    /**
     * transformar para regex acima method params
     */
    private String createRegex(String value) {
        String[] resources = value.split("/");
        StringBuilder sb = new StringBuilder();
        sb.append("^");
        for (String resource : resources) {
            if (resource.length() == 0) {
                continue;
            }
            sb.append("/");
            if (resource.contains(":")) {
                sb.append("(.[^/\\?]*)");
            } else {
                sb.append(resource);
            }
        }
        sb.append("/?$?[\\?.]*?");
        return sb.toString();
    }

    protected boolean match(String resource, String customRegex) {
        if (resource != null) {
            return resource.matches(createRegex(customRegex));
        }
        return false;
    }

    protected Map<String, String> extract(String path, String customRegex) {
        Pattern datePatt = Pattern.compile(createRegex(customRegex));
        Matcher m = datePatt.matcher(path);

        List<String> args = new ArrayList<>();

        while (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                args.add(m.group(i));
            }
        }

        List<String> cusromKeys = getCustomKeys(customRegex);
        Map<String, String> map = new HashMap<>();
        if (cusromKeys.size() == args.size()) {
            for (int i = 0; i < cusromKeys.size(); i++) {
                map.put(cusromKeys.get(i), args.get(i));
            }
        }

        Map<String, String> params = getParams(path);

        if (params != null) {
            map.putAll(params);
        }

        return map;
    }

    private List<String> getCustomKeys(String customRegex) {
        String regex = ":[\\w]+[^/]?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(customRegex);

        List<String> customKeys = new ArrayList<>();

        while (matcher.find()) {
            customKeys.add(matcher.group(0));
        }
        return customKeys;
    }

    private Map<String, String> getParams(String path) {
        if (path != null && path.contains("?")) {
            Map<String, String> map = new HashMap<>();

            String query = (path.split("\\?"))[1];
            String[] params = query.split("&");

            for (String param : params) {
                String[] keyValue = param.split("=");
                map.put(keyValue[0], keyValue[1]);
            }
            return map;
        }
        return null;
    }

    protected Response builder(Object obj, String contentType) {
        Response response;
        if (contentType == null) {
            response = new ResponseBuilder().ok(obj, ResponseBuilder.ContentType.JSON).build();
        } else {
            response = new ResponseBuilder().ok(obj, contentType).build();
        }
        return response;

    }
}
