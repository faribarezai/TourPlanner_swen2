package com.example.TourPlanner.service;
import com.example.TourPlanner.model.TourLog;
import com.example.TourPlanner.repository.TourLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourLogService {

    @Autowired
    private TourLogRepository tourLogRepository;

    public List<TourLog> getAllTourLogs() {
        return tourLogRepository.findAll();
    }

    public TourLog getTourLogById(Long id) {
        return tourLogRepository.findById(id).orElse(null);
    }

    public Optional<TourLog> getTourLogsByTourLogId(Long tourlogId) {
        return tourLogRepository.findById(tourlogId);
    }

    public TourLog saveTourLog(TourLog tourLog) {
        return tourLogRepository.save(tourLog);
    }

    public void deleteTourLogById(Long id) {
        tourLogRepository.deleteById(id);
    }
}
