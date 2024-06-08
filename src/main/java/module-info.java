module com.example.TourPlanner {
    requires javafx.fxml;
    requires javafx.controls;
    requires spring.web;
    requires spring.beans;
    requires spring.context;
    requires spring.data.jpa;
    requires lombok;
    requires jakarta.persistence;
    requires spring.boot.autoconfigure;
    requires spring.boot;

    opens com.example.TourPlanner to javafx.fxml;
    exports com.example.TourPlanner;
}