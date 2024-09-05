package com.example.TourPlanner;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.model.TourLog;
import com.example.TourPlanner.service.TourLogService;
import com.example.TourPlanner.service.TourService;
import com.example.TourPlanner.view.MainController;
import com.example.TourPlanner.viewModel.TourLogViewModel;
import com.example.TourPlanner.viewModel.TourViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MainControllerTest {

    @InjectMocks
    private MainController mainController;

    @Mock
    private TourViewModel tourViewModel;

    @Mock
    private TourService tourService;

    @Mock
    private TourLogViewModel tourLogViewModel;

    @Mock
    private TourLogService tourLogService;

    @Mock
    private DatePicker datePicker;

    @Mock
    private ComboBox<String> difficultyComboBox, ratingComboBox;

    @Mock
    private Spinner<Integer> durationSpinner;

    @Mock
    private Spinner<Double> distanceSpinner;

    @Mock
    private ComboBox<Long> tourIdComboBox;

    @Mock
    private TextField commentField;

    @Mock
    private Label successLabel;

    @Mock
    private TableView<TourLog> tourLogTableView;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleSaveTourLog_ValidInput() {
        LocalDate logDate = LocalDate.now();
        String comment = "Great Tour!";
        String difficulty = "Medium";
        Integer duration = 120;
        Double distance = 15.0;
        String rating = "5 stars";
        Long tourID = 1L;

        when(datePicker.getValue()).thenReturn(logDate);
        when(commentField.getText()).thenReturn(comment);
        when(difficultyComboBox.getValue()).thenReturn(difficulty);
        when(durationSpinner.getValue()).thenReturn(duration);
        when(distanceSpinner.getValue()).thenReturn(distance);
        when(ratingComboBox.getValue()).thenReturn(rating);
        when(tourIdComboBox.getValue()).thenReturn(tourID);

        Tour selectedTour = new Tour(); // Create or mock a valid Tour
        when(tourService.getTourById(tourID)).thenReturn(selectedTour);

        mainController.handleSaveTourLog();

        verify(tourLogService).addTourLog(any(TourLog.class));
        verify(tourLogViewModel).refreshTourLogs();
        assertEquals("Tour log saved successfully!", successLabel.getText());
        assertEquals("-fx-text-fill: green;", successLabel.getStyle());
    }

    @Test
    public void testHandleSaveTourLog_InvalidInput() {
        when(datePicker.getValue()).thenReturn(null);
        when(commentField.getText()).thenReturn("");
        when(difficultyComboBox.getValue()).thenReturn(null);
        when(durationSpinner.getValue()).thenReturn(null);
        when(distanceSpinner.getValue()).thenReturn(null);
        when(ratingComboBox.getValue()).thenReturn(null);

        mainController.handleSaveTourLog();

        assertEquals("All fields must be filled out!", successLabel.getText());
        assertEquals("-fx-text-fill: red;", successLabel.getStyle());
    }

    @Test
    public void testHandleRemoveTourLog() {
        TourLog selectedTourLog = new TourLog(); // Create or mock a TourLog
        selectedTourLog.setTourlogID(1L); // Set ID for verification
        when(tourLogTableView.getSelectionModel().getSelectedItem()).thenReturn(selectedTourLog);

        mainController.handleRemoveTourLog();

        verify(tourLogService).deleteTourLogById(1L);
        verify(tourLogViewModel).removeTourLog(1L);
    }

    @Test
    public void testHandleEditTourLog() throws IOException {
        TourLog selectedTourLog = new TourLog(); // Create or mock a TourLog
        selectedTourLog.setTourlogID(1L); // Set ID for verification
        when(tourLogTableView.getSelectionModel().getSelectedItem()).thenReturn(selectedTourLog);
        when(tourLogViewModel.getTourLogByID(1L)).thenReturn(selectedTourLog);

        FXMLLoader fxmlLoader = mock(FXMLLoader.class);
        when(fxmlLoader.load()).thenReturn(new Scene(new TableView<>()));
        //whenNew(FXMLLoader.class).withAnyArguments().thenReturn(fxmlLoader);

        mainController.handleEditTourLog();

        verify(tourLogViewModel).updateTourLog(selectedTourLog);
    }

    @Test
    public void testHandleAddTourLog() throws IOException {
        FXMLLoader fxmlLoader = mock(FXMLLoader.class);
        when(fxmlLoader.load()).thenReturn(new Scene(new TableView<>()));
        //whenNew(FXMLLoader.class).withAnyArguments().thenReturn(fxmlLoader);

        mainController.handleAddTourLog();

        // Ensure that the stage was shown
        // You might need additional verification based on how the dialog is initialized
    }

    @Test
    public void testHandleTourLogCancel() {
        mainController.handleTourLogCancel();

        // Verify that fields are cleared
        verify(datePicker).setValue(null);
        verify(commentField).clear();
        verify(difficultyComboBox).getSelectionModel().clearSelection();
        verify(durationSpinner.getValueFactory()).setValue(0);
        verify(distanceSpinner.getValueFactory()).setValue(0.0);
        verify(ratingComboBox).getSelectionModel().clearSelection();
        verify(tourIdComboBox.getSelectionModel()).clearSelection();
        assertEquals("", successLabel.getText());

        // Verify that the stage is closed
        Stage stage = (Stage) datePicker.getScene().getWindow();
        assertNotNull(stage);
        verify(stage).close();
    }
}
