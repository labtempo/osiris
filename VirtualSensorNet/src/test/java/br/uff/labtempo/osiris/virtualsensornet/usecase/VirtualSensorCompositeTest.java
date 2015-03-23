/*
 * Copyright 2015 Felipe.
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
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.common.data.FieldTo;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.virtualsensornet.CompositeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.ValueVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.controller.ConverterController;
import br.uff.labtempo.osiris.virtualsensornet.controller.DataTypeController;
import br.uff.labtempo.osiris.virtualsensornet.controller.NotifyController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorCompositeController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorLinkController;
import br.uff.labtempo.osiris.virtualsensornet.usecase.server.Bootstrap;
import br.uff.labtempo.osiris.virtualsensornet.usecase.server.CollectorDataBuilder;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Felipe
 */
public class VirtualSensorCompositeTest {

    private static Bootstrap bootstrap;
    private static VirtualSensorCompositeController controller;
    private static VirtualSensorController vscontroller;
    private static NotifyController notifycontroller;
    private static long dt1;
    private static long dt2;
    private static long dt3;
    private static long cc1;
    private static long cc2;
    private static long cc3;
    private static long cc4;
    private static long fld1;
    private static long fld2;
    private static long fld3;
    private static long fld4;

    @Before
    public void getController() throws Exception {

    }

    @BeforeClass
    public static void upserver() throws Exception {
        bootstrap = new Bootstrap();

        controller = bootstrap.getCompositeController();
        vscontroller = bootstrap.getVirtualSensorController();
        notifycontroller = bootstrap.getNotifyController();

        DataTypeController dtc = bootstrap.getDataTypeController();
        dt1 = dtc.create(new DataTypeVsnTo("tempCelsius", ValueType.NUMBER, "celsius", "°C"));
        dt2 = dtc.create(new DataTypeVsnTo("tempFahrenheit", ValueType.NUMBER, "fahrenheit ", "°F"));
        dt3 = dtc.create(new DataTypeVsnTo("tempKelvin", ValueType.NUMBER, "kelvin", "°K"));

        ConverterController cc = bootstrap.getConverterController();
        cc1 = cc.create(new ConverterVsnTo("celsiusToFahrenheit", "value = value * 9/5 + 32;", dt1, dt2));
        cc2 = cc.create(new ConverterVsnTo("celsiusToKelvin", "value = value + 273.15;", dt1, dt3));
        cc3 = cc.create(new ConverterVsnTo("FahrenheitToCelsius", "value = (value - 32) * 5/9;", dt2, dt1));
        cc4 = cc.create(new ConverterVsnTo("kelvinToFahrenheit", "value = value * 9/5 - 459.67;", dt3, dt2));

        VirtualSensorLinkController linkcontroller = bootstrap.getLinkController();
        LinkVsnTo to1 = new LinkVsnTo("sensor1", "collector", "network");
        to1.createField("temperature", dt2, cc1);
        to1.createField("temperature", dt1);

        LinkVsnTo to2 = new LinkVsnTo("sensor2", "collector", "network");
        to2.createField("temperature", dt2, cc1);
        to2.createField("temperature", dt1);

        long lnk1 = linkcontroller.create(to1);
        long lnk2 = linkcontroller.create(to2);

        VirtualSensorVsnTo vsvt1 = vscontroller.get(lnk1);
        VirtualSensorVsnTo vsvt2 = vscontroller.get(lnk2);

        List<ValueVsnTo> vsnTo1 = vsvt1.getValuesTo();
        List<ValueVsnTo> vsnTo2 = vsvt2.getValuesTo();

        fld1 = vsnTo1.get(0).getId();
        fld2 = vsnTo1.get(1).getId();
        fld3 = vsnTo2.get(0).getId();
        fld4 = vsnTo2.get(1).getId();
    }

    @AfterClass
    public static void downserver() throws Exception {
        bootstrap.close();
    }

    @Test(expected = BadRequestException.class)
    public void testCreateComposite_Null_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        controller.create(null);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateComposite_NullLabel_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        CompositeVsnTo to = new CompositeVsnTo(null);
        to.linkField(1);
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateComposite_NullFields_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        CompositeVsnTo to = new CompositeVsnTo("test");
        controller.create(to);
    }

    @Test
    public void TestCreateComposite_Valid_ShouldPass() throws BadRequestException, NotFoundException, InternalServerErrorException {
        List<CompositeVsnTo> vsnTos = controller.getAll();

        CompositeVsnTo to = new CompositeVsnTo("TestCreateComposite");
        to.linkField(fld1);
        to.linkField(fld2);
        to.linkField(fld3);
        to.linkField(fld4);
        controller.create(to);

        int expectedSize = vsnTos.size() + 1;
        vsnTos = controller.getAll();
        long current = vsnTos.size();
        Assert.assertEquals(expectedSize, current);
    }

    @Test
    public void TestGetAllComposite_Valid_ShouldPass() throws NotFoundException, BadRequestException, InternalServerErrorException {
        List<CompositeVsnTo> vsnTos = controller.getAll();

        CompositeVsnTo to = new CompositeVsnTo("TestGetAllComposite");
        to.linkField(fld1);
        controller.create(to);

        List<CompositeVsnTo> vsnTos1 = controller.getAll();
        long expectedSize = vsnTos.size() + 1;
        Assert.assertEquals(expectedSize, vsnTos1.size());
    }

    @Test
    public void TestGetComposite_Valid_ShouldPass() throws NotFoundException, BadRequestException, InternalServerErrorException {
        CompositeVsnTo to = new CompositeVsnTo("TestGetComposite");
        to.linkField(fld1);
        to.linkField(fld2);
        to.linkField(fld3);
        to.linkField(fld4);

        long id = controller.create(to);
        CompositeVsnTo vsnTo = controller.get(id);

        Assert.assertEquals(id, vsnTo.getId());
    }

    @Test(expected = NotFoundException.class)
    public void TestGetComposite_InvalidIdRange_ShouldThrowException() throws NotFoundException, BadRequestException, InternalServerErrorException {
        CompositeVsnTo to = controller.get(10000);
    }

    @Test(expected = BadRequestException.class)
    public void TestUpdateComposite_NullFields_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        CompositeVsnTo to = new CompositeVsnTo("TestUpdateComposite_NullParams");
        to.linkField(fld1);
        to.linkField(fld2);
        to.linkField(fld3);
        to.linkField(fld4);

        long id = controller.create(to);

        CompositeVsnTo to1 = new CompositeVsnTo("TestUpdateComposite_NullParams");

        controller.update(id, to1);
    }

    @Test(expected = BadRequestException.class)
    public void TestUpdateComposite_NullLabel_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        CompositeVsnTo to = new CompositeVsnTo("TestUpdateComposite_NullLabel");
        to.linkField(fld1);
        to.linkField(fld2);
        to.linkField(fld3);
        to.linkField(fld4);

        long id = controller.create(to);

        CompositeVsnTo to1 = new CompositeVsnTo(null);
        to.linkField(fld1);

        controller.update(id, to1);
    }

    @Test
    public void TestUpdateComposite_RemoveField_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        CompositeVsnTo to = new CompositeVsnTo("TestUpdateComposite_NoFieldUpdate");
        to.linkField(fld1);
        to.linkField(fld2);
        to.linkField(fld3);
        to.linkField(fld4);

        long id = controller.create(to);

        String expectedLabel = "TestUpdateComposite_NoFieldUpdate";

        CompositeVsnTo lvt = controller.get(id);

        CompositeVsnTo to1 = new CompositeVsnTo(expectedLabel);

        for (FieldTo field : lvt.getFields()) {
            to1.linkField(field.getId());
            break;
        }

        boolean isUpdated = controller.update(id, to1);

        Assert.assertTrue(isUpdated);

        CompositeVsnTo vsnTo = controller.get(id);

        Assert.assertEquals(id, vsnTo.getId());
        Assert.assertEquals(expectedLabel, vsnTo.getLabel());

        List<? extends FieldTo> list = vsnTo.getFields();
        int expectedFieldListSize = 1;
        Assert.assertEquals(expectedFieldListSize, list.size());
    }

    @Test
    public void TestUpdateComposite_AddField_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        CompositeVsnTo to = new CompositeVsnTo("TestUpdateComposite_AddRemoveField");
        to.linkField(fld1);

        long id = controller.create(to);

        String expectedLabel = "TestUpdateComposite_NoFieldUpdate";

        CompositeVsnTo lvt = controller.get(id);

        CompositeVsnTo to1 = new CompositeVsnTo(expectedLabel);
        to1.linkField(fld2);
        to1.linkField(fld3);
        to1.linkField(fld4);

        for (FieldTo field : lvt.getFields()) {
            to1.linkField(field.getId());
            break;
        }

        boolean isUpdated = controller.update(id, to1);

        Assert.assertTrue(isUpdated);

        CompositeVsnTo vsnTo = controller.get(id);

        Assert.assertEquals(id, vsnTo.getId());
        Assert.assertEquals(expectedLabel, vsnTo.getLabel());

        List<? extends FieldTo> list = vsnTo.getFields();
        int expectedFieldListSize = 4;
        Assert.assertEquals(expectedFieldListSize, list.size());
    }

    @Test
    public void TestUpdateComposite_CheckValues_CheckFieldIsInitialized_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, InternalServerErrorException, BadRequestException {
        CompositeVsnTo to = new CompositeVsnTo("TestUpdateComposite_CheckValues");
        to.linkField(fld1);
        to.linkField(fld2);
        to.linkField(fld3);
        to.linkField(fld4);
        long id = controller.create(to);

        CompositeVsnTo lvt = controller.get(id);

        FieldTo ft1 = lvt.getFields().get(0);
        FieldTo ft2 = lvt.getFields().get(1);
        FieldTo ft3 = lvt.getFields().get(2);
        FieldTo ft4 = lvt.getFields().get(3);

        Assert.assertEquals(false, ft1.isInitialized());
        Assert.assertEquals(false, ft2.isInitialized());
        Assert.assertEquals(false, ft3.isInitialized());
        Assert.assertEquals(false, ft4.isInitialized());

        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector", "sensor1");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.updateValues(sampleCoTo);

        builder = new CollectorDataBuilder("network", "collector", "sensor2");
        sampleCoTo = builder.generateSample();
        notifycontroller.updateValues(sampleCoTo);

        lvt = controller.get(id);

        //new sample
        sampleCoTo = builder.generateSample();
        notifycontroller.updateValues(sampleCoTo);

        CompositeVsnTo lvt1 = controller.get(id);
        int expectedListSize = 4;
        Assert.assertEquals(expectedListSize, lvt1.getFields().size());

        ft1 = lvt.getFields().get(0);
        ft2 = lvt.getFields().get(1);
        ft3 = lvt.getFields().get(2);
        ft4 = lvt.getFields().get(3);

        Assert.assertEquals(true, ft1.isInitialized());
        Assert.assertEquals(true, ft2.isInitialized());
        Assert.assertEquals(true, ft3.isInitialized());
        Assert.assertEquals(true, ft4.isInitialized());
    }

    @Test
    public void TestDeleteComposite_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, InternalServerErrorException, BadRequestException {
        CompositeVsnTo to = new CompositeVsnTo("TestDeleteComposite");
        to.linkField(fld2);
        to.linkField(fld3);
        to.linkField(fld4);

        long id = controller.create(to);
        boolean result = controller.delete(id);
        Assert.assertEquals(true, result);
    }

    @Test(expected = NotFoundException.class)
    public void TestDeleteComposite_InvalidIdRange_ShouldThrowException() throws NotFoundException, BadRequestException, InternalServerErrorException, MethodNotAllowedException {
        controller.delete(10000);
    }
}
