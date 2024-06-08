package com.example.TourPlanner;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.model.TourLog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainController {
    public Button addTour;


    @FXML
    private TableView<TourLog> tourLogTableView;

    @FXML
    private TableColumn<TourLog, String> dateColumn;

    @FXML
    private TableColumn<TourLog, String> durationColumn;

    @FXML
    private TableColumn<TourLog, String> distanceColumn;

    @FXML
    private ListView<String> tourListView = new ListView<>();

    public void initialize() {// Initialize the ListView
        // Create sample tour objects
        Tour tour1 = new Tour("Wienerwald");
        Tour tour2 = new Tour("Dopplerhütte");
        Tour tour3 = new Tour("Figlwarte");
        Tour tour4 = new Tour("Dorfrunde");

        // Populate the ListView with tour names
        tourListView.getItems().addAll(tour1.getName(), tour2.getName(), tour3.getName(), tour4.getName());

        tourListView.getItems().add("vestibulum et eros");

        // Sample data
    /*
        ObservableList<TourLog> tourLogs = FXCollections.observableArrayList(
                new TourLog(new LocalDate(2024,3,30), 2, 10),
                new TourLog(new LocalDate(2024, 5, 28 ),1.5, 8),
                new TourLog(new LocalDate(2024,4,1), 3, 15)
        );

        dateColumn.setCellValueFactory(data -> data.getValue().getDate());
        durationColumn.setCellValueFactory(data -> data.getValue().getDuration());
        distanceColumn.setCellValueFactory(data -> data.getValue().getDistance());


        // Set data to the TableView
        tourLogTableView.setItems(tourLogs);
    }*/


    }
}
