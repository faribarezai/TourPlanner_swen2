package com.example.TourPlanner.controller;

import com.example.TourPlanner.model.TourLog;
import com.example.TourPlanner.service.TourLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tourLogs")
public class TourLogController {

    @Autowired
    private TourLogService tourLogService;

    @GetMapping
    public List<TourLog> getAllTourLogs() {
        return tourLogService.getAllTourLogs();
    }

    @GetMapping("/{id}")
    public TourLog getTourLogById(@PathVariable Long id) {
        return tourLogService.getTourLogById(id);
    }


    @PostMapping
    public TourLog createTourLog(@RequestBody TourLog tourLog) {
        return tourLogService.saveTourLog(tourLog);
    }

    @DeleteMapping("/{id}")
    public void deleteTourLogById(@PathVariable Long id) {
        tourLogService.deleteTourLogById(id);
    }
}
