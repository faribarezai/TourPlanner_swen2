package com.example.TourPlanner;

import com.example.TourPlanner.service.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.repository.TourRepository;

class TourServiceTest {

    private TourService tourService;
    private TourRepository tourRepository;

    @BeforeEach
    void setUp() {
        tourRepository = mock(TourRepository.class);
        tourService = new TourService(tourRepository);
    }


    @Test
    void testGetAllTours() {
        // Arrange
        List<Tour> tours = Arrays.asList(new Tour(), new Tour());
        when(tourRepository.findAll()).thenReturn(tours);

        // Act
        List<Tour> result = tourService.getAllTours();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tourRepository).findAll();
    }

    @Test
    void testGetTourById() {
        // Arrange
        Long id = 1L;
        Tour tour = new Tour();
        tour.setTourID(id);
        when(tourRepository.findById(id)).thenReturn(java.util.Optional.of(tour));

        // Act
        Tour result = tourService.getTourById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getTourID());
        verify(tourRepository).findById(id);
    }

    @Test
    void testGetTourByName() {
        // Arrange
        String name = "Example Tour";
        Tour tour = new Tour();
        tour.setName(name);
        when(tourRepository.findByName(name)).thenReturn(tour);

        // Act
        Tour result = tourService.getTourByName(name);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(tourRepository).findByName(name);
    }

    @Test
    void testAddTour() {
        // Arrange
        Tour tour = new Tour();
        tour.setTourID(1L);
        tour.setName("New Tour");

        // Act
        tourService.addTour(tour);

        // Assert
        verify(tourRepository).save(tour);
    }

   /* @Test
    void testDeleteTourById() {
        // Arrange
        Long id = 1L;

        // Act
        tourService.deleteTourById(id);

        // Assert
        verify(tourRepository).deleteById(id);
    }

    @Test
    void testUpdateTour() {
        // Arrange
        Long id = 1L;
        Tour updatedTour = new Tour();
        updatedTour.setTourID(id);
        updatedTour.setName("Updated Name");

        Tour existingTour = new Tour();
        existingTour.setTourID(id);
        when(tourRepository.findById(id)).thenReturn(java.util.Optional.of(existingTour));

        // Act
        tourService.updateTour(updatedTour);

        // Assert
        verify(tourRepository).findById(id);
        verify(existingTour).setName("Updated Name");
        verify(tourRepository).save(existingTour);
    }


    */

}