/*
 * Copyright 2015 Felipe Santos <feliperalph at hotmail.com>.
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
package br.uff.labtempo.osiris.virtualsensornet.usecase;

import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.osiris.to.common.definitions.FunctionOperation;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.function.InterfaceFnTo;
import br.uff.labtempo.osiris.to.function.ParamFnTo;
import br.uff.labtempo.osiris.to.function.ParamTypeFnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.BlendingVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.controller.ConverterController;
import br.uff.labtempo.osiris.virtualsensornet.controller.DataTypeController;
import br.uff.labtempo.osiris.virtualsensornet.controller.FunctionController;
import br.uff.labtempo.osiris.virtualsensornet.controller.NotifyController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorBlendingController;
import br.uff.labtempo.osiris.virtualsensornet.usecase.server.Bootstrap;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VirtualSensorBlendingTest {

    private static Bootstrap bootstrap;
    private static VirtualSensorBlendingController controller;
    private static NotifyController notifycontroller;
    private static long dt1;
    private static long dt2;
    private static long dt3;
    private static long cc1;
    private static long cc2;
    private static long cc3;
    private static long cc4;
    private static long fn1;
    private static long fn2;

    @Before
    public void getController() throws Exception {
        controller = bootstrap.getBlendingController();
        notifycontroller = bootstrap.getNotifyController();
    }

    @BeforeClass
    public static void upserver() throws Exception {
        bootstrap = new Bootstrap();
        DataTypeController dtc = bootstrap.getDataTypeController();
        dt1 = dtc.create(new DataTypeVsnTo("tempCelsius", ValueType.NUMBER, "celsius", "°C"));
        dt2 = dtc.create(new DataTypeVsnTo("tempFahrenheit", ValueType.NUMBER, "fahrenheit ", "°F"));
        dt3 = dtc.create(new DataTypeVsnTo("tempKelvin", ValueType.NUMBER, "kelvin", "°K"));

        ConverterController cc = bootstrap.getConverterController();
        cc1 = cc.create(new ConverterVsnTo("celsiusToFahrenheit", "value = value * 9/5 + 32;", dt1, dt2));
        cc2 = cc.create(new ConverterVsnTo("celsiusToKelvin", "value = value + 273.15;", dt1, dt3));
        cc3 = cc.create(new ConverterVsnTo("FahrenheitToCelsius", "value = (value - 32) * 5/9;", dt2, dt1));
        cc4 = cc.create(new ConverterVsnTo("kelvinToFahrenheit", "value = value * 9/5 - 459.67;", dt3, dt2));

        FunctionController fc = bootstrap.getFunctionController();
        List<FunctionOperation> operations = new ArrayList<>();
        List<ParamFnTo> requestParams = new ArrayList<>();
        List<ParamFnTo> responseParams = new ArrayList<>();
        ParamTypeFnTo inputType = new ParamTypeFnTo("celsius", ValueType.NUMBER);
        ParamTypeFnTo outputType = new ParamTypeFnTo("kelvin", ValueType.NUMBER);

        operations.add(FunctionOperation.SYNCHRONOUS);

        requestParams.add(new ParamFnTo("in", inputType));
        responseParams.add(new ParamFnTo("out", outputType));

        InterfaceFnTo to = new InterfaceFnTo("temperature conversor", "convert celsius to kelvin", "omcp://converter.function.osiris", operations, requestParams, responseParams);
        fn1 = fc.create(to);
    }

    @AfterClass
    public static void downserver() throws Exception {
        bootstrap.close();
    }

    @Test(expected = BadRequestException.class)
    public void testCreateBlending_Null_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        controller.create(null);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateBlending_NullParams_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        BlendingVsnTo to = new BlendingVsnTo(null);
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateBlending_NullFields_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        BlendingVsnTo to = new BlendingVsnTo("TestCreateBlending_NullFields_ShouldThrowException");
        controller.create(to);
    }

    @Test
    public void TestCreateBlending_Valid_ShouldPass() throws BadRequestException, NotFoundException, InternalServerErrorException {
        List<BlendingVsnTo> vsnTos = controller.getAll();
        long currentSize = vsnTos.size();

        BlendingVsnTo to = new BlendingVsnTo("TestCreateBlending_Valid_ShouldPass");

        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);

        long id = controller.create(to);

        vsnTos = controller.getAll();
        long expectedSize = currentSize + 1;
        Assert.assertEquals(expectedSize, vsnTos.size());
    }

    @Test
    public void TestGetAllBlending_Valid_ShouldPass() throws NotFoundException, BadRequestException, InternalServerErrorException {
        List<BlendingVsnTo> vsnTos = controller.getAll();
        BlendingVsnTo to = new BlendingVsnTo("TestGetAllBlending_Valid_ShouldPass");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);

        controller.create(to);
        List<BlendingVsnTo> vsnTos1 = controller.getAll();
        long expectedSize = vsnTos.size() + 1;
        Assert.assertEquals(expectedSize, vsnTos1.size());
    }

    @Test
    public void TestGetBlending_Valid_ShouldPass() throws NotFoundException, BadRequestException, InternalServerErrorException {
        BlendingVsnTo to = new BlendingVsnTo("TestGetBlending_Valid_ShouldPass");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);

        long id = controller.create(to);
        BlendingVsnTo vsnTo = controller.get(id);
        Assert.assertEquals(id, vsnTo.getId());
    }

    @Test
    public void TestUpdateBlending_Valid_ShouldPass() throws BadRequestException, NotFoundException, InternalServerErrorException, MethodNotAllowedException {
        List<BlendingVsnTo> vsnTos = controller.getAll();
        long currentSize = vsnTos.size();

        BlendingVsnTo to = new BlendingVsnTo("TestUpdateBlending_Valid_ShouldPass");

        to.createField("temperature", dt1);//celsius
        to.createField("temperature", dt2, cc4);//fahrenheit <- kelvinToFahrenheit

        long id = controller.create(to);

        to = controller.get(id);

        long fldId1 = to.getFields().get(0).getId();
        long fldId2 = to.getFields().get(1).getId();

        to.setFunction(fn1);
        to.setCallMode(FunctionOperation.SYNCHRONOUS);
        to.setCallIntervalInMillis(2000);
        to.addRequestParam(fldId1, "in"); //ParamTypeFnTo inputType = celsius
        to.addResponseParam(fldId2, "out"); //ParamTypeFnTo outputType = kelvin

        boolean expectedResult = controller.update(id, to);
        Assert.assertTrue(expectedResult);
    }
}
