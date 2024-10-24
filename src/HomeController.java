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
import javafx.scene.layout.VBox;

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

    @FXML
    private Label jumpingJaksLv;
    @FXML
    private Label armCirclesLv;
    @FXML
    private Label legSwingsLv;
    @FXML
    private Label dynamicStretchesLv;
    @FXML
    private Label squatsLv;
    @FXML
    private Label cardioLv;
    @FXML
    private Label deadLiftLv;
    @FXML
    private Label benchPressLv;
    @FXML
    private Label overheadPressLv;
    @FXML

    private VBox boxjumpingJaksLv;
    @FXML
    private VBox boxarmCirclesLv;
    @FXML
    private VBox boxlegSwingsLv;
    @FXML
    private VBox boxdynamicStretchesLv;
    @FXML
    private VBox boxsquatsLv;
    @FXML
    private VBox boxcardioLv;
    @FXML
    private VBox boxdeadLiftLv;
    @FXML
    private VBox boxbenchPressLv;
    @FXML
    private VBox boxoverheadPressLv;



    private int jumpingJaks = 0;
    private int armCircles = 0;
    private int legSwings = 0;
    private int dynamicStretches = 0;
    private int squats = 0;
    private int cardio = 0;
    private int deadLift = 0;
    private int benchPress = 0;
    private int overheadPress = 0;

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
        last7DaysActivities(1);
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
        if (activityType == null || activityTime.isEmpty() || !isNumeric(activityTime)) {
            errorLabel.setText("Activity type and time cannot be empty! also time must be a number");
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
                last7DaysActivities(1);
            } else {
                errorLabel.setText("Failed to submit activity!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Database error: " + e.getMessage());
        }
    }


    private void last7DaysActivities (int day){
        int userId =  Session.getUserId();

        String query = "SELECT * FROM daily_activity " + "WHERE activity_date >= DATE_SUB(CURDATE(), INTERVAL "+ day +" DAY) " + "AND user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            int jJaks = 0;
            int aCircles = 0;
            int lSwings = 0;
            int dStretches = 0;
            int sqt = 0;
            int cdo = 0;
            int dLift = 0;
            int bPress = 0;
            int ohPress = 0;

            while (resultSet.next()) {
                
                String activityType = resultSet.getString("activity_data"); 
                int activityTime = resultSet.getInt("activity_time");
                System.out.println(activityType+ " " + activityTime);


                switch (activityType) {
                    case Activity.jumpingJaks:
                        jJaks+= activityTime;
                        break;
                    case Activity.armCircles:
                        aCircles+= activityTime;
                        break;
                    case Activity.legSwings:
                        lSwings+= activityTime;
                        break;
                    case Activity.dynamicStretches:
                        dStretches+= activityTime;
                        break;
                    case Activity.squats:
                        sqt+= activityTime;
                        break;
                    case Activity.cardio:
                        cdo+= activityTime;
                        break;
                    case Activity.deadLift:
                        dLift+= activityTime;
                        break;
                    case Activity.benchPress:
                        bPress+= activityTime;
                        break;
                    default:
                        ohPress+= activityTime;
                        break;
                }
            }
            jumpingJaks = jJaks;
            legSwings = lSwings;
            armCircles = aCircles;
            dynamicStretches = dStretches;
            squats = sqt;
            cardio = cdo;
            deadLift = dLift;
            benchPress = bPress;
            overheadPress = ohPress;
            setSummary();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handle7DayButtonClick() {
        last7DaysActivities(7);
    }

    @FXML
    public void handleMonthButtonClick() {
        last7DaysActivities(30);
    }



    private void setSummary() {
        jumpingJaksLv.setText(jumpingJaks+"");
        boxjumpingJaksLv.setStyle((jumpingJaks > 20) ? "-fx-border-color: green;" : (jumpingJaks > 0) ? "-fx-border-color: #CC4B1E; " : "-fx-border-color: red" );
        armCirclesLv.setText(armCircles+"");
        boxarmCirclesLv.setStyle((armCircles > 20) ? "-fx-border-color: green;" : (armCircles > 0) ? "-fx-border-color: #CC4B1E; " : "-fx-border-color: red" );
        legSwingsLv.setText(legSwings+"");
        boxlegSwingsLv.setStyle((legSwings > 20) ? "-fx-border-color: green;" : (legSwings > 0) ? "-fx-border-color: #CC4B1E; " : "-fx-border-color: red" );
        dynamicStretchesLv.setText(dynamicStretches+"");
        boxdynamicStretchesLv.setStyle((dynamicStretches > 20) ? "-fx-border-color: green;" : (dynamicStretches > 0) ? "-fx-border-color: #CC4B1E; " : "-fx-border-color: red" );
        squatsLv.setText(squats+"");
        boxsquatsLv.setStyle((squats > 20) ? "-fx-border-color: green;" : (squats > 0) ? "-fx-border-color: #CC4B1E; " : "-fx-border-color: red;" );
        cardioLv.setText(cardio+"");
        boxcardioLv.setStyle((cardio > 20) ? "-fx-border-color: green;" : (cardio > 0) ? "-fx-border-color: #CC4B1E; " : "-fx-border-color: red;" );
        deadLiftLv.setText(deadLift+"");
        System.out.println(deadLift);
        boxdeadLiftLv.setStyle((deadLift > 20) ? "-fx-border-color: green;" : (deadLift > 0) ? "-fx-border-color: #CC4B1E; " : "-fx-border-color: red;" );
        benchPressLv.setText(benchPress+"");
        boxbenchPressLv.setStyle((benchPress > 20) ? "-fx-border-color: green;" : (benchPress > 0) ? "-fx-border-color: #CC4B1E; " : "-fx-border-color: red;" );
        overheadPressLv.setText(overheadPress+"");
        boxoverheadPressLv.setStyle((overheadPress > 20) ? "-fx-border-color: green;" : (overheadPress > 0) ? "-fx-border-color: #CC4B1E; " : "-fx-border-color: red;" );
    }
}