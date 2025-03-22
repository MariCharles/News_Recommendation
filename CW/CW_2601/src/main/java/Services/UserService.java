package Services;

import com.example.cw_2601.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    private final DatabaseConnection dbConnector;
    private static String currentUsername;

    public UserService(DatabaseConnection dbConnector) {
        this.dbConnector = dbConnector;
    }

    // Authenticate user credentials
    public boolean authenticateUser(String username, String password, DatabaseConnection dbConnector) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    currentUsername = username;
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to authenticate user: " + e.getMessage());
        }
        return false;
    }

    // Retrieve current user ID based on username
    public static int getCurrentUserId() {
        if (currentUsername == null) {
            throw new IllegalStateException("No user is currently logged in.");
        }

        String query = "SELECT userid FROM users WHERE username = ?";
        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, currentUsername);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("userid");
                } else {
                    throw new SQLException("User ID not found for the current username.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve the current user ID.", e);
        }
    }

    // Register a new user
    public boolean registerUser(String username, String password, String preferences) {
        String query = "INSERT INTO users (username, password, preferences) VALUES (?, ?, ?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, preferences);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to register user: " + e.getMessage());
        }
        return false;
    }

    // Update user preferences for a specific article
    public boolean updateUserPreferences(int userId, int articleId, int preferenceValue) {
        String query = "INSERT INTO user_preferences (userid, article_id, rating) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE rating = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, articleId);
            stmt.setInt(3, preferenceValue);
            stmt.setInt(4, preferenceValue);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update the user's article actions
    public boolean updateUserArticleAction(int userId, String column, int articleId) {
        String query = "UPDATE users SET " + column + " = CASE "
                + "WHEN " + column + " IS NULL THEN ? "
                + "ELSE CONCAT(" + column + ", ',', ?) END "
                + "WHERE userid = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String articleIdString = String.valueOf(articleId);
            stmt.setString(1, articleIdString);
            stmt.setString(2, articleIdString);
            stmt.setInt(3, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean verifyPassword(int userId, String currentPassword) {
        String query = "SELECT password FROM users WHERE userid = ?";
        try (PreparedStatement statement = dbConnector.getConnection().prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return storedPassword.equals(currentPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(int userId, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE userid = ?";
        try (PreparedStatement statement = dbConnector.getConnection().prepareStatement(query)) {
            statement.setString(1, newPassword);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
