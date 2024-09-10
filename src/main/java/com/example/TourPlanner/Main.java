
package com.example.TourPlanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@SpringBootApplication
@Configuration(proxyBeanMethods = false)
public class Main extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        springContext = new SpringApplicationBuilder(Main.class).headless(false).run();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TourPlanner/mainWindow.fxml"));

        // Use Spring to manage controllers
        fxmlLoader.setControllerFactory(springContext::getBean);
        Scene scene = new Scene(fxmlLoader.load());
        // Add CSS file to scene
        scene.getStylesheets().add((Objects.requireNonNull(getClass().getResource("/TourPlanner/styles.css"))).toExternalForm());


        primaryStage.setScene(scene);
        primaryStage.setTitle("Tour Planner");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}


