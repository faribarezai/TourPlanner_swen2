package com.example.TourPlanner.model;
import jakarta.persistence.*;
import lombok.Getter;


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
    private String tranportType;
    @Column
    private Integer tourDistance;
    @Column
    private Integer estimatedTime;
    @Column
    private String routeInfos;// image with tour-map



    public Tour(String name, String description, String from, String to, String transportType) {
        this.name= name;
        this.description = description;
        this.tourFrom= from;
        this.tourTo= to;
        this.tranportType = transportType;
        this.tourDistance = getTourDistance();
        this.estimatedTime = getEstimatedTime();
        this.routeInfos= getRouteInfos();

    }
    public Tour(String name) {
        this.name= name;
    }

    public Tour() {

    }
}
