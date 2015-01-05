package br.uff.labtempo.osiris.monitor.controllers;

import br.uff.labtempo.osiris.monitor.omcp.DataDao;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private ListView<SensorSnTo> listViewSensors;

    @FXML
    private ListView<CollectorSnTo> listViewCollectors;

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
                    try {
                        listViewCollectors.getSelectionModel().clearSelection();
                        listViewSensors.getSelectionModel().clearSelection();
                    } catch (Exception ex) {
                    }
                    setSensorListView(dao.getSensors(nid));
                    setCollectorListView(dao.getCollectors(nid));
                } catch (Exception ex) {
                    Logger.getLogger(SensorNetController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        listViewCollectors.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String nid = listViewNetworks.getSelectionModel().getSelectedItem();
                String cid = listViewCollectors.getSelectionModel().getSelectedItem().getId();
                try {
                    try {
                        listViewSensors.getSelectionModel().clearSelection();
                    } catch (Exception ex) {
                    }
                    setSensorListView(dao.getSensors(nid, cid));
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

    public void setSensorListView(List<SensorSnTo> sensors) {
        ObservableList observableList = FXCollections.observableArrayList();
        observableList.setAll(sensors);
        listViewSensors.setItems(observableList);
        listViewSensors.setCellFactory(new Callback<ListView<SensorSnTo>, javafx.scene.control.ListCell<SensorSnTo>>() {
            @Override
            public ListCell<SensorSnTo> call(ListView<SensorSnTo> listView) {
                return new SensorListViewCell();
            }
        });
    }

    public void setCollectorListView(List<CollectorSnTo> collectors) {
        ObservableList observableList = FXCollections.observableArrayList();
        observableList.setAll(collectors);
        listViewCollectors.setItems(observableList);
        listViewCollectors.setCellFactory(new Callback<ListView<CollectorSnTo>, javafx.scene.control.ListCell<CollectorSnTo>>() {
            @Override
            public ListCell<CollectorSnTo> call(ListView<CollectorSnTo> listView) {
                return new CollectorListViewCell();
            }
        });
    }

    public class SensorListViewCell extends ListCell<SensorSnTo> {

        @Override
        public void updateItem(SensorSnTo sensor, boolean empty) {
            super.updateItem(sensor, empty);
            if (sensor != null) {
                SensorNetSensorController data = new SensorNetSensorController();
                data.setInfo(sensor);
                setGraphic(data.getBox());
            }
        }
    }

    public class CollectorListViewCell extends ListCell<CollectorSnTo> {

        @Override
        public void updateItem(CollectorSnTo collector, boolean empty) {
            super.updateItem(collector, empty);
            if (collector != null) {
                SensorNetCollectorController data = new SensorNetCollectorController();
                data.setInfo(collector);
                setGraphic(data.getBox());
            }
        }
    }

}
