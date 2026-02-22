import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;

public class Panel_Header {

    private static ImageView createImageView(String path, double size) {
        try {
            Image image = new Image(path);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(size);
            imageView.setFitHeight(size);
            imageView.setPreserveRatio(true);
            return imageView;
        } catch (Exception e) {
            System.err.println("Error loading image from path: " + path);
            return new ImageView();
        }
    }

    public static VBox createHeader(MainWindow mainWindow) {
        VBox header = new VBox();

        header.setStyle("-fx-background-color: #FFFFFF;");
        header.setAlignment(Pos.CENTER_RIGHT);
        header.setPadding(new Insets(10, 30, 10, 30));

        header.setMaxWidth(Double.MAX_VALUE);
        header.setPrefHeight(UI_Utilities.USER_ICON_SIZE + 30);

        HBox headerContent = new HBox(20);
        headerContent.setAlignment(Pos.CENTER_RIGHT);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // User Icon Button
        ImageView userIcon = createImageView(UI_Utilities.USER_ICON_PATH, UI_Utilities.USER_ICON_SIZE);

        Button userButton = new Button();
        userButton.setGraphic(userIcon);
        userButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        userButton.setPadding(Insets.EMPTY);

        // FIX: Now correctly navigates to the individual account settings/profile page
        userButton.setOnAction(e -> {
            mainWindow.showAccount();
        });

        headerContent.getChildren().addAll(spacer, userButton);
        header.getChildren().add(headerContent);

        return header;
    }
}