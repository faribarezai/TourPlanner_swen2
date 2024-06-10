package com.example.TourPlanner;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.service.TourService;
import com.example.TourPlanner.viewModel.TourViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainController {
    private final ApplicationContext springContext;

    private final TourViewModel tourViewModel;
    private final TourService tourService;

    @Autowired
    public MainController(ApplicationContext springContext, TourViewModel tourViewModel, TourService tourService) {
        this.springContext = springContext;
        this.tourViewModel = tourViewModel;
        this.tourService = tourService;
    }

    @FXML
    private ListView<String> tourListView;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField tourNameField;

    @FXML
    private TextField tourDescriptionField;

    @FXML
    private TextField tourFromField;

    @FXML
    private TextField tourToField;

    @FXML
    private TextField transportTypeField;

    @FXML
    public void initialize() {
        // Using injected tourViewModel
        if (tourViewModel != null) {
            tourListView.setItems(tourViewModel.getTours());
            tourViewModel.selectedTourProperty().bind(tourListView.getSelectionModel().selectedItemProperty());
        }
    }

    @FXML
    private void handleAddTour() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TourPlanner/addTour.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        //fxmlLoader.setController(this);
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();
        stage.setTitle("Add New Tour");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private void handleSaveTour() {
        String tourName = tourNameField.getText();
        String tourDescription = tourDescriptionField.getText();
        String tourFrom = tourFromField.getText();
        String tourTo = tourToField.getText();
        String transportType = transportTypeField.getText();

        if (tourName != null && !tourName.isEmpty()) {
            Tour tour = new Tour(tourName, tourDescription, tourFrom, tourTo, transportType);
            tourService.addTour(tour);
        }
        Stage stage = (Stage) tourNameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) tourNameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleRemoveTour() {
        String selectedTour = tourViewModel.selectedTourProperty().get();
        if (selectedTour != null) {
            tourViewModel.removeTour(selectedTour);
        }
    }

    @FXML
    private void handleEditTour() {
        String selectedTour = tourViewModel.selectedTourProperty().get();
        if (selectedTour != null) {
            tourViewModel.editTour(selectedTour, "Edited Tour");
        }
    }
}
