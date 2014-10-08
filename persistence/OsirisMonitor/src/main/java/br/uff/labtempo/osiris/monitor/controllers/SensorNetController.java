/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.monitor.controllers;

import br.uff.labtempo.osiris.monitor.model.Sensor;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Felipe
 */
public class SensorNetController implements Initializable {

    @FXML
    private ListView<String> listViewNetworks;

    @FXML
    private ListView<Sensor> listViewSensors;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (listViewNetworks == null) {
            listViewNetworks = new ListView<>();
        }

        ObservableList<String> items = listViewNetworks.getItems();

        items.add("Network One");
        items.add("Network Two");
        items.add("Network Three");
        items.add("Network Four");
        items.add("Network Five");

        setListView();

//        ObservableList<String> items2 = listViewSensors.getItems();
//
//        items2.add("Sensor\n One");
//        items2.add("Sensor\n Two");
//        items2.add("Sensor\n Three");
//        items2.add("Sensor\n Four");
//        items2.add("Sensor\n Five");
    }

    public void setListView() {
        Set<Sensor> stringSet = new HashSet<>();
        ObservableList observableList = FXCollections.observableArrayList();
        Map<String, String> v1 = new HashMap<>();
        Map<String, String> v2 = new HashMap<>();
        Map<String, String> v3 = new HashMap<>();
        Map<String, String> v4 = new HashMap<>();

        v1.put("Temperatura", "20");
        v1.put("Luminosidade", "30");
        v1.put("Bateria", "10");

        v2.put("Temperatura", "24");
        v2.put("Luminosidade", "35");
        v2.put("Bateria", "16");

        v3.put("Temperatura", "21");
        v3.put("Luminosidade", "32");
        v3.put("Bateria", "13");

        v4.put("Temperatura", "46");
        v4.put("Luminosidade", "45");
        v4.put("Bateria", "13");

        stringSet.add(new Sensor("01", "Micaz", Calendar.getInstance(), v1));
        stringSet.add(new Sensor("02", "Iris", Calendar.getInstance(), v2));
        stringSet.add(new Sensor("03", "Micaz", Calendar.getInstance(), v3));
        stringSet.add(new Sensor("04", "Micaz", Calendar.getInstance(), v4));
        observableList.setAll(stringSet);
        listViewSensors.setItems(observableList);
        listViewSensors.setCellFactory(new Callback<ListView<Sensor>, javafx.scene.control.ListCell<Sensor>>() {
            @Override
            public ListCell<Sensor> call(ListView<Sensor> listView) {
                return  new ListViewCell();
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
