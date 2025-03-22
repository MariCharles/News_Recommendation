package com.example.cw_2601;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the Welcome page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Welcome.fxml"));
            Scene scene = new Scene(loader.load());

            primaryStage.setTitle("Welcome Page");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}