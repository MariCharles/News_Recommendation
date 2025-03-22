package com.example.cw_2601;

import Services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;

public class EditProfileController {

    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button saveButton;

    private DatabaseConnection databaseConnection;
    private UserService userService;

    private int userId;

    // Initializing services and database connection
    public EditProfileController() {
        databaseConnection = new DatabaseConnection();
        userService = new UserService(databaseConnection);
    }

    // Set the user ID from HomePageController
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @FXML
    private void saveChanges() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "New password and confirm password do not match.");
            return;
        }

        if (!userService.verifyPassword(userId, currentPassword)) {
            showAlert("Error", "Current password is incorrect.");
            return;
        }

        // Update the password in the database
        boolean success = userService.updatePassword(userId, newPassword);
        if (success) {
            showAlert("Success", "Password updated successfully.");
            saveButton.getScene().getWindow().hide();
        } else {
            showAlert("Error", "Failed to update password. Please try again.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
