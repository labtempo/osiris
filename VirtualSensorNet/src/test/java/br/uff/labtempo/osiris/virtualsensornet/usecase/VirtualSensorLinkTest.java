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
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.common.data.FieldTo;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.controller.ConverterController;
import br.uff.labtempo.osiris.virtualsensornet.controller.DataTypeController;
import br.uff.labtempo.osiris.virtualsensornet.controller.NotifyController;
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
 * @author Felipe Santos <fralph at ic.uff.br>
 */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VirtualSensorLinkTest {

    private static Bootstrap bootstrap;
    private static VirtualSensorLinkController controller;
    private static NotifyController notifycontroller;
    private static long dt1;
    private static long dt2;
    private static long dt3;
    private static long cc1;
    private static long cc2;
    private static long cc3;
    private static long cc4;

    @Before
    public void getController() throws Exception {
        controller = bootstrap.getLinkController();
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
    }

    @AfterClass
    public static void downserver() throws Exception {
        bootstrap.close();
    }

    @Test(expected = BadRequestException.class)
    public void testCreateLink_Null_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        controller.create(null);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateLink_NullParams_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo(null, null, null);
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateLink_NullSensorId_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo(null, "collector", "network");
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateLink_NullCollectorId_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo("sensor", null, "network");
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateLink_NullNetworkId_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", null);
        controller.create(to);
    }

    @Test(expected = BadRequestException.class)
    public void TestCreateLink_NullFields_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        controller.create(to);
    }

    @Test
    public void TestCreateLink_Valid_ShouldPass() throws BadRequestException, NotFoundException, InternalServerErrorException {
        List<LinkVsnTo> vsnTos = controller.getAll();
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");

        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);

        long id = controller.create(to);
        long expectedId = vsnTos.size() + 1;
        Assert.assertEquals(expectedId, id);
    }

    @Test
    public void TestGetAllLink_Valid_ShouldPass() throws NotFoundException, BadRequestException, InternalServerErrorException {
        List<LinkVsnTo> vsnTos = controller.getAll();
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);

        controller.create(to);
        List<LinkVsnTo> vsnTos1 = controller.getAll();
        long expectedSize = vsnTos.size() + 1;
        Assert.assertEquals(expectedSize, vsnTos1.size());
    }

    @Test
    public void TestGetLink_Valid_ShouldPass() throws NotFoundException, BadRequestException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);

        long id = controller.create(to);
        LinkVsnTo vsnTo = controller.get(id);
        Assert.assertEquals(to, vsnTo);
        Assert.assertEquals(id, vsnTo.getId());
    }

    @Test(expected = NotFoundException.class)
    public void TestGetLink_InvalidIdRange_ShouldThrowException() throws NotFoundException, BadRequestException, InternalServerErrorException {
        LinkVsnTo vsnTo = controller.get(10000);
    }

    @Test(expected = BadRequestException.class)
    public void TestUpdateLink_NullParams_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);

        long id = controller.create(to);

        String expectedSensorId = null;
        String expectedCollectorId = null;
        String expectedNetworkId = null;

        LinkVsnTo to1 = new LinkVsnTo(expectedSensorId, expectedCollectorId, expectedNetworkId);

        controller.update(id, to1);
    }

    @Test(expected = BadRequestException.class)
    public void TestUpdateLink_NullFields_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);

        long id = controller.create(to);

        String expectedSensorId = "sensor1";
        String expectedCollectorId = "collector1";
        String expectedNetworkId = "network1";

        LinkVsnTo to1 = new LinkVsnTo(expectedSensorId, expectedCollectorId, expectedNetworkId);

        controller.update(id, to1);
    }

    @Test
    public void TestUpdateLink_NoFieldUpdate_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);

        long id = controller.create(to);

        String expectedSensorId = "sensor1";
        String expectedCollectorId = "collector1";
        String expectedNetworkId = "network1";

        LinkVsnTo lvt = controller.get(id);

        LinkVsnTo to1 = new LinkVsnTo(expectedSensorId, expectedCollectorId, expectedNetworkId);

        for (FieldTo field : lvt.getFields()) {
            to1.createField(field.getId(), field.getName(), field.getDataTypeId(), field.getConverterId());
        }

        controller.update(id, to1);

        LinkVsnTo vsnTo = controller.get(id);

        Assert.assertEquals(id, vsnTo.getId());
        Assert.assertEquals(expectedSensorId, vsnTo.getSensorId());
        Assert.assertEquals(expectedCollectorId, vsnTo.getCollectorId());
        Assert.assertEquals(expectedNetworkId, vsnTo.getNetworkId());

        List<? extends FieldTo> list = vsnTo.getFields();
        int expectedFieldListSize = 2;
        Assert.assertEquals(expectedFieldListSize, list.size());
    }
    @Test
    public void TestUpdateLink_AddRemoveField_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        to.createField("luminosity", dt1);

        long id = controller.create(to);

        String expectedSensorId = "sensor1";
        String expectedCollectorId = "collector1";
        String expectedNetworkId = "network1";

        String expectedFieldName = "temperature";
        long expectedFieldDataTypeId = dt3;
        long expectedFieldConversorId = cc2;

        LinkVsnTo lvt = controller.get(id);

        LinkVsnTo to1 = new LinkVsnTo(expectedSensorId, expectedCollectorId, expectedNetworkId);

        for (FieldTo field : lvt.getFields()) {
            to1.createField(field.getId(), field.getName(), field.getDataTypeId(), field.getConverterId());
            break;
        }

        to1.createField(expectedFieldName, expectedFieldDataTypeId, expectedFieldConversorId);

        FieldTo ft1 = lvt.getFields().get(0);
        FieldTo ft2 = lvt.getFields().get(1);

        controller.update(id, to1);

        LinkVsnTo vsnTo = controller.get(id);

        Assert.assertEquals(id, vsnTo.getId());
        Assert.assertEquals(expectedSensorId, vsnTo.getSensorId());
        Assert.assertEquals(expectedCollectorId, vsnTo.getCollectorId());
        Assert.assertEquals(expectedNetworkId, vsnTo.getNetworkId());

        List<? extends FieldTo> list = vsnTo.getFields();
        int expectedFieldListSize = 2;
        Assert.assertEquals(expectedFieldListSize, list.size());

        FieldTo field1 = null;
        for (FieldTo ft : list) {
            if (ft.getId() != ft1.getId()) {
                field1 = ft;
                break;
            }
        }

        Assert.assertEquals(expectedFieldName, field1.getName());
        Assert.assertEquals(expectedFieldDataTypeId, field1.getDataTypeId());
        Assert.assertEquals(expectedFieldConversorId, field1.getConverterId());
    }

    @Test
    public void TestUpdateLink_UpdateFields_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        to.createField("lumin", dt1);

        long id = controller.create(to);

        String expectedSensorId = "sensor1";
        String expectedCollectorId = "collector1";
        String expectedNetworkId = "network1";

        String expectedFieldName1 = "temperature";
        long expectedFieldDataTypeId1 = dt3;
        long expectedFieldConversorId1 = cc2;

        String expectedFieldName2 = "termic";
        long expectedFieldDataTypeId2 = dt1;
        long expectedFieldConversorId2 = 0;

        LinkVsnTo lvt = controller.get(id);

        LinkVsnTo to1 = new LinkVsnTo(expectedSensorId, expectedCollectorId, expectedNetworkId);

        FieldTo ft1 = lvt.getFields().get(0);
        FieldTo ft2 = lvt.getFields().get(1);
        FieldTo ft3 = lvt.getFields().get(2);

        to1.createField(ft1.getId(), expectedFieldName1, expectedFieldDataTypeId1, expectedFieldConversorId1);
        to1.createField(ft2.getId(), expectedFieldName2, expectedFieldDataTypeId2, expectedFieldConversorId2);
        to1.createField(ft3.getId(), ft3.getName(), ft3.getDataTypeId(), ft3.getConverterId());

        controller.update(id, to1);

        LinkVsnTo vsnTo = controller.get(id);

        Assert.assertEquals(id, vsnTo.getId());
        Assert.assertEquals(expectedSensorId, vsnTo.getSensorId());
        Assert.assertEquals(expectedCollectorId, vsnTo.getCollectorId());
        Assert.assertEquals(expectedNetworkId, vsnTo.getNetworkId());

        List<? extends FieldTo> list = vsnTo.getFields();
        int expectedFieldListSize = 3;
        Assert.assertEquals(expectedFieldListSize, list.size());

        FieldTo field1 = null;
        FieldTo field2 = null;
        for (FieldTo ft : list) {
            if (ft.getId() == ft1.getId()) {
                field1 = ft;
            }
            if (ft.getId() == ft2.getId()) {
                field2 = ft;
            }
        }

        Assert.assertEquals(expectedFieldName1, field1.getName());
        Assert.assertEquals(expectedFieldDataTypeId1, field1.getDataTypeId());
        Assert.assertEquals(expectedFieldConversorId1, field1.getConverterId());

        Assert.assertEquals(expectedFieldName2, field2.getName());
        Assert.assertEquals(expectedFieldDataTypeId2, field2.getDataTypeId());
        Assert.assertEquals(expectedFieldConversorId2, field2.getConverterId());
    }

    @Test(expected = BadRequestException.class)
    public void TestUpdateLink_UpdateFields_IncompatibleConversor_Invalid_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temp1", dt2, cc1);
        to.createField("temp2", dt1);
        to.createField("lumin", dt1);

        long id = controller.create(to);

        String expectedSensorId = "sensor1";
        String expectedCollectorId = "collector1";
        String expectedNetworkId = "network1";

        String expectedFieldName1 = "temperature";
        long expectedFieldDataTypeId1 = dt1;
        long expectedFieldConversorId1 = cc2;//incompatible conversor

        String expectedFieldName2 = "termic";
        long expectedFieldDataTypeId2 = dt1;
        long expectedFieldConversorId2 = 0;

        LinkVsnTo lvt = controller.get(id);

        LinkVsnTo to1 = new LinkVsnTo(expectedSensorId, expectedCollectorId, expectedNetworkId);

        FieldTo ft1 = lvt.getFields().get(0);
        FieldTo ft2 = lvt.getFields().get(1);
        FieldTo ft3 = lvt.getFields().get(2);

        to1.createField(ft1.getId(), expectedFieldName1, expectedFieldDataTypeId1, expectedFieldConversorId1);
        to1.createField(ft2.getId(), expectedFieldName2, expectedFieldDataTypeId2, expectedFieldConversorId2);
        to1.createField(ft3.getId(), ft3.getName(), ft3.getDataTypeId(), ft3.getConverterId());

        controller.update(id, to1);

    }

    @Test
    public void TestUpdateLink_UpdateFields_WithValues_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, InternalServerErrorException, BadRequestException {
        LinkVsnTo to = new LinkVsnTo("1", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        long id = controller.create(to);

        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.updateValues(sampleCoTo);

        String expectedFieldName1 = "temp";
        long expectedFieldDataTypeId1 = dt2;
        long expectedFieldConversorId1 = cc4;

        LinkVsnTo lvt = controller.get(id);

        LinkVsnTo to1 = new LinkVsnTo("1", "collector", "network");

        FieldTo ft1 = lvt.getFields().get(0);
        FieldTo ft2 = lvt.getFields().get(1);

        to1.createField(ft1.getId(), expectedFieldName1, expectedFieldDataTypeId1, expectedFieldConversorId1);
        to1.createField(ft2.getId(), ft2.getName(), ft2.getDataTypeId(), ft2.getConverterId());

        boolean updated = controller.update(id, to1);
        Assert.assertEquals(true, updated);
    }

    @Test(expected = BadRequestException.class)
    public void TestUpdateLink_ChangeDataTypeFields_WithValues_Invalid_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, InternalServerErrorException, BadRequestException {
        LinkVsnTo to = new LinkVsnTo("1", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        long id = controller.create(to);

        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.updateValues(sampleCoTo);

        String expectedFieldName1 = "temp";
        long expectedFieldDataTypeId1 = dt1;
        long expectedFieldConversorId1 = cc3;

        LinkVsnTo lvt = controller.get(id);

        LinkVsnTo to1 = new LinkVsnTo("1", "collector", "network");

        FieldTo ft1 = lvt.getFields().get(0);
        FieldTo ft2 = lvt.getFields().get(1);

        to1.createField(ft1.getId(), expectedFieldName1, expectedFieldDataTypeId1, expectedFieldConversorId1);
        to1.createField(ft2.getId(), ft2.getName(), ft2.getDataTypeId(), ft2.getConverterId());

        controller.update(id, to1);
    }

    @Test(expected = BadRequestException.class)
    public void TestUpdateLink_RemoveFields_WithValues_Invalid_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, InternalServerErrorException, BadRequestException {
        LinkVsnTo to = new LinkVsnTo("1", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        long id = controller.create(to);

        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.updateValues(sampleCoTo);

        LinkVsnTo lvt = controller.get(id);

        LinkVsnTo to1 = new LinkVsnTo("1", "collector", "network");

        FieldTo ft1 = lvt.getFields().get(0);
        FieldTo ft2 = lvt.getFields().get(1);

        to1.createField(ft2.getId(), ft2.getName(), ft2.getDataTypeId(), ft2.getConverterId());

        boolean updated = controller.update(id, to1);
//        Assert.assertEquals(true, updated);

//        LinkVsnTo lvt1 = controller.get(id);
//
//        int expectedListSize = 1;
//        Assert.assertEquals(expectedListSize, lvt1.getFields().size());
//
//        FieldTo to2 = lvt1.getFields().get(0);
//        Assert.assertEquals(true, to2.isInitialized());
    }

    @Test
    public void TestUpdateLink_AddConverterInFields_WithValues_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, InternalServerErrorException, BadRequestException {
        LinkVsnTo to = new LinkVsnTo("1", "collector", "network");
        to.createField("temperature", dt2);
        to.createField("temperature", dt1);
        long id = controller.create(to);

        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.updateValues(sampleCoTo);

        LinkVsnTo lvt = controller.get(id);

        LinkVsnTo to1 = new LinkVsnTo("1", "collector", "network");

        FieldTo ft1 = lvt.getFields().get(0);
        FieldTo ft2 = lvt.getFields().get(1);

        to1.createField(ft1.getId(), ft1.getName(), ft1.getDataTypeId(), cc1);
        to1.createField(ft2.getId(), ft2.getName(), ft2.getDataTypeId(), ft2.getConverterId());

        boolean updated = controller.update(id, to1);
        Assert.assertEquals(true, updated);

        LinkVsnTo lvt1 = controller.get(id);

        int expectedListSize = 2;
        Assert.assertEquals(expectedListSize, lvt1.getFields().size());
    }

    @Test
    public void TestUpdateLink_CheckValues_CheckFieldIsInitialized_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, InternalServerErrorException, BadRequestException {
        LinkVsnTo to = new LinkVsnTo("2", "collector", "network");
        to.createField("temperature", dt2);
        to.createField("temperature", dt1);
        long id = controller.create(to);

        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector","2");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.updateValues(sampleCoTo);

        LinkVsnTo lvt = controller.get(id);

        LinkVsnTo to1 = new LinkVsnTo("2", "collector", "network");

        FieldTo ft1 = lvt.getFields().get(0);
        FieldTo ft2 = lvt.getFields().get(1);

        Assert.assertEquals(false, ft1.isInitialized());
        Assert.assertEquals(true, ft2.isInitialized());

        to1.createField(ft1.getId(), ft1.getName(), ft1.getDataTypeId(), cc1);
        to1.createField(ft2.getId(), ft2.getName(), ft2.getDataTypeId(), ft2.getConverterId());

        boolean updated = controller.update(id, to1);
        Assert.assertEquals(true, updated);

        //new sample
        sampleCoTo = builder.generateSample();
        notifycontroller.updateValues(sampleCoTo);

        LinkVsnTo lvt1 = controller.get(id);
        int expectedListSize = 2;
        Assert.assertEquals(expectedListSize, lvt1.getFields().size());

        ft1 = lvt1.getFields().get(0);
        ft2 = lvt1.getFields().get(1);

        Assert.assertEquals(true, ft1.isInitialized());
        Assert.assertEquals(true, ft2.isInitialized());
    }
    
    @Test(expected = BadRequestException.class)
    public void TestUpdateLink_RemoveLogicallyField_WithValue_Invalid_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, InternalServerErrorException, BadRequestException {
        LinkVsnTo to = new LinkVsnTo("1", "collector", "network");
        to.createField("temperature", dt2,cc1);
        to.createField("temperature", dt1);
        long id = controller.create(to);

        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.updateValues(sampleCoTo);

        LinkVsnTo lvt = controller.get(id);

        LinkVsnTo to1 = new LinkVsnTo("1", "collector", "network");

        FieldTo ft1 = lvt.getFields().get(0);
        FieldTo ft2 = lvt.getFields().get(1);

        Assert.assertEquals(true, ft1.isInitialized());
        Assert.assertEquals(true, ft2.isInitialized());

        to1.createField(ft1.getId(), ft1.getName(), ft1.getDataTypeId(), cc1);

        boolean updated = controller.update(id, to1);
//        Assert.assertEquals(true, updated);
//
//        LinkVsnTo lvt1 = controller.get(id);
//        int expectedListSize = 1;
//        Assert.assertEquals(expectedListSize, lvt1.getFields().size());
    
    }

    @Test
    public void TestDeleteLink_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, InternalServerErrorException, BadRequestException {
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);

        long id = controller.create(to);
        boolean result = controller.delete(id);
        Assert.assertEquals(true, result);
    }

    @Test(expected = NotFoundException.class)
    public void TestDeleteLink_InvalidIdRange_ShouldThrowException() throws NotFoundException, BadRequestException, InternalServerErrorException, MethodNotAllowedException {
        controller.delete(10000);
    }
}
