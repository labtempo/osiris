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
package br.uff.labtempo.osiris.omcp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
final class HandlerTools {

    private HandlerTools() {
    }

    /**
     * transformar para regex acima method params
     */
    private static String createRegex(String value) {
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

    public static boolean match(String resource, String customRegex) {
        if (resource != null) {
            if (resource.contains("?")) {
                resource = resource.split("\\?")[0];
            }
            return resource.matches(createRegex(customRegex));
        }
        return false;
    }

    public static Map<String, String> extractParams(String path, String customRegex) {
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

    private static List<String> getCustomKeys(String customRegex) {
        String regex = ":[\\w]+[^/]?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(customRegex);

        List<String> customKeys = new ArrayList<>();

        while (matcher.find()) {
            customKeys.add(matcher.group(0));
        }
        return customKeys;
    }

    private static Map<String, String> getParams(String path) {
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
}
