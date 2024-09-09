
package com.example.TourPlanner;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.web.WebView;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@SpringBootApplication
@Configuration(proxyBeanMethods = false)
public class Main extends Application {

    private ConfigurableApplicationContext springContext;
    private static HostServices hostServices;


    @Override
    public void init() {
        springContext = new SpringApplicationBuilder(Main.class).headless(false).run();
        hostServices = getHostServices();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TourPlanner/mainWindow.fxml"));

        /*
        WebView webView = new WebView();
        webView.getEngine().load("/leaflet/leaflet.html");
        System.out.println("I am in Main start method() after loading Map");
        */

       // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resource/TourPlanner/mainWindow.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean); // Use Spring to manage controllers
        Scene scene = new Scene(fxmlLoader.load());
        // Add CSS file to scene
        scene.getStylesheets().add((Objects.requireNonNull(getClass().getResource("/TourPlanner/styles.css"))).toExternalForm());


        primaryStage.setScene(scene);
        primaryStage.setTitle("Tour Planner");
        primaryStage.show();
    }





    public static void showMapInDefaultBrowser(){
        hostServices.showDocument(Main.class.getResource("/main/resources/leaflet/leaflet.html").toExternalForm());
    }

    public static void main(String[] args) {
        launch(args);
    }
}


