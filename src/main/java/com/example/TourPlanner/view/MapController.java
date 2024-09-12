package com.example.TourPlanner.view;

import com.example.TourPlanner.viewModel.TourViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javafx.concurrent.Worker;

@Component
public class MapController {

    private static final Logger logger = LogManager.getLogger(TourViewModel.class.getName());

    @FXML
    public Label mapErrorLabel;

    @FXML
    private WebView mapWebView;

    private WebEngine webEngine;

@Autowired
    public MapController(ApplicationContext springContext) {
}

    @FXML
    public void initialize() {
        webEngine = mapWebView.getEngine();

        // Set the initial map URL
        String osmUrl = "https://www.openstreetmap.org/";
        webEngine.load(osmUrl);

        // Add a listener to handle errors
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.FAILED) {
                mapErrorLabel.setVisible(true);
            }
        });
    }


    public void handleRouteButton() {

        // Update the map URL based on the button click
        String newUrl = "https://www.openstreetmap.org/#map=12/37.7749/-122.4194"; // example URL
        webEngine.load(newUrl);
        logger.info("Route button displays the Map Route");

    }

}
