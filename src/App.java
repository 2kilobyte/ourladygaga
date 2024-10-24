import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));  
            Parent root = loader.load();

            root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            primaryStage.setTitle("Gym Workout Tracker");


            Scene scene = new Scene(root);

            primaryStage.setScene(scene);


            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); 
    }
}