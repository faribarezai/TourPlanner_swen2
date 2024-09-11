package com.example.TourPlanner.view;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.model.TourLog;
import com.example.TourPlanner.service.TourLogService;
import com.example.TourPlanner.service.TourService;
import com.example.TourPlanner.viewModel.TourViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class MainController {
    private static final Logger logger = LogManager.getLogger(TourViewModel.class.getName());

    private final ApplicationContext springContext;
    private final TourViewModel tourViewModel;
    private final TourService tourService;
    private TourLogController tourLogController;

    public VBox tourDetailsInclude;
    public AnchorPane mapView;
    public AnchorPane tourLogsInclude;
    private TourLogService tourLogService;


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
    private Label popularityLabel;
    @FXML
    private Label childFriendlinessLabel;




    @Autowired
    public MainController(ApplicationContext springContext, TourViewModel tourViewModel, TourService tourService) {
        this.springContext = springContext;
        this.tourViewModel = tourViewModel;
        this.tourService = tourService;

    }

    @Autowired
    public void setTourLogController(TourLogController tourLogController) {
        this.tourLogController = tourLogController;
    }

    @FXML
    private void handleTourSelection() {
        tourListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayTourDetails();
            }
        });
    }

    @FXML
    public void initialize() {

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

                // TourSelection
                handleTourSelection();

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


        //Fulltext search
        initializeSearchTextField();


    }



    private void initializeSearchTextField() {
        FilteredList<Tour> filteredList = new FilteredList<>(FXCollections.observableArrayList(tourListView.getItems()));
        tourListView.setItems(filteredList);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(tour -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return tour.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
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
        Tour selectedTour = tourListView.getSelectionModel().getSelectedItem();
       logger.info("Selected Tour: "+ selectedTour.getTourID());
        logger.info("Selected Tour Name: "+ selectedTour.getName());
long tourId= selectedTour.getTourID();

        Tour tourDetails = tourViewModel.getTourDetails(selectedTour.getName());

        if (tourDetails != null) {
            // Calculate and set computed attributes using the calculator
            int popularity = calculatePopularity(tourId);
            String childFriendliness = calculateChildFriendliness(tourId);

            tourDetails.setPopularity(popularity);
            tourDetails.setChildFriendliness(childFriendliness);

            initData(tourDetails);

            // Fetch and display tour logs
            Long tour_Id = tourDetails.getTourID();
            List<TourLog> tourLogs = tourLogService.getTourLogsByTourId(tour_Id);
            //tourLogTableView.getItems().setAll(tourLogs);
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

        // Compute and display popularity and child-friendliness
        if (tour.getPopularity() != null) {
            popularityLabel.setText("Popularity: \t\t" +calculatePopularity(tour.getTourID()));
        } else {
            popularityLabel.setText("Popularity: \t\tNot available");
        }

        if (tour.getChildFriendliness() != null) {
            childFriendlinessLabel.setText("Child-Friendliness: \t" + calculateChildFriendliness(tour.getTourID()));
        } else {
            childFriendlinessLabel.setText("Child-Friendliness: \tNot available");
        }



    }


    @Autowired
    public void setTourLogService(TourLogService tourLogService) {
        this.tourLogService = tourLogService;
    }

    @FXML
    private void handleGenerateReport() {

        logger.info("generate report Methode!!!");
        String pdfFileName = "TourReport.pdf";
/*
        try (PdfWriter writer = new PdfWriter(pdfFileName);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {

            // Add a title to the document
            document.add(new Paragraph("Tour Report")
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold());

            String selectedTourName = tourViewModel.selectedTourProperty().get();
            Tour tour = tourViewModel.getTourDetails(selectedTourName);

            if (tour != null) {
                document.add(new Paragraph("Tour ID: " + tour.getTourID()));
                document.add(new Paragraph("Name: " + tour.getName()));
                document.add(new Paragraph("From: " + tour.getTourFrom()));
                document.add(new Paragraph("To: " + tour.getTourTo()));
                document.add(new Paragraph("Transport Type: " + tour.getTransportType()));
                document.add(new Paragraph("Description: " + tour.getDescription()));
                document.add(new Paragraph("Estimated Time: " + (tour.getEstimatedTime() != null ? tour.getEstimatedTime() : "Not available")));
                document.add(new Paragraph("Tour Distance: " + (tour.getTourDistance() != null ? tour.getTourDistance() : "Not available")));
                document.add(new Paragraph("Route Infos: " + (tour.getRouteInfos() != null ? tour.getRouteInfos() : "Not available")));

                // Add a section for tour logs
               // List<TourLog> tourLogs = tourLogService.getTourLogsByTourId(tour.getTourID());
                if (tourLogs != null && !tourLogs.isEmpty()) {
                    document.add(new Paragraph("Tour Logs:").setBold().setFontSize(16).setMarginTop(20));

                    Table table = new Table(new float[]{1, 2, 2, 3, 2, 2}); // Define column widths
                    table.addHeaderCell(new Cell().add(new Paragraph("Log ID")));
                    table.addHeaderCell(new Cell().add(new Paragraph("Date")));
                    table.addHeaderCell(new Cell().add(new Paragraph("Difficulty")));
                    table.addHeaderCell(new Cell().add(new Paragraph("Comment")));
                    table.addHeaderCell(new Cell().add(new Paragraph("Duration")));
                    table.addHeaderCell(new Cell().add(new Paragraph("Distance")));

                    for (TourLog log : tourLogs) {
                        table.addCell(new Cell().add(new Paragraph(String.valueOf(log.getTourlogID()))));
                        table.addCell(new Cell().add(new Paragraph(log.getDate().toString())));
                        table.addCell(new Cell().add(new Paragraph(log.getDifficulty())));
                        table.addCell(new Cell().add(new Paragraph(log.getComment())));
                        table.addCell(new Cell().add(new Paragraph(String.valueOf(log.getDuration()))));
                        table.addCell(new Cell().add(new Paragraph(String.valueOf(log.getDistance()))));
                    }

                    document.add(table);
                } else {
                    document.add(new Paragraph("No logs available for this tour."));
                }
*/
        /*

                successLabel.setText("PDF report generated successfully!");
                successLabel.setStyle("-fx-text-fill: green;");
            } else {
                successLabel.setText("Selected tour not found.");
                successLabel.setStyle("-fx-text-fill: red;");
            }

        } catch (IOException e) {
            logger.error("Error generating PDF report", e);
            successLabel.setText("Failed to generate PDF report.");
            successLabel.setStyle("-fx-text-fill: red;");
        }
        */
    }







// computed attributes

    public int calculatePopularity(Long tourId) {
        List<TourLog> logs = tourLogService.getTourLogsByTourId(tourId);

        logger.info("Log size i popularity: " + logs.size());
        return logs.size(); // Number of logs indicates popularity
    }



    public String calculateChildFriendliness(Long tourId) {
        List<TourLog> logs = tourLogService.getTourLogsByTourId(tourId);
        if (logs.isEmpty()) return "Not available";

        logger.info("child friendliness log size: " + logs.size());

       // String totalDifficulty = "";
        double totalDistance = 0.0;

        for (TourLog log : logs) {
           // totalDifficulty += log.getDifficulty();
            totalDistance += log.getDistance();
        }


        // formula for child-friendliness
        if (totalDistance <= 5) {
            return "Very child-friendly";
        } else if (totalDistance <= 10) {
            return "Moderately child-friendly";
        } else {
            return "Less child-friendly";
        }
    }



    // Export &  Import
    @FXML
    private void handleExportTours() {
        List<Tour> tours = tourViewModel.getTours();
        exportToursToFile(tours);
    }

    @FXML
    private void handleImportTours() {
         importToursFromFile();

    }

public void exportToursToFile(List<Tour> tours) {
    // Create a new directory for exporting tours
    String exportDir = "exportedTours";
    File dir = new File(exportDir);
    if (!dir.exists()) {
        dir.mkdirs();
    }

    ObjectMapper mapper = new ObjectMapper();

    for (Tour tour : tours) {
        Hibernate.initialize(tour.getTourLogs()); // Initialize the tour logs collection

        String tourName = tour.getName();
        String fileName = tourName + ".json";
        String filePath = exportDir + "/" + fileName;

        try {
            String json = mapper.writeValueAsString(tour);

            // Write to a JSON file
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(json);
            fileWriter.close();
            logger.info("file exported successfully!");

        } catch (IOException e) {
            logger.error("Error exporting tour to file", e);
        }
    }
}


    public void importToursFromFile() {
        // Create a new directory for importing tours
        String importDir = "exportedTours";
        File dir = new File(importDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".json"));
        for (File file : files) {
            String fileName = file.getName();
            String tourName = fileName.substring(0, fileName.length() - 5); // remove ".json" extension

            try {
                ObjectMapper mapper = new ObjectMapper();
                FileReader fileReader = new FileReader(file);
                Tour tour = mapper.readValue(fileReader, Tour.class);
                tourViewModel.getTours().add(tour);
                logger.info("file imported successfully!");
            } catch (IOException e) {
                logger.error("Error importing tour from file", e);
            }
        }
    }

}