package com.example.TourPlanner.viewModel;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.service.TourService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TourViewModel {

    private static final Logger logger = Logger.getLogger(TourViewModel.class.getName());

    private final TourService tourService;
    private final ObservableList<String> tours = FXCollections.observableArrayList();
    private final StringProperty selectedTour = new SimpleStringProperty();

    @Autowired
    public TourViewModel(TourService tourService) {
        this.tourService = tourService;
       // loadTours();

       try {
            loadTours();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred while loading tours", e);
            throw e; // Re-throw to ensure Spring context is aware of the failure
        }
    }

    private void loadTours() {
       List<Tour> list = tourService.getAllTours();
       logger.log(Level.INFO, "Beginning of loadTours Method in TourViewModel!!");

        try {
            for (Tour tour : list) {
                tours.add(tour.getName());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred while loading tours from service", e);
            throw e; // Ensure exceptions are not swallowed
        }
    }

    public ObservableList<String> getTours() {
        return tours;
    }

    public StringProperty selectedTourProperty() {
        return selectedTour;
    }

    public void addTour(String name) {
        try {
            Tour tour = new Tour(name);
            tourService.addTour(tour);
            tours.add(name);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred while adding tour", e);
            throw e;
        }
    }

    public void removeTour(String name) {
        try {
            Tour tour = tourService.getTourByName(name);
            if (tour != null) {
                tourService.deleteTourById(tour.getTourID());
                tours.remove(name);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred while removing tour", e);
            throw e;
        }
    }

    public void editTour(String oldName, String newName) {
        try {
            Tour tour = tourService.getTourByName(oldName);
            if (tour != null) {
                tour.setName(newName);
                tourService.addTour(tour); // Assuming this updates if ID exists
                tours.remove(oldName);
                tours.add(newName);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred while editing tour", e);
            throw e;
        }
    }
}
