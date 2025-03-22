package com.example.cw_2601;

import Models.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminPanelController {

    private Admin admin = new Admin();

    @FXML
    private void fetchArticles() {
        admin.fetchArticles();
    }

    @FXML
    private void addArticles() {
        admin.addArticles();
    }

    @FXML
    private void handleLogout() {
        try {
            // Load the Sign-In page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage currentStage = (Stage) ((Stage) Stage.getWindows().get(0)).getScene().getWindow();

            // Switch to the Sign-In page
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
