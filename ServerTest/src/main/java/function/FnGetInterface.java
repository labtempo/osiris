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
import br.uff.labtempo.osiris.to.virtualsensornet.BlendingVsnTo;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class FnGetInterface {

    public static void main(String[] args) throws Exception {
        try (OmcpClient client = new OmcpClientBuilder().host("192.168.0.7").user("admin", "admin").source("generico").build()) {
            deleteAll(client);
        };
    }

    public static void deleteAll(OmcpClient client) {
        String address = "omcp://sum.function/interface/";
        Response r = client.doGet(address);
        if (r.getStatusCode() == StatusCode.OK) {
            InterfaceFnTo to = r.getContent(InterfaceFnTo.class);
            System.out.println("name: " + to.getName());
            System.out.println("description: " + to.getDescription());
            System.out.println("address: " + to.getAddress());

            //get operations
            List<FunctionOperation> operations = to.getOperations();
            StringBuilder sb = new StringBuilder();
            for (FunctionOperation operation : operations) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(operation.toString());
            }
            System.out.println("operations: " + sb.toString());

            List<ParamFnTo> requestFnTos = to.getRequestParams();
            if (requestFnTos.size() > 0) {
                System.out.println("Request Params:");
            }
            for (ParamFnTo requestFnTo : requestFnTos) {
                System.out.println("\tname:" + requestFnTo.getName());
                System.out.println("\tunit:" + requestFnTo.getUnit());
                System.out.println("\ttype:" + requestFnTo.getType().toString());
                System.out.println("\tcollection:" + requestFnTo.isCollection());
            }
            List<ParamFnTo> responseFnTos = to.getResponseParams();
            if (responseFnTos.size() > 0) {
                System.out.println("Response Params:");
            }
            for (ParamFnTo responseFnTo : responseFnTos) {
                System.out.println("\tname:" + responseFnTo.getName());
                System.out.println("\tunit:" + responseFnTo.getUnit());
                System.out.println("\ttype:" + responseFnTo.getType().toString());
                System.out.println("\tcollection:" + responseFnTo.isCollection());
            }

        } else {
            throw new RuntimeException(r.getStatusCode().toString());
        }
    }
}
