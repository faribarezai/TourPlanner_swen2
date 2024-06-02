/**
package com.example.tourplanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;


public class TourController {

    @FXML
    private ListView<String> tourListView= new ListView<>();

    public void initialize() {// Initialize the ListView
        // Create sample tour objects
        Tour tour1 = new Tour("Tour 1");
        Tour tour2 = new Tour("Tour 2");
        Tour tour3 = new Tour("Tour 3");

        // Populate the ListView with tour names
        tourListView.getItems().addAll(tour1.getName(), tour2.getName(), tour3.getName());
    }

}
*/