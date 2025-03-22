package com.example.cw_2601;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class NewsDatabase {

    // Keywords for categorization
    private static final String[] TECHNOLOGY_KEYWORDS = {"tech", "technology", "software", "hardware", "innovation", "computer", "robotics", "ai"};
    private static final String[] HEALTH_KEYWORDS = {"health", "medicine", "doctor", "hospital", "disease", "treatment", "healthcare"};
    private static final String[] SPORTS_KEYWORDS = {"sports", "football", "basketball", "soccer", "athlete", "match", "game", "stadium"};
    private static final String[] ENTERTAINMENT_KEYWORDS = {"entertainment", "movie", "music", "actor", "actress", "show", "celebrity", "film"};
    private static final String[] BUSINESS_KEYWORDS = {"business", "market", "economy", "company", "stocks", "investment"};
    private static final String[] POLITICS_KEYWORDS = {"politics", "government", "election", "president", "congress", "party"};
    private static final String[] WEATHER_KEYWORDS = {"weather", "storm", "rain", "temperature", "forecast", "climate", "snow", "hurricane", "flood"};

    // Store articles in the database
    public static void storeArticles(JSONArray articles) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(true);

            // SQL statement to insert articles
            String insertSQL = "INSERT INTO news (title, author, description, url, published_at, content, category) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertSQL);

            int insertedRows = 0;

            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);

                String title = article.optString("title", "No title");
                String author = article.optString("author", "Unknown");
                String description = article.optString("description", "No description");
                String url = article.optString("url", "No URL");
                String publishedAt = article.optString("publishedAt", "Unknown date");
                String content = article.optString("content", "").trim();

                // Skip articles without content
                if (content.isEmpty()) {
                    System.out.println("Skipping article with no content: " + title);
                    continue;
                }

                // Check if the article already exists in the database
                if (isArticleAlreadyStored(conn, url)) {
                    System.out.println("Article already exists in the database: " + title);
                    continue;
                }

                // Preprocess the content
                String processedContent = preprocessText(content);

                // Categorize the article
                String category = categorizeArticle(processedContent);

                // Set parameters for the INSERT statement
                stmt.setString(1, title);
                stmt.setString(2, author);
                stmt.setString(3, description);
                stmt.setString(4, url);
                stmt.setString(5, publishedAt);
                stmt.setString(6, content);
                stmt.setString(7, category);

                // Execute the update
                insertedRows += stmt.executeUpdate();
            }

            System.out.println("Inserted rows: " + insertedRows);
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error during database insertion:");
            e.printStackTrace();
        }
    }

    // Checks if an article with the given URL is already stored in the database
    private static boolean isArticleAlreadyStored(Connection conn, String url) {
        String checkSQL = "SELECT COUNT(*) FROM news WHERE url = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
            checkStmt.setString(1, url);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error while checking for existing article:");
            e.printStackTrace();
        }
        return false;
    }

    // Categorize the article based on keywords
    private static String categorizeArticle(String content) {
        if (containsKeywords(content, TECHNOLOGY_KEYWORDS)) {
            return "Technology";
        } else if (containsKeywords(content, HEALTH_KEYWORDS)) {
            return "Health";
        } else if (containsKeywords(content, SPORTS_KEYWORDS)) {
            return "Sports";
        } else if (containsKeywords(content, ENTERTAINMENT_KEYWORDS)) {
            return "Entertainment";
        } else if (containsKeywords(content, BUSINESS_KEYWORDS)) {
            return "Business and Finance";
        } else if (containsKeywords(content, POLITICS_KEYWORDS)) {
            return "Politics";
        } else if (containsKeywords(content, WEATHER_KEYWORDS)) {
            return "Weather";
        } else {
            return "General";
        }
    }

    // Check if the content contains any of the keywords from the provided array
    private static boolean containsKeywords(String content, String[] keywords) {
        for (String keyword : keywords) {
            if (content.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // Preprocessing text by converting to lowercase, removing stopwords, punctuation, etc.
    private static String preprocessText(String text) {
        text = text.toLowerCase();

        // Remove punctuation and numbers
        text = text.replaceAll("[^a-zA-Z\\s]", "");

        // Define stopwords (using HashSet for faster lookup)
        Set<String> stopwords = new HashSet<>(Arrays.asList(
                "a", "an", "the", "and", "is", "in", "of", "to", "on", "for", "with",
                "that", "it", "at", "by", "as", "was", "were", "be", "this", "but", "not",
                "are", "or", "from", "which", "their", "they", "we", "you", "he", "she"
        ));

        // Tokenize text into words
        List<String> words = Arrays.asList(text.split("\\s+"));

        // Remove stopwords from the list of words
        words = words.stream().filter(word -> !stopwords.contains(word)).collect(Collectors.toList());

        // Join the words back into a string with single spaces and trim any leading/trailing spaces
        return String.join(" ", words).replaceAll("\\s+", " ").trim();
    }
}
