package com.example.cw_2601;

import Models.User;
import Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignUpController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button signUpButton;
    @FXML
    private Button signInLinkButton;
    @FXML
    private Label errorLabel;

    @FXML
    private CheckBox technologyCheckBox;
    @FXML
    private CheckBox healthCheckBox;
    @FXML
    private CheckBox sportsCheckBox;
    @FXML
    private CheckBox entertainmentCheckBox;
    @FXML
    private CheckBox businessCheckBox;
    @FXML
    private CheckBox politicsCheckBox;
    @FXML
    private CheckBox weatherCheckBox;

    private UserService userService;

    public SignUpController() {
        this.userService = new UserService(new DatabaseConnection());
    }

    @FXML
    private void handleSignUpButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please enter both username and passwords.");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        List<String> preferences = new ArrayList<>();
        if (technologyCheckBox.isSelected()) preferences.add("Technology");
        if (healthCheckBox.isSelected()) preferences.add("Health");
        if (sportsCheckBox.isSelected()) preferences.add("Sports");
        if (entertainmentCheckBox.isSelected()) preferences.add("Entertainment");
        if (businessCheckBox.isSelected()) preferences.add("Business");
        if (politicsCheckBox.isSelected()) preferences.add("Politics");
        if (weatherCheckBox.isSelected()) preferences.add("Weather");

        // Join preferences as a comma-separated string
        String preferencesString = String.join(",", preferences);

        User user = new User(0, username, password, preferencesString);

        if (user.register(userService, preferencesString)) {
            showSuccessPopup();
            loadPage("SignIn.fxml");
        } else {
            errorLabel.setText("Registration failed. Username might already be taken.");
        }
    }

    @FXML
    private void handleSignInLink() {
        loadPage("SignIn.fxml");
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSuccessPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Successful");
        alert.setHeaderText(null);
        alert.setContentText("User registered successfully! Sign in to continue...");
        alert.showAndWait();
    }
}
