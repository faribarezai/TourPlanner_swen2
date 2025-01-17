/*
package com.example.TourPlanner.controller;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tours")
public class TourController {
    private static final Logger logger = LogManager.getLogger(TourController.class);

    @Autowired
    private TourService tourService;

    @GetMapping
    public List<Tour> getAllTours() {
        return tourService.getAllTours();
    }

    @GetMapping("/{id}")
    public Tour getTourById(@PathVariable Long id) {
        return tourService.getTourById(id);
    }

    @PostMapping
    public Tour createTour(@RequestBody Tour tour) {
        return tourService.addTour(tour);
    }

    @DeleteMapping("/{id}")
    public void deleteTourById(@PathVariable Long id) {
        tourService.deleteTourById(id);
    }

    @GetMapping("/name/{name}")
    public Tour getTourByName(@PathVariable String name) {
        return tourService.getTourByName(name);
    }

}
*/