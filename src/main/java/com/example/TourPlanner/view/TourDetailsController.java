package com.example.TourPlanner.view;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.viewModel.TourViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class TourDetailsController {
    private static final Logger logger = LogManager.getLogger(TourViewModel.class.getName());

    @FXML
    private Label tourIdLabel;

    @FXML
    private Label tourNameLabel;

    @FXML
    private Label tourFromLabel;

    @FXML
    private Label tourToLabel;

    @FXML
    private Label tourTransportTypeLabel;

    @FXML
    private Label tourDescriptionLabel;

    @FXML
    private Label tourEstimatedTimeLabel;

    @FXML
    private Label tourDistanceLabel;

    @FXML
    private ImageView routeImageView;

    @Setter
    private MainController mainController;

    public void initialize() {
        // Initialize labels with default values or placeholders
     /*   tourIdLabel.setText("---");
        tourNameLabel.setText("---");
        tourFromLabel.setText("---");
        tourToLabel.setText("---");
        tourTransportTypeLabel.setText("---");
        tourDescriptionLabel.setText("---");
        tourEstimatedTimeLabel.setText("---");
        tourDistanceLabel.setText("/");

      */
    }


    public void initData(Tour tour) {
        if (tour == null) {
            logger.warn("Tour object passed to initData() is null.");
            return;
        }

        logger.info("see the detail of tour please! are they visible?: ");
        // Log the values to ensure they are correct
        logger.info("Tour ID: " + tour.getTourID());
        logger.info("Tour Name: " + tour.getName());
        logger.info("Tour From: " + tour.getTourFrom());
        logger.info("Tour To: " + tour.getTourTo());


        tourIdLabel.setText("TourId: " + tour.getTourID());
        tourNameLabel.setText(tour.getName());
        tourFromLabel.setText(tour.getTourFrom());
        tourToLabel.setText(tour.getTourTo());
        tourTransportTypeLabel.setText("Transport Type: " + tour.getTransportType());
        tourDescriptionLabel.setText("Description: " + tour.getDescription());
        // Set estimated time and distance if they are available
        // Example of handling nullable properties
        if (tour.getEstimatedTime() != null) {
            tourEstimatedTimeLabel.setText("Estimated time: " + tour.getEstimatedTime());
        } else {
            tourEstimatedTimeLabel.setText("Estimated time: Not available");
        }

        if (tour.getTourDistance() != null) {
            tourDistanceLabel.setText("Tour distance: " + tour.getTourDistance());
        } else {
            tourDistanceLabel.setText("Tour distance: Not available");
        }

       // tourEstimatedTimeLabel.setText("Estimated time:  no time calculated yet!"); //+ tour.getEstimatedTime());
        // tourDistanceLabel.setText("Tour distance: no distance calculated yet!"); // + tour.getTourDistance());
        // Load the map image into the ImageView
        // This is a placeholder. You'll need to update this to load your actual map image.
       // Image mapImage = new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAEklEQVR4nGMAAQAABQABDQottAAAAABJRU5ErkJggg==");
       // routeImageView.setImage(mapImage);

        logger.info("All details are displayed!");
    }

}

