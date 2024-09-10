module com.example.TourPlanner {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.web;
    requires spring.web;
    requires spring.beans;
    requires spring.context;
    requires spring.data.jpa;
    requires lombok;
    requires jakarta.persistence;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires java.sql;
    requires org.apache.logging.log4j;
    requires java.base; // already implicitly required
    requires org.hibernate.orm.core; // add this if you are using Hibernate

    opens com.example.TourPlanner to javafx.fxml;
    opens com.example.TourPlanner.model;
    opens com.example.TourPlanner.repository;
    opens com.example.TourPlanner.service;
    opens com.example.TourPlanner.viewModel;

    exports com.example.TourPlanner;
    exports com.example.TourPlanner.model;
    exports com.example.TourPlanner.repository;
    exports com.example.TourPlanner.service;
    exports com.example.TourPlanner.viewModel;
    exports com.example.TourPlanner.view;
    opens com.example.TourPlanner.view;
}