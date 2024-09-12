package com.example.TourPlanner.view;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.model.TourLog;
import com.example.TourPlanner.service.TourService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

import com.example.TourPlanner.service.TourLogService;
import com.example.TourPlanner.viewModel.TourLogViewModel;
import com.example.TourPlanner.viewModel.TourViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@Component
public class TourLogController {
    private static final Logger logger = LogManager.getLogger(TourViewModel.class.getName());

    private final ApplicationContext springContext;
    private final TourLogViewModel tourLogViewModel;
    private final TourLogService tourLogService;
    private final TourService tourService;
    private MainController mainController;


        // TourLog
        @FXML
        private DatePicker datePicker;
        @FXML
        private ComboBox<String> difficultyComboBox;
        @FXML
        private TextField commentField;
        @FXML
        private Spinner<Integer> durationSpinner; // in min
        @FXML
        private Spinner<Double> distanceSpinner; // in km
        @FXML
        private ComboBox<String> ratingComboBox;
        @FXML
        private ComboBox<Long> tourIdComboBox;
        @FXML
        private Label successLabel;
        @FXML
        private TableView<TourLog> tourLogTableView;
        @FXML
        private TableColumn<TourLog, LocalDate> tourLogDate;
        @FXML
        private TableColumn<TourLog, Integer> tourLogDuration;
        @FXML
        private TableColumn<TourLog, Double> tourLogDistance;
        @FXML
        private TableColumn<TourLog, String> tourLogDifficulty;
        @FXML
        private TableColumn<TourLog, String> tourLogRating;
        @FXML
        private TableColumn<TourLog, String> tourLogComment;
        @FXML
        private TableColumn<TourLog, Long> tourLogTourId;

        //...

    @Autowired
    public TourLogController(ApplicationContext springContext, TourLogViewModel tourLogViewModel, TourLogService tourLogService, TourService tourService) {
        this.springContext = springContext;
        this.tourLogViewModel = tourLogViewModel;
        this.tourLogService = tourLogService;
        this.tourService = tourService;


    }

    @Autowired
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
     public void initialize() {

        // Initialize TableColumns
        if (tourLogDate != null) tourLogDate.setCellValueFactory(new PropertyValueFactory<>("date")); //String "date" is from model
        if (tourLogDuration != null) tourLogDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        if (tourLogDistance != null) tourLogDistance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        if (tourLogDifficulty != null) tourLogDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        if (tourLogRating != null) tourLogRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        if (tourLogComment != null) tourLogComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        if(tourLogTourId !=null) tourLogTourId.setCellValueFactory(new PropertyValueFactory<>("tour_id"));


        // Set items for the TableView
        if (tourLogViewModel != null) {
            tourLogTableView.setItems(tourLogViewModel.getTourLogs());

            // Handle double-clicks to display tour details
            tourLogTableView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    mainController.displayTourDetails();
                }
            });
        }

        tourLogTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if (newValue != null) {
                displayTourDetails(newValue);
            }
        } );

     }


    // when clicking on a TourLog, its corresponding Tour detail shall display
    private void displayTourDetails(TourLog selectedTourlog) {
        Long tourId = selectedTourlog.getTourId();
        //fetch tour details through the ID
        Tour tourDetails = tourService.getTourById(tourId);

        if (tourDetails != null) {
        mainController.initData(tourDetails);
    }

    }

    // ADD into Tourlog
    @FXML
    public void handleAddTourLog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TourPlanner/addTourLog.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();
        stage.setTitle("Add New TourLog");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }


    @FXML
    public void handleSaveTourLog() {
        // Retrieve the selected date from DatePicker
        LocalDate logDate = datePicker.getValue();
        String comment = commentField.getText();
        String difficulty = difficultyComboBox.getValue();
        Integer duration = durationSpinner.getValue();
        Double distance = distanceSpinner.getValue();
        String rating = ratingComboBox.getValue();
        Long tourID = tourIdComboBox.getValue();

        // Check if all required fields are filled out
        if (logDate == null || comment.isEmpty() || difficulty == null || duration == null || distance == null || rating == null) {
            successLabel.setText("All fields must be filled out!");
            successLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // Retrieve the Tour entity from the database by its ID
        Tour selectedTour = tourService.getTourById(tourID);

        if (selectedTour == null) {
            successLabel.setText("Selected tour not found!");
            successLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // Create and persist the TourLog
        TourLog tourLog = new TourLog(logDate, comment, difficulty, duration, distance, rating);
        tourLog.setTour(selectedTour);


        mainController.updateTourIdComboBox();
        // Save the TourLog using the service
        tourLogService.addTourLog(tourLog);

        // Update the tour log in the ViewModel to reflect changes in the UI
        tourLogViewModel.refreshTourLogs();

        successLabel.setText("Tour log was saved successfully!");
        successLabel.setStyle("-fx-text-fill: green;");

        // Reset all fields for adding a new TourLog
        datePicker.setValue(null);
        commentField.setText("");
        difficultyComboBox.setValue(null);
        durationSpinner.getValueFactory().setValue(1);
        distanceSpinner.getValueFactory().setValue(0.1);
        ratingComboBox.setValue(null);
        tourIdComboBox.setValue(null);

    }




    public void handleRemoveTourLog() {
        TourLog selectedTourLog = tourLogTableView.getSelectionModel().getSelectedItem();
        if (selectedTourLog != null) {
            // Remove the TourLog from the database
            tourLogService.deleteTourLogById(selectedTourLog.getTourlogID());
            logger.info("TourLog was removed from DB!");
            // Remove the TourLog from the ViewModel
            tourLogViewModel.removeTourLog(selectedTourLog.getTourlogID());
            logger.info("TourLog was removed from ViewModel!");
        }

    }

    public void handleEditTourLog() throws IOException {

        TourLog selectedTourLog = tourLogTableView.getSelectionModel().getSelectedItem();
        if (selectedTourLog != null) {
            // Make your changes to the selectedTourLog here
            TourLog tourLogToEdit = tourLogViewModel.getTourLogByID(selectedTourLog.getTourlogID());


            if (tourLogToEdit != null) {
                logger.info("TourLog was found!");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TourPlanner/editTourLog.fxml"));
                fxmlLoader.setControllerFactory(springContext::getBean);

                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                EditTourLogController editTourLogController = fxmlLoader.getController();
                editTourLogController.setTourLogToEdit(tourLogToEdit);
                stage.setTitle("Edit TourLog");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            }
            else {
                logger.info("TourLog does not exist in the DB!");
            }


            // Update the tour log in the ViewModel
            tourLogViewModel.updateTourLog(selectedTourLog);
        }
    }



    public void handleTourLogCancel() {
        // Clear all input fields
        datePicker.setValue(null);
        commentField.clear();
        difficultyComboBox.getSelectionModel().clearSelection();
        durationSpinner.getValueFactory().setValue(0); // Reset to default value
        distanceSpinner.getValueFactory().setValue(0.0); // Reset to default value
        ratingComboBox.getSelectionModel().clearSelection();
        tourIdComboBox.getSelectionModel().clearSelection();

        logger.info("Exit TourLog!");
        successLabel.setText("");
        Stage stage = (Stage) datePicker.getScene().getWindow();
        stage.close();

    }



}