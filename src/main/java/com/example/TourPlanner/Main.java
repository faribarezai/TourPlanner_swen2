package com.example.TourPlanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        showStage(primaryStage);
    }
        /*
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("TourPlanner");

        //scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/resources/com.example.tourplanner/styles.css")).toExternalForm());

        URL stylesheetUrl = getClass().getResource("src/main/resources/styles.css");
        if (stylesheetUrl != null) {
            scene.getStylesheets().add(stylesheetUrl.toExternalForm());
        } else {
            System.err.println("Stylesheet not found: src/main/resources/styles.css");
        }

        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
    */


    public static Parent showStage(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Main.class.getResource("mainWindow.fxml"));
        primaryStage.setTitle("Tour Planner");
        Scene scene = new Scene(root, 320, 240);
        primaryStage.setMinWidth(400);

        URL stylesheetUrl = Main.class.getResource("src/main/resources/styles.css");
        if (stylesheetUrl != null) {
            scene.getStylesheets().add(stylesheetUrl.toExternalForm());
        } else {
            System.err.println("Stylesheet not found: src/main/resources/styles.css");
        }


        primaryStage.setScene(scene);
        primaryStage.show();
        return root;
    }
}