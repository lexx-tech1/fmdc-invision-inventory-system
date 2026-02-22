import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane; // ADDED
import javafx.scene.control.TextInputControl; // ADDED - Base class for TextField/PasswordField
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.stage.Screen;

public class Page_Login {

    public static HBox createLoginRoot(MainWindow mainWindow) {
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

        // Create "LOG IN" label
        Label loginLabel = new Label("LOG IN");
        loginLabel.setFont(Font.font(UI_Utilities.FONT_FAMILY, FontWeight.BOLD, UI_Utilities.FONT_SIZE_LARGE));
        loginLabel.setTextFill(UI_Utilities.WHITE);
        loginLabel.setPrefWidth(500);
        loginLabel.setAlignment(Pos.CENTER);

        // --- User ID Field Setup ---
        // Create base TextField
        TextField userIdInput = new TextField();
        userIdInput.setPromptText("User ID");
        userIdInput.setFont(Font.font(UI_Utilities.FONT_FAMILY, UI_Utilities.FONT_SIZE_LARGE));
        userIdInput.setPrefWidth(500);
        userIdInput.setMaxWidth(500);
        userIdInput.setPrefHeight(60);
        userIdInput.setStyle(
                "-fx-background-color: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + ";" +
                        "-fx-text-fill: " + UI_Utilities.toHexString(UI_Utilities.DARK_GREEN) + ";" +
                        "-fx-background-radius: 5;" +
                        "-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);"
        );
        // Wrap the TextField with the icon using the helper method
        StackPane userIdFieldWithIcon = createIconField(userIdInput, UI_Utilities.USER_ICON_PATH);
        // --- End User ID Field Setup ---

        // --- Password Field Setup ---
        // Create base PasswordField
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        passwordInput.setFont(Font.font(UI_Utilities.FONT_FAMILY, UI_Utilities.FONT_SIZE_LARGE));
        passwordInput.setPrefWidth(500);
        passwordInput.setMaxWidth(500);
        passwordInput.setPrefHeight(60);
        passwordInput.setStyle(
                "-fx-background-color: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + ";" +
                        "-fx-text-fill: " + UI_Utilities.toHexString(UI_Utilities.DARK_GREEN) + ";" +
                        "-fx-background-radius: 5;" +
                        "-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);"
        );
        // Wrap the PasswordField with the icon using the helper method
        StackPane passwordFieldWithIcon = createIconField(passwordInput, UI_Utilities.PASSWORD_ICON_PATH);
        // --- End Password Field Setup ---

        // Create error message label
        Label errorLabel = new Label("User ID/Password incorrect! Try again.");
        errorLabel.setFont(Font.font(UI_Utilities.FONT_FAMILY, FontPosture.ITALIC, UI_Utilities.FONT_SIZE_ERROR));
        errorLabel.setTextFill(UI_Utilities.RED);
        errorLabel.setPrefWidth(500);
        errorLabel.setMaxWidth(500);
        errorLabel.setPrefHeight(50);
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setStyle(
                "-fx-background-color: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + ";" +
                        "-fx-background-radius: 5;"
        );
        errorLabel.setVisible(false); // Initially hidden

        // Create LOG IN button
        Button loginButton = createButton("LOG IN", UI_Utilities.WHITE, UI_Utilities.DARK_GREEN);

        // Create BACK button with lighter green background and white text
        Button backButton = createButton("BACK", UI_Utilities.LIGHT_GREEN, UI_Utilities.WHITE);

        // Back button action - return to role selection
        backButton.setOnAction(e -> mainWindow.showRoleSelection());

        // Login button action - navigate to home page when fields are filled
        loginButton.setOnAction(e -> {
            String uid = userIdInput.getText().trim();
            String pwd = passwordInput.getText();   // never trim passwords

            if (uid.isEmpty() || pwd.isEmpty()) {
                errorLabel.setVisible(true);
                return;
            }

            try {
                // 1. Authenticate user
                int userPk = DB_Util.getConnection(uid, pwd); // DB_Util.authenticate returns user PK or -1
                if (userPk == -1) {
                    errorLabel.setVisible(true);
                } else {
                    errorLabel.setVisible(false);

                    // 2. Set user status to ONLINE
                    DB_Util.setUserStatus(uid, "Online");

                    // 3. Show home page
                    mainWindow.showHome(uid);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                errorLabel.setText("Database connection error!");
                errorLabel.setVisible(true);
            }
        });


        // Add the StackPane wrappers to the right panel
        rightPanel.getChildren().addAll(loginLabel, userIdFieldWithIcon, passwordFieldWithIcon, errorLabel, loginButton, backButton);

        return rightPanel;
    }

    /**
     * Creates a StackPane wrapper for a TextInputControl (TextField or PasswordField)
     * to embed an icon on the left side by adjusting the control's padding.
     */
    private static StackPane createIconField(TextInputControl inputField, String iconPath) {
        try {
            // 1. Create ImageView
            Image iconImage = new Image(iconPath);
            ImageView iconView = new ImageView(iconImage);
            iconView.setFitHeight(40); // Size of the icon
            iconView.setFitWidth(40);  // Size of the icon

            // 2. Adjust input field style to accommodate the icon
            String baseStyle = inputField.getStyle();

            // FIX: Increased left padding from 50px to 65px to add left indention/gap for the prompt text.
            inputField.setStyle(baseStyle + "-fx-padding: 0 10 0 65;");

            // 3. Create StackPane and add children
            StackPane stackPane = new StackPane();
            stackPane.setMaxWidth(inputField.getMaxWidth());
            stackPane.setPrefHeight(inputField.getPrefHeight());

            // Add the field first so the icon is on top
            stackPane.getChildren().add(inputField);

            // Add the icon, positioned left with margin
            StackPane.setAlignment(iconView, Pos.CENTER_LEFT);
            // Icon is centered on the left padding space.
            StackPane.setMargin(iconView, new Insets(0, 0, 0, 15));

            stackPane.getChildren().add(iconView);

            return stackPane;

        } catch (Exception e) {
            // Log the error and return the input field without the icon
            System.err.println("Failed to load icon for input field. Check file path/permissions for: " + iconPath + " Error: " + e.getMessage());
            StackPane failedStackPane = new StackPane(inputField);
            failedStackPane.setMaxWidth(inputField.getMaxWidth());
            failedStackPane.setPrefHeight(inputField.getPrefHeight());
            return failedStackPane;
        }
    }

    private static Button createButton(String text, javafx.scene.paint.Color bgColor, javafx.scene.paint.Color textColor) {
        Button button = new Button(text);
        button.setFont(Font.font(UI_Utilities.FONT_FAMILY, FontWeight.BOLD, UI_Utilities.FONT_SIZE_LARGE));
        button.setStyle(
                "-fx-background-color: " + UI_Utilities.toHexString(bgColor) + ";" +
                        "-fx-text-fill: " + UI_Utilities.toHexString(textColor) + ";" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );
        button.setPrefWidth(500);
        button.setPrefHeight(60);

        // Hover and pressed effects
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: " + UI_Utilities.toHexString(bgColor) + ";" +
                        "-fx-text-fill: " + UI_Utilities.toHexString(textColor) + ";" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;" +
                        "-fx-opacity: 0.8;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: " + UI_Utilities.toHexString(bgColor) + ";" +
                        "-fx-text-fill: " + UI_Utilities.toHexString(textColor) + ";" +
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