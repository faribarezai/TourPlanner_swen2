package com.example.TourPlanner.viewModel;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.service.TourService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




@Component
public class TourViewModel {

    private static final Logger logger = LogManager.getLogger(TourViewModel.class.getName());

    private final TourService tourService;
    @Getter
    private ObservableList<Tour> tours = FXCollections.observableArrayList();
    private final StringProperty selectedTour = new SimpleStringProperty();

    @Autowired
    public TourViewModel(TourService tourService) {
        this.tourService = tourService;

       try {
            loadTours();
        } catch (Exception e) {
            logger.error("Exception occurred while loading tours",e);
            throw e; // Re-throw to ensure Spring context is aware of the failure
        }
    }


    public void loadTours() {
        tours.setAll(tourService.getAllTours());

    }


    public StringProperty selectedTourProperty() {
        return selectedTour;
    }


    public void removeTour(String name) {
        try {
            Tour tour = tourService.getTourByName(name);
            if (tour != null) {
                tourService.deleteTourById(tour.getTourID());
                logger.info("Tour deleted successfully!");
                tours.remove(tour); // before it was name and underlined yellow
            }
        } catch (Exception e) {
            logger.error("Exception occurred while removing tour", e);
            throw e;
        }
    }


    public Tour getTourDetails(String selectedTour) {
            return tourService.getTourByName(selectedTour);
    }

    public void updateTour(Tour updateTour) {
        tourService.updateTour(updateTour);
        loadTours();

    }


}
