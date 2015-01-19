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
package br.uff.labtempo.osiris.virtualsensornet.usecase;

import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.controller.ConverterController;
import br.uff.labtempo.osiris.virtualsensornet.controller.DataTypeController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorLinkController;
import br.uff.labtempo.osiris.virtualsensornet.usecase.server.Bootstrap;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConverterTest {

    private static Bootstrap bootstrap;
    private static ConverterController controller;
    private static VirtualSensorLinkController linkcontroller;
    private static long dt1;
    private static long dt2;
    private static long dt3;

    @Before
    public void getController() throws Exception {
        controller = bootstrap.getConverterController();
        linkcontroller = bootstrap.getLinkController();
    }

    @BeforeClass
    public static void upserver() throws Exception {
        bootstrap = new Bootstrap();
        DataTypeController dtc = bootstrap.getDataTypeController();
        dt1 = dtc.create(new DataTypeVsnTo("tempCelsius", ValueType.NUMBER, "celsius", "°C"));
        dt2 = dtc.create(new DataTypeVsnTo("tempFahrenheit", ValueType.NUMBER, "fahrenheit ", "°F"));
        dt3 = dtc.create(new DataTypeVsnTo("tempKelvin", ValueType.NUMBER, "kelvin", "°K"));
    }

    @AfterClass
    public static void downserver() throws Exception {
        bootstrap.close();
    }

    @Test(expected = BadRequestException.class)
    public void testCreateConverter_Null_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        controller.create(null);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateConverter_NullParams_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        ConverterVsnTo to = new ConverterVsnTo(null, null, 0, 0);
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateConverter_NullDisplayName_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        ConverterVsnTo to = new ConverterVsnTo(null, "value = value;", dt1, dt2);
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateConverter_NullExpression_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        ConverterVsnTo to = new ConverterVsnTo("test", null, dt1, dt2);
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateConverter_NullInputDataType_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        ConverterVsnTo to = new ConverterVsnTo("test", "value = value;", 0, dt2);
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateConverter_NullOutputDataType_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        ConverterVsnTo to = new ConverterVsnTo("test", "value = value;", dt1, 0);
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateConverter_InvalidExpression_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        List<ConverterVsnTo> vsnTos = controller.getAll();
        ConverterVsnTo to = new ConverterVsnTo("test", "hjkhuhkj", dt1, dt2);
        long id = controller.create(to);
        long expectedId = vsnTos.size() + 1;
        Assert.assertEquals(expectedId, id);
    }

    @Test
    public void TestCreateConverter_Valid_ShouldPass() throws BadRequestException, NotFoundException, InternalServerErrorException {
        List<ConverterVsnTo> vsnTos = controller.getAll();
        ConverterVsnTo to = new ConverterVsnTo("test", "value = value;", dt1, dt2);
        long id = controller.create(to);
        long expectedId = vsnTos.size() + 1;
        Assert.assertEquals(expectedId, id);
    }

    @Test
    public void TestGetAllConverter_Valid_ShouldPass() throws NotFoundException, BadRequestException, InternalServerErrorException {
        List<ConverterVsnTo> vsnTos = controller.getAll();
        ConverterVsnTo to = new ConverterVsnTo("test", "value = value;", dt1, dt2);
        controller.create(to);
        List<ConverterVsnTo> vsnTos1 = controller.getAll();
        long expectedSize = vsnTos.size() + 1;
        Assert.assertEquals(expectedSize, vsnTos1.size());
    }

    @Test
    public void TestGetConverter_Valid_ShouldPass() throws NotFoundException, BadRequestException, InternalServerErrorException {
        ConverterVsnTo to = new ConverterVsnTo("test", "value = value;", dt1, dt2);
        long id = controller.create(to);
        ConverterVsnTo vsnTo = controller.get(id);
        Assert.assertEquals(to, vsnTo);
        Assert.assertEquals(id, vsnTo.getId());
    }
    
    @Test
    public void TestGetConverter_CountUseByFields_ShouldPass() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        long cc1 = controller.create(new ConverterVsnTo("celsiusToFahrenheit", "value = value * 9/5 + 32;", dt1, dt2));

        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        linkcontroller.create(to);
        
        to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        linkcontroller.create(to);

        ConverterVsnTo vsnTo = controller.get(cc1);
        long expectedAmount = 2;
        Assert.assertEquals(expectedAmount, vsnTo.getUsedBy());        
    }

    @Test(expected = NotFoundException.class)
    public void TestGetConverter_InvalidIdRange_ShouldThrowException() throws NotFoundException, BadRequestException, InternalServerErrorException {
        ConverterVsnTo vsnTo = controller.get(10000);
    }

    @Test(expected = BadRequestException.class)
    public void TestUpdateConverter_NullParams_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        ConverterVsnTo to = new ConverterVsnTo("test", "value = value;", dt1, dt2);
        long id = controller.create(to);

        String expectedDisplayName = null;
        String expectedExpression = null;
        long expectedInputDataId = 0;
        long expectedOutputDataId = 0;

        ConverterVsnTo to1 = new ConverterVsnTo(expectedDisplayName, expectedExpression, expectedInputDataId, expectedOutputDataId);

        controller.update(id, to1);
    }

    @Test(expected = BadRequestException.class)
    public void TestUpdateConverter_UsedInField_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        long cc1 = controller.create(new ConverterVsnTo("celsiusToFahrenheit", "value = value * 9/5 + 32;", dt1, dt2));

        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        long id = linkcontroller.create(to);

        String expectedDisplayName = "test1";
        String expectedExpression = "value = value + 1;";
        long expectedInputDataId = dt1;
        long expectedOutputDataId = dt3;

        ConverterVsnTo to1 = new ConverterVsnTo(expectedDisplayName, expectedExpression, expectedInputDataId, expectedOutputDataId);

        controller.update(cc1, to1);
    }

    @Test
    public void TestUpdateConverter_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        ConverterVsnTo to = new ConverterVsnTo("test", "value = value;", dt1, dt2);
        long id = controller.create(to);

        String expectedDisplayName = "test1";
        String expectedExpression = "value = value + 1;";
        long expectedInputDataId = dt2;
        long expectedOutputDataId = dt3;

        ConverterVsnTo to1 = new ConverterVsnTo(expectedDisplayName, expectedExpression, expectedInputDataId, expectedOutputDataId);

        controller.update(id, to1);

        ConverterVsnTo vsnTo = controller.get(id);

        Assert.assertEquals(id, vsnTo.getId());
        Assert.assertEquals(expectedDisplayName, vsnTo.getDisplayName());
        Assert.assertEquals(expectedExpression, vsnTo.getExpression());
        Assert.assertEquals(expectedInputDataId, vsnTo.getInputDataTypeId());
        Assert.assertEquals(expectedOutputDataId, vsnTo.getOutputDataTypeId());
    }

    @Test
    public void TestDeleteConverter_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, InternalServerErrorException, BadRequestException {
        ConverterVsnTo to = new ConverterVsnTo("test", "value = value;", dt1, dt2);
        long id = controller.create(to);
        boolean result = controller.delete(id);
        Assert.assertEquals(true, result);
    }

    @Test(expected = BadRequestException.class)
    public void TestDeleteConverter_UsedInField_ShouldThrowException() throws NotFoundException, BadRequestException, InternalServerErrorException {
        long cc1 = controller.create(new ConverterVsnTo("celsiusToFahrenheit", "value = value * 9/5 + 32;", dt1, dt2));

        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        long id = linkcontroller.create(to);

        controller.delete(cc1);
    }

    @Test(expected = NotFoundException.class)
    public void TestDeleteConverter_InvalidIdRange_ShouldThrowException() throws NotFoundException, BadRequestException, InternalServerErrorException {
        controller.delete(10000);
    }
}
