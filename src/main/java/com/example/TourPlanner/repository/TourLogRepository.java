package com.example.TourPlanner.repository;


import com.example.TourPlanner.model.TourLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourLogRepository extends JpaRepository<TourLog, Long> {

       // TourLog editTour(TourLog tourlog);
       // TourLog findByName(String name);

}
