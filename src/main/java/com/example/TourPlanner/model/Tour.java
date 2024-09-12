package com.example.TourPlanner.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Data
@Entity
@Table(name="tour")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tourID;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String tourFrom;
    @Column
    private String tourTo;
    @Column
    private String transportType;
    @Column
    private Double tourDistance;
    @Column
    private Time estimatedTime;
    @Column
    private String routeInfos;// image with tour-map

    // Computed attributes
    private Integer popularity;
    private String childFriendliness;


    @OneToMany(mappedBy = "tour", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourLog> tourLogs = new ArrayList<>();;

    public Tour(String name, String description, String from, String to, String transportType) {
        this.name= name;
        this.description = description;
        this.tourFrom= from;
        this.tourTo= to;
        this.transportType = transportType;
        this.routeInfos= getRouteInfos();
        this.estimatedTime =getEstimatedTime();
        this.tourDistance= getTourDistance();

    }
    public Tour(String name) {
        this.name= name;
    }

    public Tour() {}


    public void addTourLog(TourLog log) {
        tourLogs.add(log);
        log.setTour(this);
    }

    public void removeTourLog(TourLog log) {
        tourLogs.remove(log);
        log.setTour(null);
    }

    @Override
    public String toString() {
        return name;
        }

}
