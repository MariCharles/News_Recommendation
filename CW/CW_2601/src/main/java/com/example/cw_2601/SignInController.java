package com.example.cw_2601;

import Models.User;
import Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignInController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signInButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Button signUpLinkButton;

    private DatabaseConnection dbConnector;

    public SignInController() {
        this.dbConnector = new DatabaseConnection();
    }

    @FXML
    private void handleSignInButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password.");
            return;
        }

        User user = new User(0, username, password, null);

        UserService userService = new UserService(dbConnector);
        if (user.signIn(dbConnector, userService)) {
            errorLabel.setText("Login successful!");
            loadHomePage(user);  // Pass the authenticated user
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    private void loadHomePage(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
            Stage stage = (Stage) signInButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());

            HomePageController homePageController = loader.getController();
            homePageController.setUsername(user.getUsername());

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error loading home page.");
        }
    }

    @FXML
    private void handleSignUpLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Stage stage = (Stage) signUpLinkButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error loading sign-up page.");
        }
    }
}
