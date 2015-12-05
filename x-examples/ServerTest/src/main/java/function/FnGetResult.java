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
package function;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import br.uff.labtempo.osiris.to.common.definitions.FunctionOperation;
import br.uff.labtempo.osiris.to.function.InterfaceFnTo;
import br.uff.labtempo.osiris.to.function.ParamFnTo;
import br.uff.labtempo.osiris.to.function.ParameterizedRequestFn;
import br.uff.labtempo.osiris.to.function.RequestFnTo;
import br.uff.labtempo.osiris.to.function.ResponseFnTo;
import br.uff.labtempo.osiris.to.function.ValueFnTo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class FnGetResult {

    public static void main(String[] args) throws Exception {
        try (OmcpClient client = new OmcpClientBuilder().host("192.168.0.7").user("admin", "admin").source("generico").build()) {
            deleteAll(client);
        };
    }

    public static void deleteAll(OmcpClient client) {
        String uri = "omcp://sum.function/";
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        RequestFnTo requestFnTo = new RequestFnTo();
        requestFnTo.addValue("input", list);
        requestFnTo.addValue("input2", list);

        ParameterizedRequestFn requestFn = new ParameterizedRequestFn(requestFnTo);

        String address = requestFn.getRequestUri(uri);

        Response r = client.doGet(address);
        if (r.getStatusCode() == StatusCode.OK) {
            ResponseFnTo to = r.getContent(ResponseFnTo.class);
            System.out.println("requisição: " + to.getRequestTimestampInMillis());
            System.out.println("resposta: " + to.getResponseTimestampInMillis());

            ValueFnTo valueFnTo = to.getValues().get(0);
            System.out.println("nome do campo: " + valueFnTo.getName());
            System.out.println("valor do campo: " + valueFnTo.getValue());
        } else {
            throw new RuntimeException(r.getStatusCode().toString());
        }
    }
}
