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
package client.vsn;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import br.uff.labtempo.osiris.to.virtualsensornet.RevisionVsnTo;
import config.OmcpFactory;
import java.net.URLEncoder;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VsnGetRevisions {

    public static void main(String[] args) throws Exception {
        try (OmcpClient connection = OmcpFactory.getClient()) {
            String from = "05-07-2015 21:33:52 -03:00";
            from = URLEncoder.encode(from, "UTF-8");
            Response r = connection.doGet("omcp://virtualsensornet/vsensor/1/revisions/?to="+from+"&limit=5");
            if (r.getStatusCode() == StatusCode.OK) {
                RevisionVsnTo[] to = r.getContent(RevisionVsnTo[].class);
                System.out.println("GOT(revisions): " + to.length);
                System.out.println(r.getContent());
            } else {
                throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
            }
        }
    }
}
