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
package br.uff.labtempo.osiris.monitor.controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SensorNetSensorController {

    @FXML
    private VBox vBox;
    
    @FXML
    private Label labelId;
    @FXML
    private Label labelModel;
    @FXML
    private Label labelDate;
    @FXML
    private Label listValues;

    public SensorNetSensorController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SensorNetSensor.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(SensorSnTo sensor) {
        labelId.setText(sensor.getId());
        labelModel.setText("micaz");
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sensor.getAcquisitionTimestampInMillis());        
        labelDate.setText(generate(calendar));
        
        
        StringBuilder sb = new StringBuilder();
        
        for (Map<String, String> value : sensor.getValues()) {           
            
            sb.append(value.get("name")+": "+value.get("value")+" "+value.get("symbol"));
            sb.append("\n");
        }
        
        listValues.setText(sb.toString());

    }

    public VBox getBox() {
        return vBox;
    }
    
    private String generate(Calendar calendar) {
        return getFormat().format(calendar.getTime());
    }

    private SimpleDateFormat getFormat() {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format;
    }

}
