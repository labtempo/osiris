package br.uff.labtempo.osiris.monitor.controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import br.uff.labtempo.osiris.collector.temp.Sensor;
import br.uff.labtempo.osiris.sensornet.to.CollectorSnTo;
import br.uff.labtempo.osiris.sensornet.to.SensorSnTo;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Felipe
 */
public class SensorNetCollectorController {

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

    public SensorNetCollectorController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SensorNetSensor.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(CollectorSnTo collector) {
        labelId.setText(collector.getId());
        labelModel.setText("sw");
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(collector.getModified());        
        labelDate.setText(generate(calendar));
        
        
        StringBuilder sb = new StringBuilder();
        
        for (Map.Entry<String, String> value : collector.getInfo().entrySet()) {           
            
            sb.append(value.getKey()+": "+value.getValue()+"\n");
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
