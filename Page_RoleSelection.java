import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

public class Page_RoleSelection {

    public static HBox createRoleSelectionRoot(MainWindow mainWindow) {
        VBox leftWhitePanel = createLeftWhitePanel();
        VBox rightGreenPanel = createRightGreenPanel(mainWindow);

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double panelWidth = screenWidth / 2;

        leftWhitePanel.setPrefWidth(panelWidth);
        rightGreenPanel.setPrefWidth(panelWidth);

        HBox mainContainer = new HBox(leftWhitePanel, rightGreenPanel);
        mainContainer.setSpacing(0);

        return mainContainer;
    }

    private static VBox createLeftWhitePanel() {
        VBox leftPanel = new VBox();
        leftPanel.setStyle("-fx-background-color: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + ";");
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.getChildren().add(createFMDCLogo());
        return leftPanel;
    }

    private static VBox createRightGreenPanel(MainWindow mainWindow) {
        VBox rightPanel = new VBox(30);
        rightPanel.setStyle("-fx-background-color: " + UI_Utilities.toHexString(UI_Utilities.DARK_GREEN) + ";");
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(50));

        // Create "LOG IN AS" label
        Label loginLabel = new Label("LOG IN AS");
        loginLabel.setFont(Font.font(UI_Utilities.FONT_FAMILY, FontWeight.BOLD, UI_Utilities.FONT_SIZE_LARGE));
        loginLabel.setTextFill(UI_Utilities.WHITE);

        // Create role buttons
        Button adminButton = createRoleButton("ADMINISTRATOR", mainWindow);
        Button warehouseButton = createRoleButton("WAREHOUSE KEEPER", mainWindow);
        Button technicalButton = createRoleButton("TECHNICAL STAFF", mainWindow);
        Button marketingButton = createRoleButton("MARKETING STAFF", mainWindow);

        rightPanel.getChildren().addAll(loginLabel, adminButton, warehouseButton, technicalButton, marketingButton);

        return rightPanel;
    }

    private static Button createRoleButton(String text, MainWindow mainWindow) {
        Button button = new Button(text);
        button.setFont(Font.font(UI_Utilities.FONT_FAMILY, FontWeight.BOLD, UI_Utilities.FONT_SIZE_LARGE));
        button.setStyle(
                "-fx-background-color: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + ";" +
                        "-fx-text-fill: " + UI_Utilities.toHexString(UI_Utilities.DARK_GREEN) + ";" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );
        button.setPrefWidth(400);
        button.setPrefHeight(60);

        // Click event - navigate to login page for ADMINISTRATOR
        if (text.equals("ADMINISTRATOR")) {
            button.setOnAction(e -> mainWindow.showLogin("ADMINISTRATOR"));
        }

        // Hover effect
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + ";" +
                        "-fx-text-fill: " + UI_Utilities.toHexString(UI_Utilities.DARK_GREEN) + ";" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;" +
                        "-fx-opacity: 0.9;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + ";" +
                        "-fx-text-fill: " + UI_Utilities.toHexString(UI_Utilities.DARK_GREEN) + ";" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        ));

        return button;
    }

    private static ImageView createFMDCLogo() {
        try {
            ImageView logoImage = new ImageView(new Image(UI_Utilities.LOGO_PATH));
            logoImage.setFitHeight(400);
            logoImage.setFitWidth(400);
            logoImage.setPreserveRatio(true);
            return logoImage;
        } catch (Exception e) {
            System.err.println("Failed to load logo: " + e.getMessage());
            return new ImageView();
        }
    }
}