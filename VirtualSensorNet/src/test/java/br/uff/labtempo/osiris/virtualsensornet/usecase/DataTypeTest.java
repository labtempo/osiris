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
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataTypeTest {

    private static Bootstrap bootstrap;
    private static DataTypeController controller;
    private static VirtualSensorLinkController linkcontroller;
    private static ConverterController convertercontroller;

    @Before
    public void getController() throws Exception {
        controller = bootstrap.getDataTypeController();
        convertercontroller = bootstrap.getConverterController();
        linkcontroller = bootstrap.getLinkController();
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
    public void testCreateDataType_Null_Invalid_ShouldThrowException() throws BadRequestException, InternalServerErrorException {
        controller.create(null);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateDataType_DataTypeWithNullParams_Invalid_ShouldThrowException() throws BadRequestException, InternalServerErrorException {
        DataTypeVsnTo to = new DataTypeVsnTo(null, ValueType.LOGIC, null, null);
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateDataType_DataTypeWithNullDisplayName_ShouldThrowException() throws BadRequestException, InternalServerErrorException {
        DataTypeVsnTo to = new DataTypeVsnTo(null, ValueType.NUMBER, "temperature", "°C");
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateDataType_DataTypeWithNullUnit_ShouldThrowException() throws BadRequestException, InternalServerErrorException {
        DataTypeVsnTo to = new DataTypeVsnTo("test", ValueType.LOGIC, null, "°C");
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateDataType_DataTypeWithNullSymbol_Invalid_ShouldThrowException() throws BadRequestException, InternalServerErrorException {
        DataTypeVsnTo to = new DataTypeVsnTo("test", ValueType.LOGIC, "temperature", null);
        controller.create(to);
    }

    @Test
    public void TestCreateDataType_Valid_ShouldPass() throws BadRequestException, InternalServerErrorException {
        DataTypeVsnTo to = new DataTypeVsnTo("test", ValueType.NUMBER, "temperature", "°C");
        List<DataTypeVsnTo> vsnTos = controller.getAll();
        long id = controller.create(to);
        long expectedSize = vsnTos.size() + 1;
        vsnTos = controller.getAll();
        Assert.assertEquals(expectedSize, vsnTos.size());
    }

    @Test
    public void TestGetAllDataType_Valid_ShouldPass() throws NotFoundException, BadRequestException, InternalServerErrorException {
        List<DataTypeVsnTo> vsnTos = controller.getAll();
        DataTypeVsnTo to = new DataTypeVsnTo("test", ValueType.NUMBER, "temperature", "°C");
        controller.create(to);
        List<DataTypeVsnTo> vsnTos1 = controller.getAll();
        long expectedSize = vsnTos.size() + 1;
        Assert.assertEquals(expectedSize, vsnTos1.size());
    }

    @Test
    public void TestGetDataType_Valid_ShouldPass() throws NotFoundException, BadRequestException, InternalServerErrorException {
        DataTypeVsnTo to = new DataTypeVsnTo("test", ValueType.NUMBER, "temperature", "°C");
        long id = controller.create(to);
        DataTypeVsnTo vsnTo = controller.get(id);
        Assert.assertEquals(to, vsnTo);
        Assert.assertEquals(id, vsnTo.getId());
    }

    @Test
    public void TestGetDataType_CountUseByFields_ShouldPass() throws NotFoundException, BadRequestException, InternalServerErrorException {
        long dt1 = controller.create(new DataTypeVsnTo("tempCelsius", ValueType.NUMBER, "celsius", "°C"));
        long dt2 = controller.create(new DataTypeVsnTo("tempFahrenheit", ValueType.NUMBER, "fahrenheit ", "°F"));
        long cc1 = convertercontroller.create(new ConverterVsnTo("celsiusToFahrenheit", "value = value * 9/5 + 32;", dt1, dt2));

        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        linkcontroller.create(to);

        to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        linkcontroller.create(to);

        DataTypeVsnTo to1 = controller.get(dt1);
        DataTypeVsnTo to2 = controller.get(dt2);

        long expectedAmountTo1 = 4;
        long expectedAmountTo2 = 2;
        Assert.assertEquals(expectedAmountTo1, to1.getUsedBy());
        Assert.assertEquals(expectedAmountTo2, to2.getUsedBy());
    }

    @Test(expected = NotFoundException.class)
    public void TestGetDataType_InvalidIdRange_ShouldThrowException() throws NotFoundException, BadRequestException, InternalServerErrorException {
        DataTypeVsnTo vsnTo = controller.get(10000);
    }

    @Test(expected = BadRequestException.class)
    public void TestUpdateDataType_NullParams_Invalid_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        DataTypeVsnTo to = new DataTypeVsnTo("test", ValueType.NUMBER, "temperature", "°C");
        long id = controller.create(to);
        String expectedDisplayName = null;
        String expectedUnit = null;
        String expectedSymbol = null;
        ValueType valueType = ValueType.TEXT;
        DataTypeVsnTo to1 = new DataTypeVsnTo(expectedDisplayName, valueType, expectedUnit, expectedSymbol);
        controller.update(id, to1);
    }

    @Test(expected = BadRequestException.class)
    public void TestUpdateDataType_UsedInField_Invalid_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        long dt1 = controller.create(new DataTypeVsnTo("tempCelsius", ValueType.NUMBER, "celsius", "°C"));
        long dt2 = controller.create(new DataTypeVsnTo("tempFahrenheit", ValueType.NUMBER, "fahrenheit ", "°F"));
        long dt3 = controller.create(new DataTypeVsnTo("tempKelvin", ValueType.NUMBER, "kelvin", "°K"));
        long cc1 = convertercontroller.create(new ConverterVsnTo("celsiusToFahrenheit", "value = value * 9/5 + 32;", dt1, dt2));

        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        long id = linkcontroller.create(to);

        String expectedDisplayName = "test1";
        String expectedUnit = "luminosity";
        String expectedSymbol = "Candela";
        ValueType valueType = ValueType.TEXT;

        DataTypeVsnTo to1 = new DataTypeVsnTo(expectedDisplayName, valueType, expectedUnit, expectedSymbol);

        controller.update(dt1, to1);
    }

    @Test
    public void TestUpdateDataType_UsedInField_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        long dt1 = controller.create(new DataTypeVsnTo("tempCelsius", ValueType.NUMBER, "celsius", "°C"));
        long dt2 = controller.create(new DataTypeVsnTo("tempFahrenheit", ValueType.NUMBER, "fahrenheit ", "°F"));
        long dt3 = controller.create(new DataTypeVsnTo("tempKelvin", ValueType.NUMBER, "kelvin", "°K"));
        long cc1 = convertercontroller.create(new ConverterVsnTo("celsiusToFahrenheit", "value = value * 9/5 + 32;", dt1, dt2));

        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        long id = linkcontroller.create(to);

        String expectedDisplayName = "test1";
        String expectedUnit = "celsius";
        String expectedSymbol = "°C";
        ValueType valueType = ValueType.NUMBER;

        DataTypeVsnTo to1 = new DataTypeVsnTo(expectedDisplayName, valueType, expectedUnit, expectedSymbol);

        boolean updated = controller.update(dt1, to1);
        Assert.assertTrue(updated);

        DataTypeVsnTo vsnTo = controller.get(dt1);
        Assert.assertEquals(expectedDisplayName, vsnTo.getDisplayName());
    }

    @Test
    public void TestUpdateDataType_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        DataTypeVsnTo to = new DataTypeVsnTo("test", ValueType.NUMBER, "temperature", "°C");
        long id = controller.create(to);

        String expectedDisplayName = "test1";
        String expectedUnit = "luminosity";
        String expectedSymbol = "Candela";
        ValueType valueType = ValueType.TEXT;

        DataTypeVsnTo to1 = new DataTypeVsnTo(expectedDisplayName, valueType, expectedUnit, expectedSymbol);

        controller.update(id, to1);

        DataTypeVsnTo vsnTo = controller.get(id);

        Assert.assertEquals(id, vsnTo.getId());
        Assert.assertEquals(expectedDisplayName, vsnTo.getDisplayName());
        Assert.assertEquals(expectedSymbol, vsnTo.getSymbol());
        Assert.assertEquals(expectedUnit, vsnTo.getUnit());
        Assert.assertEquals(valueType, vsnTo.getType());
    }

    @Test(expected = BadRequestException.class)
    public void TestDeleteDataType_UsedInField_Invalid_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        long dt1 = controller.create(new DataTypeVsnTo("tempCelsius", ValueType.NUMBER, "celsius", "°C"));
        long dt2 = controller.create(new DataTypeVsnTo("tempFahrenheit", ValueType.NUMBER, "fahrenheit ", "°F"));
        long dt3 = controller.create(new DataTypeVsnTo("tempKelvin", ValueType.NUMBER, "kelvin", "°K"));
        long cc1 = convertercontroller.create(new ConverterVsnTo("celsiusToFahrenheit", "value = value * 9/5 + 32;", dt1, dt2));

        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        linkcontroller.create(to);

        boolean updated = controller.delete(dt1);
        Assert.assertTrue(updated);
    }

    @Test
    public void TestDeleteDataType_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, InternalServerErrorException, BadRequestException {
        DataTypeVsnTo to = new DataTypeVsnTo("test", ValueType.NUMBER, "temperature", "°C");
        long id = controller.create(to);
        boolean result = controller.delete(id);
        Assert.assertEquals(true, result);
    }

    @Test(expected = NotFoundException.class)
    public void TestDeleteDataType_InvalidIdRange_ShouldThrowException() throws NotFoundException, BadRequestException, InternalServerErrorException {
        controller.delete(10000);
    }
}
