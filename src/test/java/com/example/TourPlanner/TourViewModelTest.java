package com.example.TourPlanner;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.service.TourService;
import com.example.TourPlanner.viewModel.TourViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TourViewModelTest {

    @InjectMocks
    private TourViewModel tourViewModel;

    @Mock
    private TourService tourService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadTours() {
        // Arrange
        List<Tour> mockTours = Arrays.asList(new Tour(), new Tour());
        when(tourService.getAllTours()).thenReturn(mockTours);
        // Act
        tourViewModel.loadTours();
        // Assert
        assertEquals(2, tourViewModel.getTours().size());
    }


    @Test
    void testRemoveTour() {
        // Arrange
        Tour existingTour = new Tour();
        existingTour.setName("Test Tour");
        existingTour.setTourID(1L);
        tourViewModel.getTours().add(existingTour);

        when(tourService.getTourByName("Test Tour")).thenReturn(existingTour);
        doNothing().when(tourService).deleteTourById(1L);

        // Act
        tourViewModel.removeTour("Test Tour");

        // Assert
        assertFalse(tourViewModel.getTours().contains(existingTour), "The tour should be removed from the list");
        verify(tourService).getTourByName("Test Tour");
        verify(tourService).deleteTourById(1L);
    }


    @Test
    void testGetTourDetails() {
        // Arrange
        Tour mockTour = new Tour();
        String tourName = "Test Tour";
        when(tourService.getTourByName(any(String.class))).thenReturn(mockTour);

        // Act & Assert
        assertEquals(mockTour, tourViewModel.getTourDetails(tourName));
    }



    @Test
    void testUpdateTour() {
        // Arrange
        Tour existingTour = new Tour();
        existingTour.setTourID(1L);
        existingTour.setName("Original Name");
        tourViewModel.getTours().add(existingTour);

        // create an updated Tour
        Tour updatedTour = new Tour();
        updatedTour.setTourID(1L);  // same ID
        updatedTour.setName("Updated Name");
        updatedTour.setDescription("Updated Description");

        //mocking the update-methods
        doNothing().when(tourService).updateTour(any(Tour.class));
        when(tourService.getAllTours()).thenReturn(List.of(updatedTour));

        // Act
        tourViewModel.updateTour(updatedTour);

        // Assert
        assertEquals(1, tourViewModel.getTours().size(), "There should be exactly 1 tour in the list after update");

        // Verify that the tour in the list was updated
        Tour refreshedTour = tourViewModel.getTours().getFirst();
        assertEquals("Updated Name", refreshedTour.getName(), "The tour name should be updated");
        assertEquals("Updated Description", refreshedTour.getDescription(), "The tour description should be updated");

        // Verify interactions
        verify(tourService).updateTour(updatedTour);
        verify(tourService, times(2)).getAllTours();  // Verify that getAllTours was called twice: once in constructor and once in updateTour
    }



    @Test
    void testLoadToursExceptionHandling() {
        // Arrange
        when(tourService.getAllTours()).thenThrow(new RuntimeException("Error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tourViewModel.loadTours());
    }



// Tour does not exist


    @Test
    void testLoadToursWithEmptyList() {
    when(tourService.getAllTours()).thenReturn(List.of());

    tourViewModel.loadTours();

    assertTrue(tourViewModel.getTours().isEmpty(), "The tours list should be empty");
    }

    @Test
    void testRemoveTourWhenTourDoesNotExist() {
        // Arrange
        String nonExistentTourName = "Non Existent Tour";
        when(tourService.getTourByName(nonExistentTourName)).thenReturn(null);

        // Act
        tourViewModel.removeTour(nonExistentTourName);

        // Assert
        // Verify that no calls were made to delete the tour
        verify(tourService, never()).deleteTourById(anyLong());
    }


    @Test
    void testGetTourDetailsWhenTourDoesNotExist() {
        // Arrange
        String nonExistentTourName = "Non Existent Tour";
        when(tourService.getTourByName(nonExistentTourName)).thenReturn(null);

        // Act
        Tour result = tourViewModel.getTourDetails(nonExistentTourName);

        // Assert
        assertNull(result, "The result should be null when the tour does not exist");
    }


    @Test
    void testUpdateTourWhenTourDoesNotExist() {
        // Arrange
        Tour nonExistentTour = new Tour();
        nonExistentTour.setTourID(1L);  // Assuming this ID does not exist
        nonExistentTour.setName("Non Existent Tour");

        // Mock the service methods
        when(tourService.getAllTours()).thenReturn(List.of());  // Return an empty list
        doNothing().when(tourService).updateTour(any(Tour.class));

        // Act
        tourViewModel.updateTour(nonExistentTour);

        // Assert
        // Verify that the list of tours was reloaded and remains empty
        assertTrue(tourViewModel.getTours().isEmpty(), "The tours list should be empty when the tour does not exist");

        // Verify interactions
        verify(tourService).updateTour(nonExistentTour);
        verify(tourService, times(2)).getAllTours();  // Adjusted to allow for additional call
    }


}
