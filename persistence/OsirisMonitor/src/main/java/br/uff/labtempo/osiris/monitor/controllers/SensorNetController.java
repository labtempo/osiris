/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.monitor.controllers;


import br.uff.labtempo.osiris.collector.temp.Sensor;
import br.uff.labtempo.osiris.monitor.omcp.DataDao;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Felipe
 */
public class SensorNetController implements Initializable {

    @FXML
    public void handleMouseClick(MouseEvent arg0) {
        System.out.println("clicked on " + listViewNetworks.getSelectionModel().getSelectedItem());
    }
    @FXML
    private ListView<String> listViewNetworks;

    @FXML
    private ListView<Sensor> listViewSensors;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final DataDao dao = new DataDao("192.168.0.7", "admin", "admin");

        if (listViewNetworks == null) {
            listViewNetworks = new ListView<>();
        }
        
        listViewNetworks.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String nid = listViewNetworks.getSelectionModel().getSelectedItem();
                
                try {
                    setListView(dao.getSensors(nid));
                } catch (Exception ex) {
                    Logger.getLogger(SensorNetController.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            }
        });
        
        
        ObservableList<String> items = listViewNetworks.getItems();

        try {
            items.addAll(dao.getNetworks());

            //setListView();
//        ObservableList<String> items2 = listViewSensors.getItems();
//
//        items2.add("Sensor\n One");
//        items2.add("Sensor\n Two");
//        items2.add("Sensor\n Three");
//        items2.add("Sensor\n Four");
//        items2.add("Sensor\n Five");
        } catch (Exception ex) {
            Logger.getLogger(SensorNetController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setListView(List<Sensor> sensors) {
        ObservableList observableList = FXCollections.observableArrayList();
        observableList.setAll(sensors);
        listViewSensors.setItems(observableList);
        listViewSensors.setCellFactory(new Callback<ListView<Sensor>, javafx.scene.control.ListCell<Sensor>>() {
            @Override
            public ListCell<Sensor> call(ListView<Sensor> listView) {
                return new ListViewCell();
            }
        });
    }

    public class ListViewCell extends ListCell<Sensor> {

        @Override
        public void updateItem(Sensor sensor, boolean empty) {
            super.updateItem(sensor, empty);
            if (sensor != null) {
                SensorNetSensorController data = new SensorNetSensorController();
                data.setInfo(sensor);
                setGraphic(data.getBox());
            }
        }
    }

}
