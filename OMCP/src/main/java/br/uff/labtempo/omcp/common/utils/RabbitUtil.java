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
package br.uff.labtempo.omcp.common.utils;

import br.uff.labtempo.omcp.common.Request;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RabbitUtil {

    /**
     * Method to parse url's name format to rabbitmq's queue name
     *
     */
    public static String getHostAddress(URI uri, final String DOMAIN) {
        return getHostAddress(uri.getHost(), DOMAIN);
    }

    public static String getHostAddress(Request request, final String DOMAIN) {
        return getHostAddress(request.getModule(), DOMAIN);
    }

    public static String getHostAddress(String host, final String DOMAIN) {
        String queue = host;
        if (!queue.contains(DOMAIN)) {
            queue = host + "." + DOMAIN;
        }

        List<String> items = new ArrayList<>();
        items.addAll(Arrays.<String>asList(queue.split("\\.")));
        Collections.reverse(items);

        StringBuilder sb = new StringBuilder();
        sb.append(items.remove(0));

        for (String item : items) {
            sb.append(".");
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * Method to parse url's name format and extract routingKey
     *
     */
    public static String getRoutingKey(URI uri) {
        String url = uri.toString();
        String path = uri.getPath();
        if (url.contains("#")) {
            path = url.split(uri.getHost())[1];
        }
        return getRoutingKey(path);
    }

    public static String getRoutingKey(Request request) {
        return getRoutingKey(request.getResource());
    }

    public static String getRoutingKey(String resource) {
        String routingKey = resource;
        if (routingKey.length() <= 1) {
            return "";
        }

        while (routingKey.startsWith("/")) {
            routingKey = routingKey.substring(1);
        }

        while (routingKey.endsWith("/")) {
            routingKey = routingKey.substring(0, routingKey.length() - 1);
        }

        List<String> items = new ArrayList<>();
        items.addAll(Arrays.<String>asList(routingKey.split("/")));

        //reverse routing keys
        //Collections.reverse(items);
        StringBuilder sb = new StringBuilder();
        sb.append(items.remove(0));

        for (String item : items) {
            sb.append(".");
            sb.append(item);
        }
        return sb.toString();
    }

    public static AMQP.Exchange.DeclareOk declareExchange(Channel channel, String exchangeName) throws IOException {
        return channel.exchangeDeclare(exchangeName, "topic", true);
    }

    public static AMQP.Queue.DeclareOk declareDurableQueue(Channel channel, String queueName) throws IOException {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 60000);
        return channel.queueDeclare(queueName, true, false, false, args);
    }

    public static AMQP.Queue.DeclareOk declareTemporaryQueue(Channel channel) throws IOException {
        return channel.queueDeclare();
    }
}
