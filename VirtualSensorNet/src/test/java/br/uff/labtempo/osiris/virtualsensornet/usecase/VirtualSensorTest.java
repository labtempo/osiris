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
import br.uff.labtempo.osiris.to.common.data.ValueTo;
import br.uff.labtempo.osiris.to.common.definitions.State;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.controller.ConverterController;
import br.uff.labtempo.osiris.virtualsensornet.controller.DataTypeController;
import br.uff.labtempo.osiris.virtualsensornet.controller.NotifyController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorLinkController;
import br.uff.labtempo.osiris.virtualsensornet.usecase.server.Bootstrap;
import br.uff.labtempo.osiris.virtualsensornet.usecase.server.CollectorDataBuilder;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
public class VirtualSensorTest {

    private static Bootstrap bootstrap;
    private static VirtualSensorController controller;
    private static VirtualSensorLinkController linkcontroller;
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
        controller = bootstrap.getVirtualSensorController();
        linkcontroller = bootstrap.getLinkController();
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

    @Test(expected = MethodNotAllowedException.class)
    public void testCreateVirtualSensor_Invalid_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException, MethodNotAllowedException {
        VirtualSensorVsnTo to = new VirtualSensorVsnTo(5, State.NEW, System.currentTimeMillis(), 0, 0, TimeUnit.MILLISECONDS, System.currentTimeMillis(), System.currentTimeMillis(), Calendar.getInstance(), VirtualSensorType.LINK);
        controller.create(to);
    }

    @Test(expected = MethodNotAllowedException.class)
    public void testCreateVirtualSensor_Null_ShouldThrowException() throws BadRequestException, NotFoundException, InternalServerErrorException, MethodNotAllowedException {
        controller.create(null);
    }

    @Test
    public void TestGetAllVirtualSensor_Valid_ShouldPass() throws NotFoundException, BadRequestException, InternalServerErrorException {
        List<VirtualSensorVsnTo> vsnTos = controller.getAll();

        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        linkcontroller.create(to);

        List<VirtualSensorVsnTo> vsnTos1 = controller.getAll();
        long expectedSize = vsnTos.size() + 1;
        Assert.assertEquals(expectedSize, vsnTos1.size());
    }

    @Test
    public void TestGetVirtualSensor_Valid_ShouldPass() throws NotFoundException, BadRequestException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);

        long id = linkcontroller.create(to);

        VirtualSensorVsnTo vsnTo = controller.get(id);
        Assert.assertEquals(id, vsnTo.getId());
    }

    @Test
    public void TestUpdateVirtualSensor_WithValues_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, InternalServerErrorException, BadRequestException {
        LinkVsnTo to = new LinkVsnTo("1", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);
        long id = linkcontroller.create(to);

        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.updateValues(sampleCoTo);

        VirtualSensorVsnTo lvt = controller.get(id);

        List<? extends ValueTo> list = lvt.getValuesTo();

        ValueTo to1 = list.get(0);
        ValueTo to2 = list.get(1);

        Assert.assertEquals("celsius", to2.getUnit());
        Assert.assertEquals("fahrenheit", to1.getUnit());

        Assert.assertTrue(Double.parseDouble(to2.getValue()) > 0);
        Assert.assertTrue(Double.parseDouble(to1.getValue()) > 0);

    }

    @Test(expected = NotFoundException.class)
    public void TestGetVirtualSensor_InvalidIdRange_ShouldThrowException() throws NotFoundException, BadRequestException, InternalServerErrorException {
        VirtualSensorVsnTo vsnTo = controller.get(10000);
    }

    @Test(expected = MethodNotAllowedException.class)
    public void TestUpdateVirtualSensor_Invalid_ShouldThrowException() throws NotFoundException, MethodNotAllowedException, BadRequestException, InternalServerErrorException {
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);

        long id = linkcontroller.create(to);

        VirtualSensorVsnTo to1 = new VirtualSensorVsnTo(5, State.NEW, System.currentTimeMillis(), 0, 0, TimeUnit.MILLISECONDS, System.currentTimeMillis(), System.currentTimeMillis(), Calendar.getInstance(), VirtualSensorType.LINK);

        controller.update(id, to1);
    }

    @Test(expected = MethodNotAllowedException.class)
    public void TestDeleteVirtualSensor_Valid_ShouldPass() throws NotFoundException, MethodNotAllowedException, InternalServerErrorException, BadRequestException {
        LinkVsnTo to = new LinkVsnTo("sensor", "collector", "network");
        to.createField("temperature", dt2, cc1);
        to.createField("temperature", dt1);

        long id = linkcontroller.create(to);
        boolean result = controller.delete(id);
        Assert.assertEquals(true, result);
    }

    @Test(expected = MethodNotAllowedException.class)
    public void TestDeleteVirtualSensor_InvalidIdRange_ShouldThrowException() throws NotFoundException, BadRequestException, InternalServerErrorException, MethodNotAllowedException {
        controller.delete(10000);
    }
}
