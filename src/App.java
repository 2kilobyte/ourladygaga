import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml")); // Make sure the FXML file is in the right location
            // FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeScene.fxml")); // Make sure the FXML file is in the right location
            Parent root = loader.load(); // Load the FXML

            root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            // Set the title of the window
            primaryStage.setTitle("Gym Workout Tracker");

            // Create a new scene with the loaded FXML layout
            Scene scene = new Scene(root);

            // Set the scene to the primary stage (main window)
            primaryStage.setScene(scene);

            // Show the window
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}