package Models;

import com.example.cw_2601.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Article {
    private final int articleId;
    private final String title;
    private final String content;

    public Article(int articleId, String title, String content) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
    }

    public int getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    // Fetch all articles from the database
    public static List<Article> fetchAllArticles(DatabaseConnection dbConnection) {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT article_id, title, content FROM news";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int articleId = rs.getInt("article_id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                articles.add(new Article(articleId, title, content));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    // Check if an article is liked by a user
    public static boolean isArticleLiked(DatabaseConnection dbConnection, int userId, int articleId) {
        String query = "SELECT COUNT(*) FROM user_preferences WHERE userid = ? AND article_id = ? AND rating = 5";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, articleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get liked article titles for a user
    public static List<String> getLikedArticleTitles(DatabaseConnection dbConnection, int userId) {
        List<String> likedTitles = new ArrayList<>();
        String queryLiked = "SELECT liked_articles FROM users WHERE userid = ?";
        String queryTitle = "SELECT title FROM news WHERE article_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmtLiked = conn.prepareStatement(queryLiked);
             PreparedStatement stmtTitle = conn.prepareStatement(queryTitle)) {

            stmtLiked.setInt(1, userId);
            ResultSet rsLiked = stmtLiked.executeQuery();

            if (rsLiked.next()) {
                String likedArticles = rsLiked.getString("liked_articles");
                if (likedArticles != null) {
                    String[] articleIds = likedArticles.split(",");
                    for (String articleId : articleIds) {
                        stmtTitle.setInt(1, Integer.parseInt(articleId.trim()));
                        ResultSet rsTitle = stmtTitle.executeQuery();
                        if (rsTitle.next()) {
                            likedTitles.add(rsTitle.getString("title"));
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likedTitles;
    }

    // Get skipped article titles for a user
    public static List<String> getSkippedArticleTitles(DatabaseConnection dbConnection, int userId) {
        List<String> skippedTitles = new ArrayList<>();
        String querySkipped = "SELECT skipped_articles FROM users WHERE userid = ?";
        String queryTitle = "SELECT title FROM news WHERE article_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmtSkipped = conn.prepareStatement(querySkipped);
             PreparedStatement stmtTitle = conn.prepareStatement(queryTitle)) {

            stmtSkipped.setInt(1, userId);
            ResultSet rsSkipped = stmtSkipped.executeQuery();

            if (rsSkipped.next()) {
                String skippedArticles = rsSkipped.getString("skipped_articles");
                if (skippedArticles != null) {
                    String[] articleIds = skippedArticles.split(",");
                    for (String articleId : articleIds) {
                        stmtTitle.setInt(1, Integer.parseInt(articleId.trim()));
                        ResultSet rsTitle = stmtTitle.executeQuery();
                        if (rsTitle.next()) {
                            skippedTitles.add(rsTitle.getString("title"));
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skippedTitles;
    }
}
