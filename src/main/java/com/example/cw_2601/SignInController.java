package com.example.cw_2601;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignInController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signInButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Label errorLabel;

    // Method called when the Sign-In button is clicked
    @FXML
    private void handleSignInButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password.");
            return;
        }

        // Call the authentication logic (this should be connected to a DB or authentication service)
        if (authenticateUser(username, password)) {
            errorLabel.setText("Login successful!");
            // Code to load the main application page
            loadMainPage();
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    // Dummy authentication method (replace with actual authentication logic)
    private boolean authenticateUser(String username, String password) {
        // Placeholder for real authentication
        return "user".equals(username) && "pass".equals(password);
    }

    private void loadMainPage() {
        // Load main page or application screen logic here
        Stage stage = (Stage) signInButton.getScene().getWindow();
        stage.close();
        // Open main application window
    }

    // Method called when the Sign-Up button is clicked
    @FXML
    private void handleSignUpButton() {
        // Redirect to the sign-up page or open a sign-up form
        errorLabel.setText("Sign-Up button clicked.");
    }
}
