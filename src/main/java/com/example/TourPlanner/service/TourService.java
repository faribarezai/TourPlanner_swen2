package com.example.TourPlanner.service;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourService {

   @Autowired
   private TourRepository tourRepository;

   public List<Tour> getAllTours() {
      return tourRepository.findAll();
   }

   public Tour getTourById(Long id) {
      return tourRepository.findById(id).orElse(null);
   }

   public Tour getTourByName(String name) {
      return tourRepository.findByName(name);
   }

   public Tour saveTour(Tour tour) {
      return tourRepository.save(tour);
   }

   public void deleteTourById(Long id) {
      tourRepository.deleteById(id);
   }

   //public Tour editTour(Tour tour){
      // return new edited Tour

    //  return tourRepository.editTour(tour);}

}
