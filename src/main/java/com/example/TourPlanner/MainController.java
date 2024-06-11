package com.example.TourPlanner;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.service.TourService;
import com.example.TourPlanner.viewModel.TourViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainController {
    private final ApplicationContext springContext;

    private final TourViewModel tourViewModel;
    private final TourService tourService;
    public Label successLabel;


    @Autowired
    public MainController(ApplicationContext springContext, TourViewModel tourViewModel, TourService tourService) {
        this.springContext = springContext;
        this.tourViewModel = tourViewModel;
        this.tourService = tourService;
    }

    @FXML
    private ListView<String> tourListView;

    @FXML
    private ComboBox<String> transportTypeComboBox;

    @FXML
    private TextField tourNameField;

    @FXML
    private TextField tourDescriptionField;

    @FXML
    private TextField tourFromField;

    @FXML
    private TextField tourToField;


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
        String transportType = transportTypeComboBox.getValue();

        // Validate input fields
        if (tourName.isEmpty() || tourDescription.isEmpty() || tourFrom.isEmpty() || tourTo.isEmpty() || transportType == null) {
            successLabel.setText("All fields must be filled out!");
            successLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        // when no texField is empty, add Tour to DB via tour service
        Tour tour = new Tour(tourName, tourDescription, tourFrom, tourTo, transportType);
        tourService.addTour(tour);

        Stage stage = (Stage) tourNameField.getScene().getWindow();
        // tour successfully added
        successLabel.setText("Tour saved successfully!");

        //clear texFields, when saved
        tourNameField.setText("");
        tourDescriptionField.setText("");
        tourFromField.setText("");
        tourToField.setText("");

        //stage.close();
    }


    @FXML
    private void handleCancel() {
            // Implement your cancel logic here, if any
            // Clear all fields
            tourNameField.clear();
            tourDescriptionField.clear();
            tourFromField.clear();
            tourToField.clear();
            transportTypeComboBox.setValue("Bike");
            successLabel.setText("");

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
    private void handleEditTour() throws IOException {
        // Get the selected tour from the view model
        String selectedTour = tourViewModel.selectedTourProperty().get();
        if (selectedTour != null) {
            // Retrieve the tour details (you need to implement this method in your view model)
            Tour tourToEdit = tourViewModel.getTourDetails(selectedTour);

            // Check if the tour details are retrieved successfully
            if (tourToEdit != null) {
                // Open a new window or dialog to allow editing of tour details
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TourPlanner/editTour.fxml"));
                fxmlLoader.setControllerFactory(springContext::getBean);

                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                    EditTourController editTourController = fxmlLoader.getController();
                    editTourController.setTourToEdit(tourToEdit); // Pass the tour details to the controller for editing
                    stage.setTitle("Edit Tour");
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.setScene(scene);
                    stage.showAndWait();
            }
        }
    }
}
