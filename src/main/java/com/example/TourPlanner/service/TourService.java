package com.example.TourPlanner.service;

import com.example.TourPlanner.model.Tour;
import com.example.TourPlanner.repository.TourRepository;
import com.example.TourPlanner.viewModel.TourViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hibernate.internal.CoreLogging.logger;

@Service
public class TourService {
   private static final Logger logger = Logger.getLogger(TourViewModel.class.getName());
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

    // Update method
    public void updateTour(Tour updatedTour) {
        Tour existingTour = getTourById(updatedTour.getTourID());
        logger.log(Level.INFO, "Id of Tour before update:" + updatedTour.getTourID());
        if (existingTour != null) {
            existingTour.setName(updatedTour.getName());
            existingTour.setDescription(updatedTour.getDescription());
            existingTour.setTourFrom(updatedTour.getTourFrom());
            existingTour.setTourTo(updatedTour.getTourTo());
            existingTour.setTransportType(updatedTour.getTransportType());
            tourRepository.save(existingTour);
        } else {
            logger.log(Level.WARNING, "Tour with ID " + updatedTour.getTourID() + " not found.");
        }
    }


    public List<Long> getAllTourIds() {
       List<Tour> allTours = getAllTours();
        List<Long> allIDs = new ArrayList<>();

        for(Tour t :allTours) {
            allIDs.add(t.getTourID());
       }
      return allIDs;
    }
}
