package com.example.TourPlanner.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
@Entity
@Table(name = "tourlog")
public class TourLog {
    //  a tour-log consists of date/time, comment, difficulty, total distance, total time, and
//rating taken on the tour
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tourlogID;
    @Column
    private LocalDate date;
    @Column
    private String comment;
    @Column
    private String difficulty; //easy to extreme
    @Column
    private Integer duration; // in minutes
    @Column
    private Double distance;
    @Column
    private String rating; // stars...

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    public Long getTourId() {
        return tour != null ? tour.getTourID() : null;
    }
    public void setTourId(long id) {
        tour = new Tour();
        tour.setTourID(id);
    }


    public TourLog(LocalDate date, String comment, String difficulty, Integer duration, Double distance, String rating) {
        this.date = date;
        this.comment = comment;
        this.difficulty = difficulty;
        this.duration = duration;
        this.distance = distance;
        this.rating = rating;
        this.tour= getTour();

    }

    public Tour getTour() {
       Tour tour = new Tour();
        return tour;
    }

    public TourLog() {
    }


}
