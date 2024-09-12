package com.example.TourPlanner.repository;


import com.example.TourPlanner.model.TourLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourLogRepository extends JpaRepository<TourLog, Long> {
  //  List<TourLog> findByTourId(Long tourId);
    @Query("SELECT t FROM TourLog t WHERE t.tour.tourID = :tourId")
    List<TourLog> findByTourId(@Param("tourId") Long tourId);
}
