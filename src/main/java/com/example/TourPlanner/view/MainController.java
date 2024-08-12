package com.example.TourPlanner.view;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.model.TourLog;
import com.example.TourPlanner.service.TourLogService;
import com.example.TourPlanner.service.TourService;
import com.example.TourPlanner.viewModel.TourLogViewModel;
import com.example.TourPlanner.viewModel.TourViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.stream.Collectors;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

@Component
public class MainController {
    private static final Logger logger = LogManager.getLogger(TourViewModel.class.getName());

    private final ApplicationContext springContext;
    private final TourViewModel tourViewModel;
    private final TourService tourService;
    private final TourLogViewModel tourLogViewModel;
    private final TourLogService tourLogService;

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
    private TableView<TourLog> tourLogTableView;


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
            // Set the items in the ListView with ObservableList<Tour>
            tourListView.setItems(tourViewModel.getTours());

            // Customize the ListView to display the Tour name
            tourListView.setCellFactory(lv -> new ListCell<Tour>() {
                @Override
                protected void updateItem(Tour item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getName()); // Display the name of the Tour
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
        }

        // Initialize distanceSpinner with double values and set a default value
        SpinnerValueFactory<Double> distanceFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 100.0, 1.0, 0.1);

        // Initialize durationSpinner with integer values and set a default value
        SpinnerValueFactory<Integer> durationFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1440, 30);

        List<Long> tourIds = tourService.getAllTourIds();
        logger.info("List of Tour Ids: " + tourIds);

        if (tourIds != null && !tourIds.isEmpty() && tourIdComboBox != null) {
            tourIdComboBox.getSelectionModel().selectFirst();
            tourIdComboBox.setItems(FXCollections.observableArrayList(tourIds));
        } else {
            if (tourIdComboBox != null) {
                tourIdComboBox.setItems(FXCollections.observableArrayList());
                tourIdComboBox.setPromptText("No Tours Available");
            }
        }
    }

/*


    @FXML
    public void initialize() {
        if (tourViewModel != null) {
           // List list= tourViewModel.getTours().stream().map(Tour::getName).collect(Collectors.toList());

            tourListView.setItems(tourViewModel.getTours());
            tourViewModel.selectedTourProperty().bind(tourListView.getSelectionModel().selectedItemProperty());

            tourListView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    displayTourDetails();
                }
            });
        }



        // Initialize distanceSpinner with double values and set a default value
        SpinnerValueFactory<Double> distanceFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 100.0, 1.0, 0.1);
       // distanceSpinner.setValueFactory(distanceFactory);
        //distanceSpinner.getValueFactory().setValue(1.0); // Set default value

        // Initialize durationSpinner with integer values and set a default value
        SpinnerValueFactory<Integer> durationFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1440, 30);
       // durationSpinner.setValueFactory(durationFactory);
        //durationSpinner.getValueFactory().setValue(30); // Set default value

        List<Long> tourIds = tourService.getAllTourIds();
        logger.info("List of Tour Ids: "+ tourIds);

        if (tourIds != null && !tourIds.isEmpty() && tourIdComboBox != null) {
          tourIdComboBox.getSelectionModel().selectFirst();
          tourIdComboBox.setItems(FXCollections.observableArrayList(tourIds));

        } else {
            if (tourIdComboBox != null) {
                tourIdComboBox.setItems(FXCollections.observableArrayList());
                tourIdComboBox.setPromptText("No Tours Available");
            }
        }


    }
    */


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
    }


//-----------------------------------------------TOUR LOG------------------------------------------------------------------
    //TourLOG
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

    public void handleRemoveTourLog() {
        long selectedTourLog = tourLogViewModel.selectedTourLogIdProperty().get();
        if (selectedTourLog >= 0) {
            tourLogViewModel.removeTourLog(selectedTourLog);
        }
    }

    public void handleEditTourLog() {
        // TODO
    }

    @FXML
    public void handleSaveTourLog() {
        // Retrieve selected date from DatePicker
        LocalDate logDate = datePicker.getValue();
        String comment = commentField.getText();
        String difficulty = difficultyComboBox.getValue();
        Integer duration = durationSpinner.getValue();
        Double distance = distanceSpinner.getValue();
        String rating = ratingComboBox.getValue();

        List<Long> tourIds = tourService.getAllTourIds();
        logger.info("List of Tour Ids: "+ tourIds);

        /*
        if (tourIds != null && !tourIds.isEmpty()) {
            tourIdComboBox.setItems(FXCollections.observableArrayList(tourIds));
        }


        List<Long> tourIds = tourService.getAllTourIds();
        */

        if (tourIds != null && !tourIds.isEmpty()) {
            tourIdComboBox.setItems(FXCollections.observableArrayList(tourIds));
           // tourIdComboBox.getSelectionModel().selectFirst(); // Optionally select the first item
        } else {
            tourIdComboBox.setItems(FXCollections.observableArrayList());
            tourIdComboBox.setPromptText("No Tours Available"); // Display a prompt text if no tours are available
        }


        Long selectedTourId = tourIdComboBox.getValue();

        if (logDate == null || comment.isEmpty() || difficulty == null || duration == null || distance == null || rating == null) {
            successLabel.setText("All fields must be filled out!");
            successLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        Tour selectedTour = tourService.getTourById(selectedTourId);
         // set the values to tourlog
        TourLog tourLog = new TourLog(logDate, comment, difficulty, duration, distance, rating);
        tourLog.setTour(selectedTour);
        tourLogService.addTourLog(tourLog);

        successLabel.setText("Tour log saved successfully!");

        datePicker.setValue(null);
        commentField.setText("");
        difficultyComboBox.setValue(null);
        durationSpinner.getValueFactory().setValue(1);
        distanceSpinner.getValueFactory().setValue(0.1);
        ratingComboBox.setValue(null);
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
