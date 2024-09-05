package com.example.TourPlanner.view;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.model.TourLog;
import com.example.TourPlanner.service.TourLogService;
import com.example.TourPlanner.service.TourService;
import com.example.TourPlanner.viewModel.TourLogViewModel;
import com.example.TourPlanner.viewModel.TourViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@Component
public class MainController {
    private static final Logger logger = LogManager.getLogger(TourViewModel.class.getName());

    private final ApplicationContext springContext;
    private final TourViewModel tourViewModel;
    private final TourService tourService;
    private final TourLogViewModel tourLogViewModel;
    private final TourLogService tourLogService;
    public VBox tourDetailsContainer;
    public VBox tourDetailsInclude;
   // public TableColumn tourLogTourID;

    @FXML
    private ListView<Tour> tourListView;
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
    private VBox tourListViewContainer;
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
    private Label routeInfoLabel;
    @FXML
    private ImageView routeImageView;
    //TourLog
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
    @FXML
    private Label successLabel;
    @FXML
    private TableView<TourLog> tourLogTableView = new TableView<>();
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



    @Autowired
    public MainController(ApplicationContext springContext, TourViewModel tourViewModel, TourService tourService, TourLogViewModel tourLogViewModel, TourLogService tourLogService) {
        this.springContext = springContext;
        this.tourViewModel = tourViewModel;
        this.tourService = tourService;
        this.tourLogViewModel = tourLogViewModel;
        this.tourLogService = tourLogService;

    }



    @FXML
    public void initialize() {

            if (tourViewModel != null) {
                // Initialize the ListView for Tours
                tourListView.setItems(tourViewModel.getTours());
                tourListView.setCellFactory(lv -> new ListCell<Tour>() {
                    @Override
                    protected void updateItem(Tour item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : item.getName());  // Display the name of the Tour
                    }
                });

                // Bind the selected item to the selectedTourProperty
                tourListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        tourViewModel.selectedTourProperty().set(newSelection.getName());
                    }
                });

                // Handle double-clicks to display tour details
                tourListView.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        displayTourDetails();
                    }
                });

                // Listen to changes in the tour list and update ComboBox accordingly
                tourViewModel.getTours().addListener((ListChangeListener<Tour>) change -> {
                    while (change.next()) {
                        if (change.wasAdded() || change.wasRemoved()) {
                            updateTourIdComboBox();
                        }
                    }
                });
            }

                // Initial population of the ComboBox (for selecting a tourId )
                updateTourIdComboBox();

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
                            displayTourDetails();
                        }
                    });
                }

                tourLogTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
                    if (newValue != null) {
                        displayTourDetails(newValue);
                    }
                } );

               // System.out.println("-----------------------------TEST_TEST_TEST_TEST_TEST_TEST_TEST_TEST_TEST_TEST_TEST_TEST_TEST-----------------------");
               // System.out.println("TourId: " + tourIdComboBox);

        }





    private void updateTourIdComboBox() {
        if (tourViewModel != null && tourIdComboBox != null) {
            // Clear the existing items in the ComboBox
            tourIdComboBox.getItems().clear();

            // Fetch the list of tours from the TourViewModel
            List<Tour> tours = tourViewModel.getTours();

            // Add tour names to the ComboBox
            if (tours != null) {
                for (Tour tour : tours) {
                    if (tour != null) {
                        tourIdComboBox.getItems().add(tour.getTourID());
                    }
                }
            }

            // Optionally, set a prompt text if the ComboBox is empty
            if (tourIdComboBox.getItems().isEmpty()) {
                tourIdComboBox.setPromptText("No tours available");
            } else {
                tourIdComboBox.setPromptText("");  // Clear the prompt text if items are available
            }
        }
    }




    ///////////////////////////TOUR TOUR TOUR TOUR TOUR TOUR TOUR /////////////////////////////////////////
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

        if (tourName.isEmpty() || tourDescription.isEmpty() || tourFrom.isEmpty() || tourTo.isEmpty() || transportType == null) {
            successLabel.setText("All fields must be filled out!");
            successLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        Tour tour = new Tour(tourName, tourDescription, tourFrom, tourTo, transportType);
        tourService.addTour(tour);

        //update the tour in the viewModel
        tourViewModel.updateTour(tour);

        successLabel.setText("Tour saved successfully!");

        tourNameField.setText("");
        tourDescriptionField.setText("");
        tourFromField.setText("");
        tourToField.setText("");
        tourTransportTypeLabel.setText("");


    }

    @FXML
    private void handleCancel() {
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
            // remove directly from observable List
            tourViewModel.getTours().removeIf(tour -> tour.getName().equals(selectedTour));
        }
    }


    @FXML
    private void handleEditTour() throws IOException {
        String selectedTour = tourViewModel.selectedTourProperty().get();
        if (selectedTour != null) {
            Tour tourToEdit = tourViewModel.getTourDetails(selectedTour);

            if (tourToEdit != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TourPlanner/editTour.fxml"));
                fxmlLoader.setControllerFactory(springContext::getBean);

                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                EditTourController editTourController = fxmlLoader.getController();
                editTourController.setTourToEdit(tourToEdit);
                stage.setTitle("Edit Tour");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            }
        }
    }

    private void displayTourDetails() {
        String selectedTour = tourViewModel.selectedTourProperty().get();
        Tour tourDetails = tourViewModel.getTourDetails(selectedTour);

        if (tourDetails != null) {
            initData(tourDetails);
        }
    }

    public void initData(Tour tour) {
        if (tour == null) {
            return;
        }

        tourIdLabel.setText("Tour ID: \t\t\t" + tour.getTourID());
        tourNameLabel.setText("Name: \t\t\t" + tour.getName());
        tourFromLabel.setText("From: \t\t\t" + tour.getTourFrom());
        tourToLabel.setText("To: \t\t\t\t" + tour.getTourTo());
        tourTransportTypeLabel.setText("Transport Type:  \t" + tour.getTransportType());
        tourDescriptionLabel.setText("Description: \t\t" + tour.getDescription());

        if (tour.getEstimatedTime() != null) {
            tourEstimatedTimeLabel.setText("Estimated Time: \t" + tour.getEstimatedTime());
        } else {
            tourEstimatedTimeLabel.setText("Estimated Time: \tNot available");
        }

        if (tour.getTourDistance() != null) {
            tourDistanceLabel.setText("Tour Distance:  \t" + tour.getTourDistance());
        } else {
            tourDistanceLabel.setText("Tour Distance:  \tNot available");
        }

        if(tour.getRouteInfos() !=null) {
            routeInfoLabel.setText("Route Infos:  \t\t" + tour.getRouteInfos());
        }
        else {
            routeInfoLabel.setText("Route Infos:  \t\tNot available");
        }
    }


//-----------------------------------------------TOUR LOG------------------------------------------------------------------
    //TourLOG

    // when clicking on a TourLog, its corresponding Tourdetail shall display
    private void displayTourDetails(TourLog selectedTourlog) {
        Long tourId = selectedTourlog.getTourId();

        //fetch tour details throught the ID
        Tour tourDetails = tourService.getTourById(tourId);

        if(tourDetails != null) {
            initData(tourDetails);
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
        logger.info("Where is my Date? : " + logDate);

        String comment = commentField.getText();
        String difficulty = difficultyComboBox.getValue();
        Integer duration = durationSpinner.getValue();
        Double distance = distanceSpinner.getValue();
        String rating = ratingComboBox.getValue();
        Long tourID = tourIdComboBox.getValue();
        logger.info("selectedTourId: " + tourID);

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

         updateTourIdComboBox();
        // Save the TourLog using the service
        tourLogService.addTourLog(tourLog);

        // Update the tour log in the ViewModel to reflect changes in the UI
         tourLogViewModel.refreshTourLogs();

        successLabel.setText("Tour log saved successfully!");
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

            // Remove the TourLog from the ViewModel
            tourLogViewModel.removeTourLog(selectedTourLog.getTourlogID());
        }
        
    }

    public void handleEditTourLog() throws IOException {

        TourLog selectedTourLog = tourLogTableView.getSelectionModel().getSelectedItem();
        if (selectedTourLog != null) {
            // Make your changes to the selectedTourLog here
            TourLog tourLogToEdit = tourLogViewModel.getTourLogByID(selectedTourLog.getTourlogID());

            if (tourLogToEdit != null) {
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


        successLabel.setText("");
        Stage stage = (Stage) datePicker.getScene().getWindow();
        stage.close();

    }
}
