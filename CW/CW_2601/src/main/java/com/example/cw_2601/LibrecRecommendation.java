package com.example.cw_2601;

import Services.UserService;
import net.librec.conf.Configuration;
import net.librec.data.DataModel;
import net.librec.data.model.TextDataModel;
import net.librec.recommender.Recommender;
import net.librec.recommender.RecommenderContext;
import net.librec.recommender.cf.UserKNNRecommender;
import net.librec.recommender.item.RecommendedItem;
import net.librec.similarity.PCCSimilarity;
import net.librec.similarity.RecommenderSimilarity;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LibrecRecommendation {

    private static final String CSV_FILE_PATH = "user_preferences.csv";
    private final UserService userService;
    private final DatabaseConnection dbConnector;

    public LibrecRecommendation(DatabaseConnection dbConnector) {
        this.dbConnector = dbConnector;
        this.userService = new UserService(dbConnector);
    }

    private void generateCSV() {
        String query = """
            SELECT
                userid,
                article_id,
                rating
            FROM user_preferences;
        """;

        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             FileWriter csvWriter = new FileWriter(CSV_FILE_PATH)) {

            // Write data to CSV
            while (rs.next()) {
                int userID = rs.getInt("userid");
                int articleID = rs.getInt("article_id");
                int rating = rs.getInt("rating");

                csvWriter.append(String.format("%d,%d,%d\n", userID, articleID, rating));
            }

            System.out.println("CSV file generated successfully: " + CSV_FILE_PATH);

        } catch (SQLException | IOException e) {
            System.err.println("Error generating CSV: " + e.getMessage());
        }
    }

    public List<RecommendedItem> generateRecommendationsForCurrentUser() {
        List<RecommendedItem> recommendations = new ArrayList<>();

        try {
            generateCSV();

            // Configuration for the recommendation system
            Configuration conf = new Configuration();
            conf.set("dfs.data.dir", ".");
            conf.set("data.model.splitter", "ratio");
            conf.set("data.splitter.trainset.ratio", "0.8");
            conf.set("data.column.format", "UIR");  // UI: User ID, R: Rating, I: Item ID
            conf.set("data.model.format", "text");
            conf.set("data.input.path", CSV_FILE_PATH);
            conf.set("rec.neighbors.knn.number", "10");

            // Build the data model
            DataModel dataModel = new TextDataModel(conf);
            dataModel.buildDataModel();

            // Initialize similarity metric
            RecommenderSimilarity similarity = new PCCSimilarity();
            similarity.buildSimilarityMatrix(dataModel);

            // Create the recommender context
            RecommenderContext context = new RecommenderContext(conf, dataModel);
            context.setSimilarity(similarity);

            // Initialize and configure the recommender
            Recommender recommender = new UserKNNRecommender();
            recommender.setContext(context);
            recommender.recommend(context);

            // Retrieve all recommendations
            recommendations = recommender.getRecommendedList();

        } catch (Exception e) {
            System.err.println("Error generating recommendations: " + e.getMessage());
            e.printStackTrace();
        }

        return recommendations;
    }

    public List<String> getArticleDetails(List<Integer> articleIds) {
        List<String> articleDetails = new ArrayList<>();
        String query = "SELECT title, content FROM news WHERE article_id = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (Integer articleId : articleIds) {
                stmt.setInt(1, articleId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String title = rs.getString("title");
                        String content = rs.getString("content");
                        articleDetails.add("Title: " + title + "\nContent: " + content);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching article details: " + e.getMessage());
        }

        return articleDetails;
    }
}
