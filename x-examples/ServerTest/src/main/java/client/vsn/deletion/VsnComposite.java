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
package client.vsn.deletion;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import br.uff.labtempo.osiris.to.virtualsensornet.CompositeVsnTo;
import config.OmcpFactory;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VsnComposite {

    public static void main(String[] args) throws Exception{
       try (OmcpClient client = OmcpFactory.getClient()) {
           deleteAll(client);
       };
    }

    public static void deleteAll(OmcpClient client) {
        String address = "omcp://virtualsensornet/composite/";
        Response r = client.doGet(address);
        if (r.getStatusCode() == StatusCode.OK) {
            CompositeVsnTo[] tos = r.getContent(CompositeVsnTo[].class);
            for (CompositeVsnTo to : tos) {
                r = client.doDelete(address + to.getId());
                if (r.getStatusCode() == StatusCode.OK) {
                    System.out.println("DELETED: " + address + to.getId());
                } else {
                    throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
                }
            }
        } else {
            throw new RuntimeException(r.getStatusCode().toString());
        }
    }
}
