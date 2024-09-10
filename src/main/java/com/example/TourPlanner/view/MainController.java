package com.example.TourPlanner.view;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.model.TourLog;
import com.example.TourPlanner.service.TourLogService;
import com.example.TourPlanner.service.TourService;
import com.example.TourPlanner.viewModel.TourLogViewModel;
import com.example.TourPlanner.viewModel.TourViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
    private final TourLogController tourLogController;


    public VBox tourDetailsInclude;
    public AnchorPane mapView;
    public AnchorPane tourLogsInclude;


    @FXML
    private TextField searchTextField;
    private final ObservableList<Tour> tourList= FXCollections.observableArrayList();

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
    @FXML
    private Label successLabel;
    @FXML
    private ComboBox<Long> tourIdComboBox;
    @FXML
    private TableView<TourLog> tourLogTableView = new TableView<>();



    @Autowired
    public MainController(ApplicationContext springContext, TourViewModel tourViewModel, TourService tourService, TourLogController tourLogController) {
        this.springContext = springContext;
        this.tourViewModel = tourViewModel;
        this.tourService = tourService;
        this.tourLogController = tourLogController;
    }

    @FXML
    public void initialize() {

        //searchbar
        // load tours from DB
        tourList.addAll(tourViewModel.getTours());

        // Add a listener to the searchTextField to update the filter
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            tourListView.setItems(filterTour(newValue));
        });



        //tourListView
        if (tourViewModel != null) {
                // Initialize the ListView for Tours
                tourListView.setItems(tourViewModel.getTours());
                tourListView.setCellFactory(lv -> new ListCell<>() {
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

                // Handle double-clicks to display tour details and its tourlogs in the tourLogTableView
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

        }




    private ObservableList<Tour> filterTour(String searchtext) {
        ObservableList<Tour> filteredTours= FXCollections.observableArrayList();
        tourListView.getItems().clear();
        for(Tour tour: tourList){
            if(tour.getName().toLowerCase().contains(searchtext.toLowerCase())) {
                filteredTours.add(tour);
            }
        }
        return filteredTours;
    }


    void updateTourIdComboBox() {
        if (tourViewModel != null && tourIdComboBox != null) {
            // Clear the existing items in the ComboBox
            tourIdComboBox.getItems().clear();

            // Fetch the list of tours from the TourViewModel
            tourViewModel.loadTours();
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
            if (tourIdComboBox.getItems().isEmpty())
                tourIdComboBox.setPromptText("No tours available");

            logger.info("ComboBOx updated successfully!");
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


    void displayTourDetails() {
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

}