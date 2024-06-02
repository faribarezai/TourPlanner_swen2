package com.example.TourPlanner.repository;

import com.example.TourPlanner.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TourRepository extends JpaRepository<Tour, Long> {

   // Tour editTour(Tour tour);
    Tour findByName(String name);

}
