package com.example.TourPlanner.viewModel;


import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.service.TourService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainViewModel {
    private final TourService tourService;
    private final ObservableList<Tour> tours;
    private final StringProperty searchQuery;

    public MainViewModel() {
        this.tourService = new TourService();
        this.tours = FXCollections.observableArrayList(tourService.getAllTours());
        this.searchQuery = new SimpleStringProperty();
    }

    public ObservableList<Tour> getTours() {
        return tours;
    }

    public StringProperty searchQueryProperty() {
        return searchQuery;
    }
/*
    public void addTour(Tour tour) {
        tourService.addTour(tour);
        tours.add(tour);
    }
    */

    // Other ViewModel logic
}
