package Models;

import com.example.cw_2601.DatabaseConnection;
import Services.UserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int userId;
    private String username;
    private String password;
    private String preferences;

    // Constructor
    public User(int userId, String username, String password, String preferences) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.preferences = preferences;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", preferences='" + preferences + '\'' +
                '}';
    }

    public boolean signIn(DatabaseConnection dbConnector, UserService userService) {
        if (userService.authenticateUser(this.username, this.password, dbConnector)) {
            String query = "SELECT * FROM users WHERE username = ?";
            try (Connection conn = dbConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, this.username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        this.userId = rs.getInt("userid");
                        return true;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Failed to retrieve user details: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean register(UserService userService, String preferences) {
        return userService.registerUser(this.username, this.password, preferences);
    }
}
