package com.example.TourPlanner.service;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.model.TourLog;
import com.example.TourPlanner.repository.TourLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class TourLogService {
    private static final Logger logger = Logger.getLogger(TourLogService.class.getName());

    @Autowired
    private TourLogRepository tourLogRepository;

    public TourLogService() {}

    public TourLogService(TourLogRepository tourLogRepository) {
        this.tourLogRepository = tourLogRepository;
    }


    public List<TourLog> getAllTourLogs() {
        List<TourLog> tourLogs = tourLogRepository.findAll();
        return tourLogs.isEmpty() ? Collections.emptyList() : tourLogs;
    }

    public TourLog getTourLogById(Long id) {
        Optional<TourLog> optionalTourLog = tourLogRepository.findById(id);
        return optionalTourLog.orElse(null);
    }

    public void addTourLog(TourLog tourLog) {
        tourLogRepository.save(tourLog);
    }

    public void deleteTourLogById(Long id) {
        tourLogRepository.deleteById(id);
    }

    public void updateTourLog(TourLog updatedTourLog) {
        Optional<TourLog> optionalExistingTourLog = tourLogRepository.findById(updatedTourLog.getTourlogID());
        if (optionalExistingTourLog.isPresent()) {
            TourLog existingTourLog = optionalExistingTourLog.get();
            logger.log(Level.WARNING, "TourLog with ID exists ->> " + existingTourLog.getTourlogID());

            existingTourLog.setDate(updatedTourLog.getDate());
            existingTourLog.setDistance(updatedTourLog.getDistance());
            existingTourLog.setComment(updatedTourLog.getComment());
            existingTourLog.setDifficulty(updatedTourLog.getDifficulty());
            existingTourLog.setDuration(updatedTourLog.getDuration());
            existingTourLog.setRating(updatedTourLog.getRating());

            Tour tour= updatedTourLog.getTour();
            existingTourLog.setTourId(tour.getTourID());
            // Set the tour object (not the ID, as it could lead to issues)
           // existingTourLog.setTour(updatedTourLog.getTour());

            // Set other properties as needed
            tourLogRepository.save(existingTourLog);
        } else {
            logger.log(Level.WARNING, "TourLog with ID " + updatedTourLog.getTourlogID() + " not found.");
        }
    }


    public List<TourLog> getTourLogsByTourId(Long tourId) {
        System.out.println("All Tourolgs of tour: "+tourId +" :->> " + getAllTourLogs().toString());
        return tourLogRepository.findByTourId(tourId);


    }

}
