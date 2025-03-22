package Models;

import com.example.cw_2601.DatabaseConnection;
import com.example.cw_2601.NewsDatabase;
import com.example.cw_2601.NewsFetcher;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import org.json.JSONArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Admin {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password123";

    // Fetch articles from the NewsFetcher and store them in the database
    public void fetchArticles() {
        try {
            JSONArray articles = NewsFetcher.fetchNews();
            NewsDatabase.storeArticles(articles);

            showAlert(AlertType.INFORMATION, "Success", "Articles Fetched Successfully",
                    "A total of " + articles.length() + " articles were fetched and stored.");
        } catch (Exception e) {
            e.printStackTrace();

            showAlert(AlertType.ERROR, "Error", "Failed to Fetch Articles",
                    "An error occurred while fetching articles. Please try again.");
        }
    }

    // Placeholder for adding articles
    public void addArticles() {
        TextInputDialog titleDialog = new TextInputDialog();
        titleDialog.setTitle("Add Article");
        titleDialog.setHeaderText("Enter Article Title");
        titleDialog.setContentText("Title:");

        String title = titleDialog.showAndWait().orElse(null);

        if (title == null || title.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "No Title Entered", "Please provide a valid title.");
            return;
        }

        TextInputDialog contentDialog = new TextInputDialog();
        contentDialog.setTitle("Add Article");
        contentDialog.setHeaderText("Enter Article Content");
        contentDialog.setContentText("Content:");

        String content = contentDialog.showAndWait().orElse(null);

        if (content == null || content.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "No Content Entered", "Please provide valid content.");
            return;
        }

        // Insert the new article into the database
        try (Connection conn = new DatabaseConnection().getConnection()) {
            String sql = "INSERT INTO news (title, content) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, title);
                stmt.setString(2, content);
                stmt.executeUpdate(); // Execute the insert operation
            }

            showAlert(AlertType.INFORMATION, "Success", "Article Added Successfully",
                    "The article has been added to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to Add Article",
                    "An error occurred while adding the article. Please try again.");
        }

    }

    public void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public boolean validateCredentials(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }
}
