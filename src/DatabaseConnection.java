

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/fitness"; // Change to your DB name
    private static final String USER = "root"; // Change to your DB username
    private static final String PASSWORD = "Pass@123"; 


    public static final String checkUserQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
    public static final String registerUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";

    public static Connection getConnection() {
        Connection connection = null;
        // Change to your DB password
        
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return connection;
    }
}