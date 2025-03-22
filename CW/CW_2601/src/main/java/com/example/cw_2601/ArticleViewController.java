package com.example.cw_2601;

import Models.Article;
import Services.ArticleService;
import Services.UserService;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.List;

public class ArticleViewController {
    @FXML
    private Text articleTitle;

    @FXML
    private Text articleContent;

    private List<Article> articles;
    private int currentIndex = 0;
    private ArticleService articleService;
    private UserService userService;

    public void initialize() {
        // Initialize ArticleService and UserService
        articleService = new ArticleService(new DatabaseConnection());
        userService = new UserService(new DatabaseConnection());

        // Load articles from the database
        articles = articleService.fetchAllArticles();

        // Display the first article
        if (!articles.isEmpty()) {
            displayArticle(0);
        } else {
            articleTitle.setText("No Articles Available");
            articleContent.setText("");
        }
    }

    private void displayArticle(int index) {
        Article article = articles.get(index);
        articleTitle.setText(article.getTitle());
        articleContent.setText(article.getContent());
    }

    @FXML
    public void showNextArticle() {
        if (currentIndex < articles.size() - 1) {
            currentIndex++;
            displayArticle(currentIndex);
        } else {
            articleTitle.setText("No more articles");
            articleContent.setText("");
        }
    }

    @FXML
    public void showPreviousArticle() {
        if (currentIndex > 0) {
            currentIndex--;
            displayArticle(currentIndex);
        } else {
            articleTitle.setText("No previous articles");
            articleContent.setText("");
        }
    }

    @FXML
    public void handleLike() {
        try {
            int userId = userService.getCurrentUserId();
            int articleId = articles.get(currentIndex).getArticleId();

            // Update user preferences
            boolean preferenceSuccess = userService.updateUserPreferences(userId, articleId, 5);
            if (preferenceSuccess) {
                System.out.println("Article liked and preference updated in the database: " + articleId);
            } else {
                System.out.println("Failed to update preferences in the database.");
            }

            // Update liked articles
            boolean actionSuccess = userService.updateUserArticleAction(userId, "liked_articles", articleId);
            if (actionSuccess) {
                System.out.println("Article liked and updated in the database: " + articleId);

                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Action Successful");
                alert.setHeaderText(null);
                alert.setContentText("You liked this article!");
                alert.showAndWait();
            } else {
                System.out.println("Failed to update liked articles in the database.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void skipArticle() {
        try {
            int userId = userService.getCurrentUserId();
            int articleId = articles.get(currentIndex).getArticleId();

            // Check if the article has been liked
            if (articleService.isArticleLiked(userId, articleId)) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                alert.setTitle("Action Denied");
                alert.setHeaderText(null);
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
    public void markAsRead() {
        try {
            int userId = userService.getCurrentUserId();
            int articleId = articles.get(currentIndex).getArticleId();

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