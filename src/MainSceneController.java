import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;


public class MainSceneController {

    @FXML
    private TextField usernameField;  // FXML reference for the username text field

    @FXML
    private PasswordField passwordField;  // FXML reference for the password field

    @FXML
    private Label errorLabel;

    @FXML
    private Label successLabel;

    // This method will be called when the Login button is clicked
    @FXML
    public void handleLoginButtonClick() {
        // Retrieve the username and password entered by the user
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username or password cannot be empty");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()){
            String query = "SELECT id, username FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String userName = resultSet.getString("username");
                // Successful login, load the new scene and pass data
                Session.setUserId(userId);
                Session.setUserName(username);
                loadHomeScene(userName, userId);
            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Database error");
        }
    }


    @FXML
    public void handleRegisterButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username or password cannot be empty.");
            // Show an error alert if username or password is empty
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Check if the username already exists
            String checkQuery = DatabaseConnection.checkUserQuery;
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                ResultSet resultSet = checkStmt.executeQuery();
                resultSet.next();
                if (resultSet.getInt(1) > 0) {
                    errorLabel.setText("Username already exists. Please choose a different username.");
                    return; // User already exists
                }
            }

            // Insert new user
            String insertQuery = DatabaseConnection.registerUserQuery;
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password); 
                insertStmt.executeUpdate();
                successLabel.setText("User registered successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void loadHomeScene(String username, int userId) throws Exception {
        // Load the new scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeScene.fxml"));
        Parent homeScene = loader.load();

        homeScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        // Get the controller of the new scene
        HomeController homeController = loader.getController();

        // Pass data to the new scene
        homeController.setUserData(username, userId);

        // Set the new scene on the stage
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(homeScene));
    }

}