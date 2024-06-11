
package com.example.TourPlanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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

       // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resource/TourPlanner/mainWindow.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean); // Use Spring to manage controllers
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tour Planner");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


