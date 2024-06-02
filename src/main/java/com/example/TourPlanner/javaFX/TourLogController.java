
/**
package com.example.tourplanner;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TourLogController {
    @FXML
    private TableView<TourLog> tourLogTableView;

    @FXML
    private TableColumn<TourLog, String> dateColumn;

    @FXML
    private TableColumn<TourLog, String> durationColumn ;

    @FXML
    private TableColumn<TourLog, String> distanceColumn;

    public void initialize() {
        // Initialize columns
        dateColumn.setCellValueFactory(data -> data.getValue().getDate());
        durationColumn.setCellValueFactory(data -> data.getValue().getDuration());
        distanceColumn.setCellValueFactory(data -> data.getValue().getDistance());
    }

    public void setTourLogData(ObservableList<TourLog> tourLogs) {
        // Set data to the TableView
        tourLogTableView.setItems(tourLogs);
    }
}
*/
