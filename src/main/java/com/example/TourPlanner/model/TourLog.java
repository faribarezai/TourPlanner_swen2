package com.example.TourPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
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
    private Integer difficulty; //1...easy to 5...very difficult
    @Column
    private Integer duration; // in minutes
    @Column
    private Integer distance;
    @Column
    private Integer rating; // 1.. to 5...


    public TourLog(LocalDate date, String comment, Integer difficulty, Integer duration, Integer distance, Integer rating) {
        this.date = date;
        this.comment = comment;
        this.difficulty = difficulty;
        this.duration = duration;
        this.distance = distance;
        this.rating = rating;

    }

    public TourLog() {
    }
}
