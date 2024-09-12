package com.example.TourPlanner.view;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.model.TourLog;
import com.example.TourPlanner.service.TourLogService;
import com.example.TourPlanner.service.TourService;
import com.example.TourPlanner.viewModel.TourViewModel;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.Document;
import java.io.*;


import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
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

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.itextpdf.kernel.pdf.PdfName.Document;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Element;

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
    @FXML
    private Button generateSingleTourReportButton;



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

        logger.info("Generate report btn clicked! !!!");
        String pdfFileName = "reports/TourReport.pdf";

        try {
            // Create the reports folder if it doesn't exist
            File reportsFolder = new File("reports");
            if (!reportsFolder.exists()) {
                reportsFolder.mkdirs();
            }

            PdfWriter writer = new PdfWriter(pdfFileName);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add a title to the document
            document.add(new Paragraph("All Tours")
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold());

            // Get all tours
            List<Tour> tours = tourViewModel.getTours();

            // Iterate through tours and add them to PDF
            for (Tour tour : tours) {
                document.add(new Paragraph("\n"));
                document.add(new Paragraph("Tour ID: " + tour.getTourID()));
                document.add(new Paragraph("Name: " + tour.getName()));
                document.add(new Paragraph("From: " + tour.getTourFrom()));
                document.add(new Paragraph("To: " + tour.getTourTo()));
                document.add(new Paragraph("Transport Type: " + tour.getTransportType()));
                document.add(new Paragraph("Description: " + tour.getDescription()));
                document.add(new Paragraph("Estimated Time: " + (tour.getEstimatedTime() != null ? tour.getEstimatedTime() : "Not available")));
                document.add(new Paragraph("Tour Distance: " + (tour.getTourDistance() != null ? tour.getTourDistance() : "Not available")));
                document.add(new Paragraph("Route Infos: " + (tour.getRouteInfos() != null ? tour.getRouteInfos() : "Not available")));
                document.add(new Paragraph("Popularity: " + (calculatePopularity(tour.getTourID()))));
                document.add(new Paragraph("Child Friendliness: " + (calculateChildFriendliness(tour.getTourID()))));

                // Add a section for tour logs
                List<TourLog> tourLogs = tourLogService.getTourLogsByTourId(tour.getTourID());
                if (tourLogs != null && !tourLogs.isEmpty()) {
                    document.add(new Paragraph("Tour Logs:").setBold().setFontSize(16).setMarginTop(20));


                    Paragraph paragraph = new Paragraph("TourLOG");
                    Cell cell = new Cell();

                    Table table = new Table(new float[]{1, 2, 2, 3, 2, 2}); // Define column widths
                    table.addCell("Log ID");
                    table.addCell("Date");
                    table.addCell("Difficulty");
                    table.addCell("Comment");
                    table.addCell("Duration");
                    table.addCell("Distance");

                    for (TourLog log : tourLogs) {
                        table.addCell(String.valueOf(log.getTourlogID()));
                        table.addCell(log.getDate().toString());
                        table.addCell(log.getDifficulty());
                        table.addCell(log.getComment());
                        table.addCell(String.valueOf(log.getDuration()));
                        table.addCell(String.valueOf(log.getDistance()));


                    }
                    document.add(table);
                } else {
                    document.add(new Paragraph("No logs available for this tour."));
                }
            }

            document.close();
           logger.info("PDF generated successfully!");
            successLabel.setText("PDF-Report for all Tours generated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        String exportDir = "exportedTours";
        File dir = new File(exportDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (Tour tour : tours) {
            Hibernate.initialize(tour.getTourLogs()); // Initialize the tour logs collection

            String tourName = tour.getName();
            String fileName = tourName + ".txt";
            String filePath = exportDir + "/" + fileName;

            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write("Tour ID: " + tour.getTourID() + "\n");
                fileWriter.write("Name: " + tour.getName() + "\n");
                fileWriter.write("From: " + tour.getTourFrom() + "\n");
                fileWriter.write("To: " + tour.getTourTo() + "\n");
                fileWriter.write("Transport Type: " + tour.getTransportType() + "\n");
                fileWriter.write("Description: " + tour.getDescription() + "\n");
                fileWriter.write("Estimated Time: " + (tour.getEstimatedTime() != null ? tour.getEstimatedTime() : "Not available") + "\n");
                fileWriter.write("Tour Distance: " + (tour.getTourDistance() != null ? tour.getTourDistance() : "Not available") + "\n");
                fileWriter.write("Route Infos: " + (tour.getRouteInfos() != null ? tour.getRouteInfos() : "Not available") + "\n");

                // Write tour logs
                List<TourLog> tourLogs = tour.getTourLogs();
                if (tourLogs != null && !tourLogs.isEmpty()) {
                    fileWriter.write("\nTour Logs:\n");
                    for (TourLog log : tourLogs) {
                        fileWriter.write("Log ID: " + log.getTourlogID() + "\n");
                        fileWriter.write("Date: " + log.getDate() + "\n");
                        fileWriter.write("Difficulty: " + log.getDifficulty() + "\n");
                        fileWriter.write("Comment: " + log.getComment() + "\n");
                        fileWriter.write("Duration: " + log.getDuration() + "\n");
                        fileWriter.write("Distance: " + log.getDistance() + "\n");
                        fileWriter.write("---------\n");
                    }
                } else {
                    fileWriter.write("No logs available for this tour.\n");
                }

                logger.info("File exported successfully!");
                successLabel.setText("Tours exported successfully!");

            } catch (IOException e) {
                logger.error("Error exporting tour to file", e);
            }
        }
    }





    public void importToursFromFile() {
        String importDir = "exportedTours";
        File dir = new File(importDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".txt"));
        if (files == null) {
            logger.error("No text files found for import.");
            return;
        }

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                Tour tour = new Tour();
                List<TourLog> tourLogs = new ArrayList<>();
                TourLog tourLog = null;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Tour ID:")) {
                        tour.setTourID(Long.parseLong(line.substring(8).trim()));
                    } else if (line.startsWith("Name:")) {
                        tour.setName(line.substring(5).trim());
                    } else if (line.startsWith("From:")) {
                        tour.setTourFrom(line.substring(5).trim());
                    } else if (line.startsWith("To:")) {
                        tour.setTourTo(line.substring(3).trim());
                    } else if (line.startsWith("Transport Type:")) {
                        tour.setTransportType(line.substring(16).trim());
                    } else if (line.startsWith("Description:")) {
                        tour.setDescription(line.substring(12).trim());
                    } else if (line.startsWith("Estimated Time:")) {
                        tour.setEstimatedTime(Time.valueOf(line.substring(15).trim()));
                    } else if (line.startsWith("Tour Distance:")) {
                        tour.setTourDistance(Double.valueOf(line.substring(14).trim()));
                    } else if (line.startsWith("Route Infos:")) {
                        tour.setRouteInfos(line.substring(12).trim());
                    } else if (line.startsWith("Log ID:")) {
                        if (tourLog != null) {
                            tourLogs.add(tourLog);
                        }
                        tourLog = new TourLog();
                        tourLog.setTourlogID(Long.parseLong(line.substring(7).trim()));
                    } else if (line.startsWith("Date:")) {
                        if (tourLog != null) {
                            tourLog.setDate(LocalDate.parse(line.substring(5).trim()));
                        }
                    } else if (line.startsWith("Difficulty:")) {
                        if (tourLog != null) {
                            tourLog.setDifficulty(line.substring(11).trim());
                        }
                    } else if (line.startsWith("Comment:")) {
                        if (tourLog != null) {
                            tourLog.setComment(line.substring(8).trim());
                        }
                    } else if (line.startsWith("Duration:")) {
                        if (tourLog != null) {
                            tourLog.setDuration(Integer.parseInt(line.substring(9).trim()));
                        }
                    } else if (line.startsWith("Distance:")) {
                        if (tourLog != null) {
                            tourLog.setDistance(Double.parseDouble(line.substring(9).trim()));
                        }
                    } else if (line.equals("---------")) {
                        if (tourLog != null) {
                            tourLogs.add(tourLog);
                            tourLog = null;
                        }
                    }
                }

                if (tourLog != null) {
                    tourLogs.add(tourLog);
                }

                tour.setTourLogs(tourLogs);
                tourViewModel.getTours().add(tour);
                logger.info("File imported successfully!");
                successLabel.setText("File imported successfully!");
            } catch (IOException e) {
                logger.error("Error importing tour from file", e);
            }
        }
    }


    public void generateSingleTourReport(Tour selectedTour) {
        logger.info("Generate single tour report btn clicked! !!!");
        String pdfFileName = "reports/" + selectedTour.getName() + " Report.pdf";

        try {
            // Create the reports folder if it doesn't exist
            File reportsFolder = new File("reports");
            if (!reportsFolder.exists()) {
                reportsFolder.mkdirs();
            }

            PdfWriter writer = new PdfWriter(pdfFileName);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add a title to the document
            document.add(new Paragraph("Single Tour Report")
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold());

            // Add tour details
            document.add(new Paragraph("Tour ID: " + selectedTour.getTourID()));
            document.add(new Paragraph("Name: " + selectedTour.getName()));
            document.add(new Paragraph("From: " + selectedTour.getTourFrom()));
            document.add(new Paragraph("To: " + selectedTour.getTourTo()));
            document.add(new Paragraph("Transport Type: " + selectedTour.getTransportType()));
            document.add(new Paragraph("Description: " + selectedTour.getDescription()));
            document.add(new Paragraph("Estimated Time: " + (selectedTour.getEstimatedTime() != null ? selectedTour.getEstimatedTime() : "Not available")));
            document.add(new Paragraph("Tour Distance: " + (selectedTour.getTourDistance() != null ? selectedTour.getTourDistance() : "Not available")));
            document.add(new Paragraph("Route Infos: " + (selectedTour.getRouteInfos() != null ? selectedTour.getRouteInfos() : "Not available")));
            document.add(new Paragraph("Popularity: " + (calculatePopularity(selectedTour.getTourID()))));
            document.add(new Paragraph("Child Friendliness: " + (calculateChildFriendliness(selectedTour.getTourID()))));

            // Add a section for tour logs
            List<TourLog> tourLogs = tourLogService.getTourLogsByTourId(selectedTour.getTourID());
            if (tourLogs != null && !tourLogs.isEmpty()) {
                document.add(new Paragraph("Tour Logs:").setBold().setFontSize(16).setMarginTop(20));

                Paragraph paragraph = new Paragraph("TourLOG");
                Cell cell = new Cell();

                Table table = new Table(new float[]{1, 2, 2, 3, 2, 2}); // Define column widths
                table.addCell("Log ID");
                table.addCell("Date");
                table.addCell("Difficulty");
                table.addCell("Comment");
                table.addCell("Duration");
                table.addCell("Distance");

                for (TourLog log : tourLogs) {
                    table.addCell(String.valueOf(log.getTourlogID()));
                    table.addCell(log.getDate().toString());
                    table.addCell(log.getDifficulty());
                    table.addCell(log.getComment());
                    table.addCell(String.valueOf(log.getDuration()));
                    table.addCell(String.valueOf(log.getDistance()));

                }
                document.add(table);
            } else {
                document.add(new Paragraph("No logs available for this tour."));
            }

            successLabel.setText("Single Report generated successfully!");
            document.close();
            logger.info("PDF generated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void handleGenerateSingleTourReport() {
        Tour selectedTour = tourListView.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            generateSingleTourReport(selectedTour);
        } else {
            System.out.println("No tour selected!");
        }
    }

}