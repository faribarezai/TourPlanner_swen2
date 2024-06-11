package com.example.TourPlanner;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.service.TourService;
import com.example.TourPlanner.viewModel.TourViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class EditTourController {
    private final ApplicationContext springContext;
    private final TourViewModel tourViewModel;
    private final TourService tourService;
    public Label successLabel;

    @Autowired
    public EditTourController(ApplicationContext springContext, TourViewModel tourViewModel, TourService tourService) {
        this.springContext = springContext;
        this.tourViewModel = tourViewModel;
        this.tourService = tourService;
    }

    @FXML
    private ListView<String> tourListView;

    @FXML
    private TextField searchTextField;

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

    private Tour tour;

    public void handleUpdateTour() {
        String tourName = tourNameField.getText();
        String tourDescription = tourDescriptionField.getText();
        String tourFrom = tourFromField.getText();
        String tourTo = tourToField.getText();
        String transportType = transportTypeComboBox.getValue();


        // Update the tour details
        tour.setName(tourName);
        tour.setDescription(tourDescription);
        tour.setTourFrom(tourFrom);
        tour.setTourTo(tourTo);
        tour.setTransportType(transportType);

        // when no texField is empty, add Tour to DB via tour service
        tourService.updateTour(tour);

        // tour successfully added
        successLabel.setText("Tour updated successfully!");
        Stage stage = (Stage) tourNameField.getScene().getWindow();
        stage.close();

    }


    public void setTourToEdit(Tour tourToEdit) {
        this.tour= tourToEdit;
        // Populate the fields with the details of the selected tour
        tourNameField.setText(tour.getName());
        tourDescriptionField.setText(tour.getDescription());
        tourFromField.setText(tour.getTourFrom());
        tourToField.setText(tour.getTourTo());
        transportTypeComboBox.setValue(tour.getTransportType());

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




}
