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
package client.sn;


import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import br.uff.labtempo.osiris.to.sensornet.RevisionSnTo;
import java.net.URLEncoder;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VsnGetRevisions {

    public static void main(String[] args) throws Exception {
        try (OmcpClient connection = new OmcpClientBuilder().host("192.168.0.7").user("admin", "admin").source("generico").build()) {
            String from = "05-09-2015 16:48:52 -03:00";
            String too = "05-09-2015 15:48:52 -03:00";
            from = URLEncoder.encode(from, "UTF-8");
            too = URLEncoder.encode(too, "UTF-8");
            Response r = connection.doGet("omcp://sensornet/labtempo/collector/labpos/sensor/1/revisions/?from="+from+"&to="+too);
            if (r.getStatusCode() == StatusCode.OK) {
                RevisionSnTo[] to = r.getContent(RevisionSnTo[].class);
                System.out.println("GOT(revisions): " + to.length);
                System.out.println(r.getContent());
            } else {
                throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
            }
        }
    }
}
