package com.example.cw_2601;

import Services.ArticleService;
import Services.UserService;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.librec.recommender.item.RecommendedItem;

import java.util.List;
import java.util.stream.Collectors;

public class RecommendationController {

    @FXML
    private Text articleTitle;
    @FXML
    private Text articleContent;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button likeButton;
    @FXML
    private Button skipButton;
    @FXML
    private Button markAsReadButton;

    private List<RecommendedItem> recommendedItems;
    private List<String> articleDetails;
    private int currentIndex = 0;

    private DatabaseConnection dbConnection = new DatabaseConnection();
    private LibrecRecommendation librecRecommendation = new LibrecRecommendation(dbConnection);

    public void setArticleDetails(String title, String content) {
        articleTitle.setText(title);
        articleContent.setText(content);
    }

    public void initialize() {
        // Call the non-static method on the instance of LibrecRecommendation
        recommendedItems = librecRecommendation.generateRecommendationsForCurrentUser();

        if (recommendedItems != null && !recommendedItems.isEmpty()) {
            List<Integer> articleIds = recommendedItems.stream()
                    .map(item -> Integer.parseInt(item.getItemId()))
                    .collect(Collectors.toList());

            articleDetails = librecRecommendation.getArticleDetails(articleIds);

            displayArticle(currentIndex);
        } else {
            articleTitle.setText("No recommendations available.");
            articleContent.setText("");
            nextButton.setDisable(true);
            previousButton.setDisable(true);
            likeButton.setDisable(true);
            skipButton.setDisable(true);
            markAsReadButton.setDisable(true);
        }
    }

    private void displayArticle(int index) {
        if (index >= 0 && index < articleDetails.size()) {
            String articleDetail = articleDetails.get(index);
            String[] parts = articleDetail.split("\nContent: ");
            articleTitle.setText(parts[0].replace("Title: ", ""));
            articleContent.setText(parts[1]);
        } else {
            articleTitle.setText("No more recommended articles.");
            articleContent.setText("");
            previousButton.setDisable(true);
            nextButton.setDisable(true);
            likeButton.setDisable(true);
            skipButton.setDisable(true);
            markAsReadButton.setDisable(true);
        }
    }

    @FXML
    private void showNextArticle() {
        if (currentIndex < articleDetails.size() - 1) {
            currentIndex++;
            displayArticle(currentIndex);
            previousButton.setDisable(false);
        } else {
            articleTitle.setText("No more recommended articles.");
            articleContent.setText("");
            nextButton.setDisable(true);
        }
    }

    @FXML
    private void showPreviousArticle() {
        if (currentIndex > 0) {
            currentIndex--;
            displayArticle(currentIndex);
            nextButton.setDisable(false);
        } else {
            articleTitle.setText("No previous articles.");
            articleContent.setText("");
            previousButton.setDisable(true);
        }
    }

    @FXML
    private void handleLike() {
        try {
            UserService userService = new UserService(dbConnection);
            int userId = userService.getCurrentUserId();

            int articleId = Integer.parseInt(recommendedItems.get(currentIndex).getItemId());
            ArticleService articleService = new ArticleService(dbConnection);

            boolean preferenceSuccess = userService.updateUserPreferences(userId, articleId, 5);
            if (preferenceSuccess) {
                System.out.println("Article liked and preference updated in the database: " + articleId);
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Action Successful");
                alert.setHeaderText(null);
                alert.setContentText("You liked this article!");
                alert.showAndWait();
            } else {
                System.out.println("Failed to update preferences in the database.");
            }

            boolean actionSuccess = userService.updateUserArticleAction(userId, "liked_articles", articleId);
            if (actionSuccess) {
                System.out.println("Article liked and updated in the database: " + articleId);
            } else {
                System.out.println("Failed to update liked articles in the database.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void skipArticle() {
        try {
            UserService userService = new UserService(dbConnection);
            int userId = userService.getCurrentUserId();

            int articleId = Integer.parseInt(recommendedItems.get(currentIndex).getItemId());

            ArticleService articleService = new ArticleService(dbConnection);

            // Check if the article has been liked by the user
            if (articleService.isArticleLiked(userId, articleId)) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                alert.setTitle("Action Denied");
                alert.setHeaderText(null); // No header text
                alert.setContentText("You cannot skip an article you have already liked.");
                alert.showAndWait();
                return;
            }

            boolean success = userService.updateUserPreferences(userId, articleId, -5);
            if (success) {
                System.out.println("Article skipped and preference updated in the database: " + articleId);
                showNextArticle();
            } else {
                System.out.println("Failed to update preferences in the database.");
            }

            boolean actionSuccess = userService.updateUserArticleAction(userId, "skipped_articles", articleId);
            if (actionSuccess) {
                System.out.println("Article skipped and updated in the database: " + articleId);
                showNextArticle();
            } else {
                System.out.println("Failed to update skipped articles in the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void markAsRead() {
        try {
            UserService userService = new UserService(dbConnection);
            int userId = userService.getCurrentUserId();

            int articleId = Integer.parseInt(recommendedItems.get(currentIndex).getItemId());

            ArticleService articleService = new ArticleService(dbConnection);

            boolean success = userService.updateUserArticleAction(userId, "read_articles", articleId);
            if (success) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Action Successful");
                alert.setHeaderText(null);
                alert.setContentText("Article marked as read!");
                alert.showAndWait();
                System.out.println("Article marked as read and updated in the database: " + articleId);
                showNextArticle();
            } else {
                System.out.println("Failed to update read articles in the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
