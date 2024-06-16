package com.example.TourPlanner.repository;

import com.example.TourPlanner.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    @Query("SELECT t FROM Tour t WHERE t.name = :name")
    Tour findByName(String name);
}
