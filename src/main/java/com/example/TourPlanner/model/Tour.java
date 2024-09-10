package com.example.TourPlanner.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


// JavaFx main lauft nicht,
// die repository classes funktionieren irgendiwe auch nicht

// SpringBoot Main lauft!
//Db anbindung funktionert!
//Tables werden created!

// every tour consists of name, tour description, from, to, transport type, tour distance,
//estimated time, route information (an image with the tour map)
//o the image, the distance, and the time should be retrieved by a REST request using the
//OpenRouteservice.org APIs and OpenStreetMap Tile Server
//(https://openrouteservice.org/dev , https://tile.openstreetmap.org/ )

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

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
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
