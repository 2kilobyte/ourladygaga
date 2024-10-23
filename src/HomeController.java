import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class HomeController {

    @FXML
    private ListView<String> activityListView;

    @FXML
    private Label userNameLabel;

    @FXML
    private ComboBox<String> activityComboBox;

    @FXML
    private TextField activityTimeField; 

    @FXML
    private Label errorLabel; // Label to show error messages
    @FXML
    private Label successLabel; // Label to show error messages

    // Method to set user data in the new scene
    public void setUserData(String username, int userId) {
        userNameLabel.setText("Welcome, " + username );
    }


    @FXML
    public void initialize() {
        // Add items to the ComboBox
        activityComboBox.getItems().addAll(
            "Jumping jacks", 
            "Arm circles", 
            "Leg swings", 
            "Dynamic stretches",
            "Squats",
            "Cardio",
            "Dead lift",
            "Bench Press",
            "Overhead Press"
        );
        loadActivities();
    }

    private void loadActivities() {
        List<String> activities = new ArrayList<>();
        int userId = Session.getUserId();

        String activityQuery = "SELECT id, activity_data, activity_time, activity_date FROM daily_activity WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(activityQuery)){
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            int count = 1;
            while (rs.next()) {
                int activityID = count;
                count++;
                String activityType = rs.getString("activity_data");
                int activityTime = rs.getInt("activity_time");
                String activityDate = rs.getString("activity_date");
                activities.add(activityID + ". Activity: " + activityType + ", Time: " + activityTime + " mins, Date: " + activityDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        activityListView.getItems().clear();
        activityListView.getItems().addAll(activities);
    }

    @FXML
    private void handleSubmitButtonClick() {
        String activityType = activityComboBox.getValue();
        String activityTime = activityTimeField.getText();
        
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = currentDate.format(formatter);


        // Clear previous error message
        errorLabel.setText("");

        // Check if fields are empty
        if (activityType == null || activityTime.isEmpty()) {
            errorLabel.setText("Activity type and time cannot be empty!");
            return;
        }


        int time = Integer.parseInt(activityTime);


        int userId = Session.getUserId(); // Implement this 

        // Insert data into database
        String insertSQL = "INSERT INTO daily_activity (user_id, activity_data, activity_time, activity_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, activityType);
            preparedStatement.setInt(3, time);
            preparedStatement.setString(4, dateString);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                successLabel.setText("Activity submitted successfully!");
                activityComboBox.setValue("");
                activityTimeField.setText("");
                loadActivities();
            } else {
                errorLabel.setText("Failed to submit activity!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Database error: " + e.getMessage());
        }
    }
}