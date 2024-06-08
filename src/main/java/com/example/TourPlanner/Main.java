package com.example.TourPlanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        showStage(primaryStage);
    }

    public void showStage(Stage primaryStage) throws Exception {
        URL fxmlUrl = Main.class.getResource("/TourPlanner/mainWindow.fxml");
        Objects.requireNonNull(fxmlUrl, "FXML file not found: /TourPlanner/mainWindow.fxml");
        Parent root = FXMLLoader.load(fxmlUrl);

        primaryStage.setTitle("TourPlanner");
        Scene scene = new Scene(root, 600, 400); // Adjust dimensions as needed
        primaryStage.setMinWidth(400);

        URL stylesheetUrl = Main.class.getResource("/TourPlanner/styles.css");
        if (stylesheetUrl != null) {
            scene.getStylesheets().add(stylesheetUrl.toExternalForm());
        } else {
            System.err.println("Stylesheet not found: /TourPlanner/styles.css");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
