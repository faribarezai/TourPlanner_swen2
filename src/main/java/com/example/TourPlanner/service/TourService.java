package com.example.TourPlanner.service;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TourService {

   @Autowired
   private TourRepository tourRepository;

   public List<Tour> getAllTours() {
      List<Tour> list= tourRepository.findAll();

      if(list.isEmpty())
      return Collections.emptyList(); // Return an empty list

      return list;

   }

   public Tour getTourById(Long id) {
      return tourRepository.findById(id).orElse(null);
   }

   public Tour getTourByName(String name) {
      return tourRepository.findByName(name);
   }

   public void addTour(Tour tour) {
      tourRepository.save(tour);
   }

   public void deleteTourById(Long id) {
      tourRepository.deleteById(id);
   }


}
