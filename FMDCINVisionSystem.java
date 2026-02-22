import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;

public class FMDCINVisionSystem extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("FMDCINVision System");

        // Set application icon
        try {
            Image icon = new Image(UI_Utilities.LOGO_PATH);
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Failed to load application icon: " + e.getMessage());
        }

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setFullScreen(UI_Utilities.FULLSCREEN);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);

        MainWindow mainWindow = new MainWindow(primaryStage);

        // START WITH SERVER CONNECTION SCREEN
        // This allows the user to select which computer they're on
        // and connect to the database server
        mainWindow.showServerConnection();

        mainWindow.showWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}