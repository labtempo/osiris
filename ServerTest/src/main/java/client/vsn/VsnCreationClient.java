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
import br.uff.labtempo.osiris.to.common.definitions.FunctionOperation;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.function.InterfaceFnTo;
import br.uff.labtempo.osiris.to.function.ParamFnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.BlendingVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.CompositeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.FunctionVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import static config.Config.*;
import config.OmcpFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VsnCreationClient {

    public static void main(String[] args) throws Exception {
        try (OmcpClient connection = OmcpFactory.getClient() ) {
            populate(connection);
        }
    }

    public static void populate(OmcpClient client) {
        Map<String, Long> ids = new HashMap<>();
        int i = 1;

        List<DataTypeVsnTo> dtvts = generateDataTypes();
        for (DataTypeVsnTo dtvt : dtvts) {
            Response r = client.doPost("omcp://virtualsensornet/datatype/", dtvt);
            if (r.getStatusCode() == StatusCode.CREATED) {
                System.out.print("Created: " + r.getLocation());
                r = client.doGet(r.getLocation());
                dtvt = r.getContent(DataTypeVsnTo.class);
                System.out.println(" ID: " + dtvt.getId());
                ids.put("dt" + String.valueOf(i++), dtvt.getId());
            } else {
                throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
            }
        }

        i = 1;
        List<ConverterVsnTo> cvts = generateConverters(ids);
        for (ConverterVsnTo cvt : cvts) {
            Response r = client.doPost("omcp://virtualsensornet/converter/", cvt);
            if (r.getStatusCode() == StatusCode.CREATED) {
                System.out.print("Created: " + r.getLocation());
                r = client.doGet(r.getLocation());
                cvt = r.getContent(ConverterVsnTo.class);
                System.out.println(" ID: " + cvt.getId());
                ids.put("cv" + String.valueOf(i++), cvt.getId());
            } else {
                throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
            }
        }

        i = 1;
        List<InterfaceFnTo> ifts = generateFunctions();
        for (InterfaceFnTo ift : ifts) {
            Response r = client.doPost("omcp://virtualsensornet/function/", ift);
            if (r.getStatusCode() == StatusCode.CREATED) {
                System.out.print("Created: " + r.getLocation());
                r = client.doGet(r.getLocation());
                FunctionVsnTo fvt = r.getContent(FunctionVsnTo.class);
                System.out.println(" ID: " + fvt.getId());
                ids.put("fn" + String.valueOf(i++), fvt.getId());
            } else {
                throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
            }
        }

        List<LinkVsnTo> lvts = generateLinks(ids);
        for (LinkVsnTo lvt : lvts) {
            Response r = client.doPost("omcp://virtualsensornet/link/", lvt);
            if (r.getStatusCode() == StatusCode.CREATED) {
                System.out.print("Created: " + r.getLocation());
                r = client.doGet(r.getLocation());
                lvt = r.getContent(LinkVsnTo.class);
                System.out.println(" ID: " + lvt.getId());
            } else {
                throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
            }
        }

        List<CompositeVsnTo> compositeVsnTos = generateComposites();
        for (CompositeVsnTo cvt : compositeVsnTos) {
            Response r = client.doPost("omcp://virtualsensornet/composite/", cvt);
            if (r.getStatusCode() == StatusCode.CREATED) {
                System.out.print("Created: " + r.getLocation());
                r = client.doGet(r.getLocation());
                cvt = r.getContent(CompositeVsnTo.class);
                System.out.println(" ID: " + cvt.getId());
            } else {
                throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
            }
        }

        List<BlendingVsnTo> bvts = generateBlendings(ids);
        for (BlendingVsnTo bvt : bvts) {
            Response r = client.doPost("omcp://virtualsensornet/blending/", bvt);
            if (r.getStatusCode() == StatusCode.CREATED) {
                System.out.print("Created: " + r.getLocation());
                r = client.doGet(r.getLocation());
                bvt = r.getContent(BlendingVsnTo.class);
                System.out.println(" ID: " + bvt.getId());
            } else {
                throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
            }
        }
    }

    private static List<DataTypeVsnTo> generateDataTypes() {
        List<DataTypeVsnTo> dtvts = new ArrayList<>();
        dtvts.add(new DataTypeVsnTo("tempCelsius", ValueType.NUMBER, "celsius", "°C"));
        dtvts.add(new DataTypeVsnTo("tempFahrenheit", ValueType.NUMBER, "fahrenheit ", "°F"));
        dtvts.add(new DataTypeVsnTo("tempKelvin", ValueType.NUMBER, "kelvin", "°K"));
        return dtvts;
    }

    private static List<ConverterVsnTo> generateConverters(Map<String, Long> ids) {
        List<ConverterVsnTo> cvts = new ArrayList<>();
        cvts.add(new ConverterVsnTo("celsiusToFahrenheit", "value = value * 9/5 + 32;", ids.get("dt1"), ids.get("dt2")));
        cvts.add(new ConverterVsnTo("celsiusToKelvin", "value = value + 273.15;", ids.get("dt1"), ids.get("dt3")));
        cvts.add(new ConverterVsnTo("FahrenheitToCelsius", "value = (value - 32) * 5/9;", ids.get("dt2"), ids.get("dt1")));
        cvts.add(new ConverterVsnTo("kelvinToFahrenheit", "value = value * 9/5 - 459.67;", ids.get("dt3"), ids.get("dt2")));
        return cvts;
    }

    private static List<LinkVsnTo> generateLinks(Map<String, Long> ids) {
        List<LinkVsnTo> lvts = new ArrayList<>();
        //link 1
        LinkVsnTo linkVsnTo = new LinkVsnTo("1", "labpos", "labtempo");
        linkVsnTo.createField("temperature", ids.get("dt1"));
        linkVsnTo.createField("temperature", ids.get("dt2"), ids.get("cv1"));
        lvts.add(linkVsnTo);
        //link 2
        linkVsnTo = new LinkVsnTo("2", "labpos", "labtempo");
        linkVsnTo.createField("temperature", ids.get("dt1"));
        linkVsnTo.createField("temperature", ids.get("dt2"), ids.get("cv1"));
        lvts.add(linkVsnTo);
        
        return lvts;
    }

    private static List<InterfaceFnTo> generateFunctions() {
        List<InterfaceFnTo> ifts = new ArrayList<>();
//
//        List<FunctionOperation> operations = new ArrayList<>();
//        operations.add(FunctionOperation.SYNCHRONOUS);
//        operations.add(FunctionOperation.ASYNCHRONOUS);
//
//        List<ParamFnTo> requestParams = new ArrayList<>();
//        requestParams.add(new ParamFnTo("in", "celsius", ValueType.NUMBER));
//
//        //instance 1
//        List<ParamFnTo> responseParams = new ArrayList<>();
//        responseParams.add(new ParamFnTo("out", "kelvin", ValueType.NUMBER));
//        InterfaceFnTo interfaceFnTo = new InterfaceFnTo("celsiusToKelvin", "convert celsius to kelvin", "omcp://ctk.function.osiris", operations, requestParams, responseParams);
//
//        ifts.add(interfaceFnTo);
//
//        //instance 2
//        List<ParamFnTo> responseParams2 = new ArrayList<>();
//        responseParams2.add(new ParamFnTo("out1", "fahrenheit", ValueType.NUMBER));
//        interfaceFnTo = new InterfaceFnTo("celsiusToFahrenheit", "convert celsius to fahrenheit", "omcp://ctf.function.osiris", operations, requestParams, responseParams2);;

//        ifts.add(interfaceFnTo);
//
//        return ifts;
        String address = "omcp://sum.function/";
        List<FunctionOperation> operations = new ArrayList<>();
        operations.add(FunctionOperation.SYNCHRONOUS);
        operations.add(FunctionOperation.ASYNCHRONOUS);

        List<ParamFnTo> requestParamFnTos = new ArrayList<>();
        requestParamFnTos.add(new ParamFnTo("input", ValueType.NUMBER, true));

        List<ParamFnTo> responseParamFnTos = new ArrayList<>();
        responseParamFnTos.add(new ParamFnTo("output", ValueType.NUMBER));

        InterfaceFnTo interfaceFnTo = new InterfaceFnTo("sum", "sum all values", address, operations, requestParamFnTos, responseParamFnTos);
        ifts.add(interfaceFnTo);

        return ifts;
    }

    private static List<BlendingVsnTo> generateBlendings(Map<String, Long> ids) {
        List<BlendingVsnTo> bvts = new ArrayList<>();

        BlendingVsnTo blendingVsnTo = new BlendingVsnTo();
        blendingVsnTo.createField("temperature", ids.get("dt1"));//celsius

        bvts.add(blendingVsnTo);
        return bvts;
    }

    private static List<CompositeVsnTo> generateComposites() {
        List<CompositeVsnTo> cvts = new ArrayList<>();
        CompositeVsnTo compositeVsnTo = new CompositeVsnTo();
        compositeVsnTo.bindToField(1);
        compositeVsnTo.bindToField(3);

        cvts.add(compositeVsnTo);
        return cvts;
    }
}
