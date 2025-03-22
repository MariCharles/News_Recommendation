package com.example.cw_2601;

import Models.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class WelcomeController {

    @FXML
    private Button signInButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Button adminButton;

    private final Admin admin = new Admin();

    @FXML
    private void goToSignInPage() {
        loadPage("SignIn.fxml");
    }

    @FXML
    private void goToSignUpPage() {
        loadPage("SignUp.fxml");
    }

    @FXML
    private void validateAdmin() {
        String username = promptForInput("Admin Login", "Enter Admin Username", "Username:");
        if (username == null) return;

        String password = promptForInput("Admin Login", "Enter Admin Password", "Password:");
        if (password == null) return;

        if (admin.validateCredentials(username, password)) {
            loadPage("AdminPanel.fxml");
        } else {
            showAlert("Login Failed", "Invalid credentials. Please try again.", Alert.AlertType.ERROR);
        }
    }

    private String promptForInput(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) signInButton.getScene().getWindow();  // Get the current stage
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load page: " + fxmlFile, Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}
