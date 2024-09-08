package com.example.TourPlanner.viewModel;

import com.example.TourPlanner.model.TourLog;
import com.example.TourPlanner.service.TourLogService;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class TourLogViewModel {

    private static final Logger logger = LogManager.getLogger(TourLogViewModel.class.getName());

    private final TourLogService tourLogService;
    @Getter
    private ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();
    @Getter
    private final LongProperty selectedTourLogId = new SimpleLongProperty(1L);

    @Autowired
    public TourLogViewModel(TourLogService tourLogService) {
        this.tourLogService = tourLogService;
        loadTourLogs();
    }

    private void loadTourLogs() {
        try {
            List<TourLog> logs = tourLogService.getAllTourLogs();
            tourLogs.addAll(logs);
        } catch (Exception e) {
            logger.error("Error loading TourLogs", e);
            // Handle error
        }
    }

    public void refreshTourLogs() {
        if(tourLogs !=null) {
            tourLogs.clear();
            tourLogs.addAll(tourLogService.getAllTourLogs());
        }
    }

    public void removeTourLog(Long id) {
        try {
            TourLog tourLogToRemove = null;
            for (TourLog tourLog : tourLogs) {
                if (tourLog.getTourlogID().equals(id)) {
                    tourLogToRemove = tourLog;
                    break;
                }
            }
            if (tourLogToRemove != null) {
                tourLogService.deleteTourLogById(id);
                tourLogs.remove(tourLogToRemove);
                logger.info("TourLog deleted successfully!");
            }
        } catch (Exception e) {
            logger.error("Exception occurred while removing TourLog", e);
            // Handle error
        }
    }

    public LongProperty selectedTourLogIdProperty() {
        return selectedTourLogId;
    }



    public void updateTourLog(TourLog updatedTourLog) {
        tourLogService.updateTourLog(updatedTourLog);
    }

    public TourLog getTourLogByID(Long T_id) {
        return tourLogService.getTourLogById(T_id);
    }
}
