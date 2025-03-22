package com.example.cw_2601;

import Services.ArticleService;
import Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.librec.recommender.item.RecommendedItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomePageController {

    @FXML
    private Label welcomeLabel;

    private DatabaseConnection databaseConnection;
    private UserService userService;
    private ArticleService articleService;
    private LibrecRecommendation librecRecommendation;

    public HomePageController() {
        databaseConnection = new DatabaseConnection();
        userService = new UserService(databaseConnection);
        articleService = new ArticleService(databaseConnection);
        librecRecommendation = new LibrecRecommendation(databaseConnection);
    }

    public void setUsername(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    @FXML
    public void viewArticles() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Articles");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getRecommendations() {
        try {
            int userId = userService.getCurrentUserId();

            // Fetch recommendations for the logged-in user
            List<RecommendedItem> recommendations = librecRecommendation.generateRecommendationsForCurrentUser();

            // Extract article IDs from the recommendations
            List<Integer> articleIds = new ArrayList<>();
            for (RecommendedItem recommendation : recommendations) {
                articleIds.add(Integer.parseInt(recommendation.getItemId()));  // Convert itemId to Integer
            }

            // Fetch article details (title and content) using getArticleDetails()
            List<String> articleDetails = librecRecommendation.getArticleDetails(articleIds);

            // Check if there are any articles to display
            if (!articleDetails.isEmpty()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("RecommendationView.fxml"));
                Parent root = loader.load();

                // Get the controller of RecommendationView.fxml
                RecommendationController recommendationController = loader.getController();

                // Extract the first article's title and content
                String articleDetail = articleDetails.get(0);
                String[] parts = articleDetail.split("\nContent: ");
                String articleTitle = parts[0].replace("Title: ", "");
                String articleContent = parts[1];

                // Pass article details to the controller
                recommendationController.setArticleDetails(articleTitle, articleContent);

                Stage stage = new Stage();
                stage.setTitle("Article Details");
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                System.out.println("No recommendations found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void likedArticles() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cw_2601/LikedArticles.fxml"));
            Parent root = loader.load();

            LikedArticlesController likedArticlesController = loader.getController();

            // Fetch liked article titles from the ArticleService for the current user
            int userId = userService.getCurrentUserId();
            List<String> likedTitles = articleService.getLikedArticleTitles(userId);

            // Pass the liked articles to the LikedArticlesController
            if (!likedTitles.isEmpty()) {
                likedArticlesController.setLikedArticles(likedTitles);
            } else {
                likedArticlesController.setLikedArticles(List.of("No liked articles found."));
            }

            Stage stage = new Stage();
            stage.setTitle("Liked Articles");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void skippedArticles() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cw_2601/SkippedArticles.fxml"));
            Parent root = loader.load();

            // Get the controller of the SkippedArticles.fxml
            SkippedArticlesController skippedArticlesController = loader.getController();

            // Fetch skipped article titles from the ArticleService for the current user
            int userId = userService.getCurrentUserId();
            List<String> skippedTitles = articleService.getSkippedArticleTitles(userId);

            // Pass the skipped articles to the SkippedArticlesController
            if (!skippedTitles.isEmpty()) {
                skippedArticlesController.setSkippedArticles(skippedTitles);
            } else {
                skippedArticlesController.setSkippedArticles(List.of("No skipped articles found."));
            }

            Stage stage = new Stage();
            stage.setTitle("Skipped Articles");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cw_2601/EditProfile.fxml"));
            Parent root = loader.load();

            // Get the controller of the EditProfile.fxml
            EditProfileController editProfileController = loader.getController();

            // Pass the current user ID to the controller
            int userId = userService.getCurrentUserId();
            editProfileController.setUserId(userId);

            Stage stage = new Stage();
            stage.setTitle("Edit Profile");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            // Load the Sign-In page again
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            Scene scene = new Scene(loader.load());

            // Switch to the Sign-In page
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

