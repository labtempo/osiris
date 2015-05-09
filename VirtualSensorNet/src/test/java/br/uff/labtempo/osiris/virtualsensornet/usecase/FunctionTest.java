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
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.osiris.to.common.definitions.FunctionOperation;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.function.InterfaceFnTo;
import br.uff.labtempo.osiris.to.function.ParamFnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.FunctionVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.controller.FunctionController;
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
public class FunctionTest {

    private static Bootstrap bootstrap;
    private static FunctionController controller;
    private static VirtualSensorBlendingController blendingController;

    @Before
    public void getController() throws Exception {
        controller = bootstrap.getFunctionController();
        blendingController = bootstrap.getBlendingController();
    }

    @BeforeClass
    public static void upserver() throws Exception {
        bootstrap = new Bootstrap();
    }

    @AfterClass
    public static void downserver() throws Exception {
        bootstrap.close();
    }

    @Test(expected = BadRequestException.class)
    public void testCreateFunction_Null_ShouldThrowException() throws BadRequestException, InternalServerErrorException {
        controller.create(null);
    }

    @Test(expected = Exception.class)
    public void testCreateFunction_NullParams_ShouldThrowException() throws BadRequestException, InternalServerErrorException {
        InterfaceFnTo to = new InterfaceFnTo(null, null, null, null, null, null);
        controller.create(to);
    }

    @Test
    public void testCreateFunction_ShouldPass() throws BadRequestException, InternalServerErrorException, NotFoundException {
        //initial length
        List<FunctionVsnTo> fnTos = controller.getAll();
        long count = fnTos.size();

        List<FunctionOperation> operations = new ArrayList<>();
        List<ParamFnTo> requestParams = new ArrayList<>();
        List<ParamFnTo> responseParams = new ArrayList<>();

        operations.add(FunctionOperation.SYNCHRONOUS);

        requestParams.add(new ParamFnTo("in1", "celsius", ValueType.NUMBER));
        responseParams.add(new ParamFnTo("out1", "kelvin", ValueType.NUMBER));

        InterfaceFnTo to = new InterfaceFnTo("temperature conversor", "convert celsius to kelvin", "omcp://converter.function.osiris", operations, requestParams, responseParams);
        long id = controller.create(to);

        long expectedListSize = count + 1;
        fnTos = controller.getAll();

        Assert.assertEquals(expectedListSize, fnTos.size());

        FunctionVsnTo vsnTo = controller.get(id);

        FunctionOperation expectedOperation = FunctionOperation.SYNCHRONOUS;
        String expectedName = "temperature conversor";
        String expectedDescription = "convert celsius to kelvin";
        String expectedAddress = "omcp://converter.function.osiris";
        int expectedCountOperations = 1;
        int expectedCountRequestParams = 1;
        int expectedCountResponseParams = 1;

        Assert.assertEquals(expectedName, vsnTo.getName());
        Assert.assertEquals(expectedDescription, vsnTo.getDescription());
        Assert.assertEquals(expectedAddress, vsnTo.getAddress());
        Assert.assertEquals(expectedCountOperations, vsnTo.getOperations().size());
        Assert.assertEquals(expectedCountRequestParams, vsnTo.getRequestParams().size());
        Assert.assertEquals(expectedCountResponseParams, vsnTo.getResponseParams().size());
        Assert.assertEquals(expectedOperation, vsnTo.getOperations().get(0));

    }

    @Test
    public void testDeleteFunction_ShouldPass() throws BadRequestException, InternalServerErrorException, NotFoundException {

        List<FunctionOperation> operations = new ArrayList<>();
        List<ParamFnTo> requestParams = new ArrayList<>();
        List<ParamFnTo> responseParams = new ArrayList<>();

        operations.add(FunctionOperation.SYNCHRONOUS);

        requestParams.add(new ParamFnTo("in1", "celsius", ValueType.NUMBER));
        responseParams.add(new ParamFnTo("out1", "kelvin", ValueType.NUMBER));

        InterfaceFnTo to = new InterfaceFnTo("testDeleteFunction1", "convert celsius to kelvin", "omcp://converter.function.osiris", operations, requestParams, responseParams);
        long id = controller.create(to);

        FunctionVsnTo vsnTo = controller.get(id);

        boolean expectedResult = controller.delete(id);

        Assert.assertTrue(expectedResult);
    }
}
