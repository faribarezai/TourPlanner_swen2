package com.example.TourPlanner.view;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.model.TourLog;
import com.example.TourPlanner.service.TourLogService;
import com.example.TourPlanner.service.TourService;
import com.example.TourPlanner.viewModel.TourLogViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
    public class EditTourLogController {
        private final ApplicationContext springContext;
        private final TourLogViewModel tourLogViewModel;
        private final TourLogService tourLogService;
        private final TourService tourService;
        public Label successLabel;

        @Autowired
        public EditTourLogController(ApplicationContext springContext, TourLogViewModel tourLogViewModel, TourLogService tourLogService, TourService tourService) {
            this.springContext = springContext;
            this.tourLogViewModel = tourLogViewModel;
            this.tourLogService = tourLogService;
            this.tourService = tourService;
        }

        @FXML
        private DatePicker datePicker;
        @FXML
        private ComboBox<String> difficultyComboBox;
        @FXML
        private TextField commentField;
        @FXML
        private Spinner<Integer> durationSpinner;

        @FXML
        private Spinner<Double> distanceSpinner;

        @FXML
        private ComboBox<String> ratingComboBox;
        @FXML
        private ComboBox<Long> tourIdComboBox;


        private TourLog tourLog;

        public void setTourLogToEdit(TourLog tourLogToEdit) {
            this.tourLog= tourLogToEdit;
            // Populate the fields with the details of the selected tour
            datePicker.setValue(tourLog.getDate());
            difficultyComboBox.setValue(tourLog.getDifficulty());
            commentField.setText(tourLog.getComment());
            durationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, tourLog.getDuration()));
            distanceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, tourLog.getDistance()));

            ratingComboBox.setValue(tourLog.getRating());

            List<Long> tourIds = tourService.getAllTourIds();
            tourIdComboBox.getItems().setAll(tourIds);

        }

    @FXML
    public void handleUpdateTourLog() {
        // Retrieve values from the fields
        LocalDate logDate = datePicker.getValue();
        String difficulty = difficultyComboBox.getValue();
        String comment = commentField.getText();
        Integer duration = durationSpinner.getValue();
        Double distance = distanceSpinner.getValue();
        String rating = ratingComboBox.getValue();
        Long tourId = tourIdComboBox.getValue();


        // Update the tour log details
        tourLog.setDate(logDate);
        tourLog.setDifficulty(difficulty);
        tourLog.setComment(comment);
        tourLog.setDuration(duration);
        tourLog.setDistance(distance);
        tourLog.setRating(rating);


        // retrieve the Tour by ID
        Tour selectedTour = tourService.getTourById(tourId);
        tourLog.setTourId(tourId);


        if (selectedTour != null) {

            tourLog.setTour(selectedTour);

            // Update the tour log in the database via the service
            tourLogService.updateTourLog(tourLog);

            // Update the tour log in the ViewModel to reflect changes in the UI
            tourLogViewModel.refreshTourLogs();

            // Display success message
            successLabel.setText("Tour log updated successfully!");

        } else {
            // tour is not found
            successLabel.setText("Selected tour not found!");
            return;
        }
/*
        // Update the tour log in the database via the service
        tourLogService.updateTourLog(tourLog);

        // Update the tour log in the ViewModel to reflect changes in the UI
        tourLogViewModel.refreshTourLogs();

        // Display success message
        successLabel.setText("Tour log updated successfully!");
*/
        // Close the window
        Stage stage = (Stage) datePicker.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        // Clear all fields
        datePicker.setValue(null);
        difficultyComboBox.setValue(null);
        commentField.clear();
        durationSpinner.getValueFactory().setValue(1);
        distanceSpinner.getValueFactory().setValue(0.1);
        ratingComboBox.setValue(null);
        tourIdComboBox.setValue(null);
        successLabel.setText("");

        // Close the window
        Stage stage = (Stage) datePicker.getScene().getWindow();
        stage.close();
    }

}