import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import java.util.Map;
import java.util.LinkedHashMap;

public class Page_AccountsView {

    // Aligned with Page_Account constants
    private static final Color DATA_LABEL_COLOR = Color.web("#AAAAAA");
    private static final Color EDIT_BUTTON_BG = Color.web("#006400"); // Dark Green
    private static final Color EDIT_BUTTON_HOVER_BG = Color.web("#004d00"); // Slightly darker green

    public static BorderPane createPage(MainWindow mainWindow) {
        BorderPane root = new BorderPane();
        // Sync background and border style with Page_Account
        root.setStyle("-fx-background-color: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + "; -fx-border-width: 0;");

        root.setLeft(Panel_Sidebar.createSidebar(mainWindow, "Accounts"));

        BorderPane rightSide = new BorderPane();
        rightSide.setStyle("-fx-background-color: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + "; -fx-border-width: 0;");

        // 1. Top Panel (Header + Title)
        VBox topPanel = new VBox(
                Panel_Header.createHeader(mainWindow),
                Panel_Title.createTitle("ACCOUNT DETAILS")
        );
        rightSide.setTop(topPanel);

        // 2. Data Definitions (Identical to Page_Account)
        Map<String, String> personalDetailsData = new LinkedHashMap<>();
        personalDetailsData.put("First Name", "Juan");
        personalDetailsData.put("Extension Name", "Jr.");
        personalDetailsData.put("Middle Name", "Dela");
        personalDetailsData.put("Birthdate", "01/15/1990");
        personalDetailsData.put("Surname", "Cruz");
        personalDetailsData.put("Age", "34");

        Map<String, String> contactDetailsData = new LinkedHashMap<>();
        contactDetailsData.put("Home Address", "123 Main Street, City, Province 12345");
        contactDetailsData.put("Contact Number", "+63 9123456789");
        contactDetailsData.put("Email Address", "juan.cruz@email.com");

        Map<String, String> accountDetailsData = new LinkedHashMap<>();
        accountDetailsData.put("User ID", "JC001");
        accountDetailsData.put("User Role", "Administrator");
        accountDetailsData.put("Account created on", "01/10/2023");
        accountDetailsData.put("Account created by", "Admin");
        accountDetailsData.put("Account last updated", "01/15/2024");

        Map<String, String> passwordDetailsData = new LinkedHashMap<>();
        passwordDetailsData.put("Password", "••••••••");

        // 3. Body Panel (Spacing 20, White Background)
        VBox bodyPanel = new VBox(20);
        bodyPanel.setStyle("-fx-background-color: #FFFFFF;");
        VBox.setVgrow(bodyPanel, Priority.ALWAYS);

        // Keep the navigation button at the top
        HBox topNavigation = createTopNavigation(mainWindow);

        bodyPanel.getChildren().addAll(
                topNavigation,
                createSubPanel("Personal Details", personalDetailsData, true),
                createSubPanel("Contact Details", contactDetailsData, true),
                createSubPanel("Account Details", accountDetailsData, false),
                createSubPanel("Password Details", passwordDetailsData, true)
        );

        // 4. Main Content Wrapper (Padding: 10, 20, 20, 20 to match Page_Account)
        VBox mainContent = new VBox(0, bodyPanel);
        mainContent.setPadding(new Insets(10, 20, 20, 20));
        mainContent.setStyle("-fx-background-color: #FFFFFF;");
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        rightSide.setCenter(Panel_Scrollbar.createScrollPane(mainContent));
        root.setCenter(rightSide);

        return root;
    }

    private static VBox createSubPanel(String title, Map<String, String> data, boolean showEditButton) {
        // Matches the light grey hex calculation in Page_Account
        String lightGreyHex = UI_Utilities.toHexString(Color.rgb(250, 250, 250));

        VBox panel = new VBox(10);
        // Matches the 30px padding from Page_Account
        panel.setPadding(new Insets(30));
        panel.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-border-color: " + lightGreyHex + ";" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 5;"
        );

        Label panelTitle = new Label(title);
        panelTitle.setFont(Font.font(UI_Utilities.FONT_FAMILY, FontWeight.BOLD, UI_Utilities.FONT_SIZE_EXTRA_MEDIUM));
        panelTitle.setTextFill(UI_Utilities.BLACK);

        BorderPane headerContainer = new BorderPane();
        headerContainer.setLeft(panelTitle);
        BorderPane.setAlignment(panelTitle, Pos.CENTER_LEFT);

        if (showEditButton) {
            Button editButton = new Button("EDIT");

            // Identical style string from Page_Account
            String buttonStyle =
                    "-fx-background-color: " + UI_Utilities.toHexString(EDIT_BUTTON_BG) + ";" +
                            "-fx-text-fill: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + ";" +
                            "-fx-padding: 7 25 7 25;" +
                            "-fx-font-family: '" + UI_Utilities.FONT_FAMILY + "';" +
                            "-fx-font-size: " + UI_Utilities.FONT_SIZE_EXTRA_SMALL + ";" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 5;";

            String hoverStyle =
                    "-fx-background-color: " + UI_Utilities.toHexString(EDIT_BUTTON_HOVER_BG) + ";" +
                            "-fx-text-fill: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + ";" +
                            "-fx-padding: 7 25 7 25;" +
                            "-fx-font-family: '" + UI_Utilities.FONT_FAMILY + "';" +
                            "-fx-font-size: " + UI_Utilities.FONT_SIZE_EXTRA_SMALL + ";" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 5;";

            editButton.setStyle(buttonStyle);
            editButton.setOnMouseEntered(e -> editButton.setStyle(hoverStyle));
            editButton.setOnMouseExited(e -> editButton.setStyle(buttonStyle));

            headerContainer.setRight(editButton);
            BorderPane.setAlignment(editButton, Pos.CENTER_RIGHT);
        }

        panel.getChildren().add(headerContainer);

        // --- Grid Logic Synced ---
        GridPane grid = new GridPane();
        grid.setHgap(40); // Synced to 40
        grid.setVgap(15); // Synced to 15
        grid.setPadding(new Insets(10, 0, 0, 0)); // Matching Page_Account grid padding

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col1.setHgrow(Priority.ALWAYS);
        col1.setHalignment(HPos.LEFT);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        col2.setHgrow(Priority.ALWAYS);
        col2.setHalignment(HPos.LEFT);

        grid.getColumnConstraints().addAll(col1, col2);

        int row = 0, col = 0;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String labelText = entry.getKey();
            String valueText = entry.getValue();

            Label dataLabel = new Label(labelText);
            dataLabel.setFont(Font.font(UI_Utilities.FONT_FAMILY, UI_Utilities.FONT_SIZE_EXTRA_SMALL));
            dataLabel.setTextFill(DATA_LABEL_COLOR);

            Label valueLabel;
            if (labelText.toLowerCase().contains("password")) {
                valueLabel = new Label("••••••••");
            } else {
                valueLabel = new Label(valueText);
            }

            valueLabel.setFont(Font.font(UI_Utilities.FONT_FAMILY, FontWeight.NORMAL, UI_Utilities.FONT_SIZE_EXTRA_SMALL));
            valueLabel.setTextFill(UI_Utilities.BLACK);
            valueLabel.setWrapText(true);

            VBox fieldBox = new VBox(2); // Synced gap
            fieldBox.getChildren().addAll(dataLabel, valueLabel);

            grid.add(fieldBox, col, row);

            if (++col > 1) {
                col = 0;
                row++;
            }
        }

        panel.getChildren().add(grid);
        return panel;
    }

    private static HBox createTopNavigation(MainWindow mainWindow) {
        HBox nav = new HBox();
        // Matching the margin style
        nav.setPadding(new Insets(0, 0, 10, 0));

        Button backBtn = new Button("BACK TO LIST");
        // Using common UI colors and fonts
        backBtn.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: " + UI_Utilities.FONT_SIZE_EXTRA_SMALL + "px; -fx-padding: 10 30; -fx-background-radius: 5; -fx-cursor: hand;");
        backBtn.setOnAction(e -> mainWindow.showAccounts());

        nav.getChildren().add(backBtn);
        return nav;
    }
}