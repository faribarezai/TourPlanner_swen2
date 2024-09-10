package com.example.TourPlanner.repository;


import com.example.TourPlanner.model.TourLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TourLogRepository extends JpaRepository<TourLog, Long> {
    @Query(value = "SELECT tl.* FROM tourlog tl INNER JOIN tour t ON tl.tour_id = t.tour_id WHERE tl.tourlog_id = :tourlogId", nativeQuery = true)
    TourLog findTourId(Long tourlogId);
}
