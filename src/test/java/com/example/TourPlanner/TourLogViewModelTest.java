package com.example.TourPlanner;

import com.example.TourPlanner.model.TourLog;
import com.example.TourPlanner.service.TourLogService;
import com.example.TourPlanner.viewModel.TourLogViewModel;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class TourLogViewModelTest {

    @Mock
    private TourLogService mockTourLogService;

    @InjectMocks
    private TourLogViewModel viewModel;

    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations (mocks and inject mocks)
        MockitoAnnotations.openMocks(this);
        viewModel = new TourLogViewModel(mockTourLogService);
    }

    @Test
    void testLoadTourLogs() {
        // Arrange
        TourLog tourLog = new TourLog();
        List<TourLog> mockLogs = Collections.singletonList(tourLog);

        // Ensure the mock is set up before initializing the view model
        when(mockTourLogService.getAllTourLogs()).thenReturn(mockLogs);

        // Act: Initialize the view model, which will invoke loadTourLogs() in the constructor
        TourLogViewModel viewModel = new TourLogViewModel(mockTourLogService);

        // Assert
        ObservableList<TourLog> tourLogs = viewModel.getTourLogs();
        assertEquals(1, tourLogs.size());

        // Verify the getAllTourLogs method was called three times
        verify(mockTourLogService, times(3)).getAllTourLogs();

        // Additional check to ensure the contents match
        assertEquals(mockLogs, tourLogs);
    }


    @Test
    void testRefreshTourLogs() {
        // Arrange
        TourLog tourLog = new TourLog();
        List<TourLog> mockLogs = Collections.singletonList(tourLog);

        // Mock the TourLogService to return one log
        when(mockTourLogService.getAllTourLogs()).thenReturn(mockLogs);

        // Act
        viewModel.refreshTourLogs(); // This method triggers the second call to getAllTourLogs()

        // Assert
        ObservableList<TourLog> tourLogs = viewModel.getTourLogs();
        assertEquals(1, tourLogs.size());
        // Ensure it was called 3 times: once in constructor, once during setup, and once in refresh
        verify(mockTourLogService, times(3)).getAllTourLogs();
    }


    @Test
    void testRemoveTourLog() {
        // Arrange
        TourLog tourLog = new TourLog();
        tourLog.setTourlogID(1L);
        viewModel.getTourLogs().add(tourLog);

        // Act
        viewModel.removeTourLog(1L);

        // Assert
        assertFalse(viewModel.getTourLogs().contains(tourLog));  // Ensure tour log was removed
        verify(mockTourLogService).deleteTourLogById(1L);  // Ensure the service was called to delete it
    }

    @Test
    void testUpdateTourLog() {
        // Arrange
        TourLog updatedTourLog = new TourLog();
        updatedTourLog.setTourlogID(1L);

        // Act
        viewModel.updateTourLog(updatedTourLog);

        // Assert
        verify(mockTourLogService).updateTourLog(updatedTourLog);  // Ensure the service's update method is called
    }

    @Test
    void testGetTourLogByID() {
        // Arrange
        TourLog mockTourLog = new TourLog();
        mockTourLog.setTourlogID(1L);

        // Mock the service call to return the tour log by ID
        when(mockTourLogService.getTourLogById(1L)).thenReturn(mockTourLog);

        // Act
        TourLog tourLog = viewModel.getTourLogByID(1L);

        // Assert
        assertEquals(1L, tourLog.getTourlogID());
        verify(mockTourLogService).getTourLogById(1L);  // Ensure the service was called to get the log
    }


    @Test
    void testSelectedTourLogIdProperty() {
        assertEquals(1L, viewModel.getSelectedTourLogId().get());
    }

    @Test
    void testLoadTourLogsWithEmptyList() {
        when(mockTourLogService.getAllTourLogs()).thenReturn(Collections.emptyList());
        TourLogViewModel viewModel = new TourLogViewModel(mockTourLogService);
        assertEquals(0, viewModel.getTourLogs().size());
    }



    @Test
    void testLoadTourLogsWithMultipleLogs() {
        List<TourLog> mockLogs = Arrays.asList(
                new TourLog(), new TourLog(), new TourLog()
        );
        when(mockTourLogService.getAllTourLogs()).thenReturn(mockLogs);
        TourLogViewModel viewModel = new TourLogViewModel(mockTourLogService);
        assertEquals(3, viewModel.getTourLogs().size());
    }



}
