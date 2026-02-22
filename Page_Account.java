

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import java.util.Map;
import java.util.LinkedHashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Page_Account {

    private static final Color DATA_LABEL_COLOR = Color.web("#AAAAAA");
    private static final Color EDIT_BUTTON_BG = Color.web("#006400");
    private static final Color EDIT_BUTTON_HOVER_BG = Color.web("#004d00");

    public static BorderPane createPage(MainWindow mainWindow) {

        String currentUserId = "JC001";

        Map<String, String> personalDetailsData = new LinkedHashMap<>();
        Map<String, String> contactDetailsData = new LinkedHashMap<>();
        Map<String, String> accountDetailsData = new LinkedHashMap<>();
        Map<String, String> passwordDetailsData = new LinkedHashMap<>();

        try (Connection con = (Connection) DBConnection.getConnection()) {

            String sql = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, currentUserId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                personalDetailsData.put("First Name", rs.getString("first_name"));
                personalDetailsData.put("Extension Name", rs.getString("extension_name"));
                personalDetailsData.put("Middle Name", rs.getString("middle_name"));
                personalDetailsData.put("Surname", rs.getString("surname"));
                personalDetailsData.put("Birthdate", rs.getDate("birthdate").toString());
                personalDetailsData.put("Age", String.valueOf(rs.getInt("age")));

                contactDetailsData.put("Home Address", rs.getString("home_address"));
                contactDetailsData.put("Contact Number", rs.getString("contact_number"));
                contactDetailsData.put("Email Address", rs.getString("email"));

                accountDetailsData.put("User ID", rs.getString("user_id"));
                accountDetailsData.put("User Role", rs.getString("role"));
                accountDetailsData.put("Account created on", rs.getDate("created_on").toString());
                accountDetailsData.put("Account created by", rs.getString("created_by"));
                accountDetailsData.put("Account last updated", rs.getDate("last_updated").toString());

                passwordDetailsData.put("Password", "••••••••");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + "; -fx-border-width: 0;");

        root.setLeft(Panel_Sidebar.createSidebar(mainWindow, "Account"));

        BorderPane rightSide = new BorderPane();
        rightSide.setStyle("-fx-background-color: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + "; -fx-border-width: 0;");

        VBox topPanel = new VBox(
                Panel_Header.createHeader(mainWindow),
                Panel_Title.createTitle("ACCOUNT SETTINGS")
        );
        rightSide.setTop(topPanel);

        VBox bodyPanel = new VBox(20);
        bodyPanel.setStyle("-fx-background-color: #FFFFFF;");
        VBox.setVgrow(bodyPanel, Priority.ALWAYS);

        bodyPanel.getChildren().addAll(
                createSubPanel(mainWindow, "Personal Details", personalDetailsData, true),
                createSubPanel(mainWindow, "Contact Details", contactDetailsData, true),
                createSubPanel(mainWindow, "Account Details", accountDetailsData, false),
                createSubPanel(mainWindow, "Password Details", passwordDetailsData, true)
        );

        VBox mainContent = new VBox(0, bodyPanel);
        mainContent.setPadding(new Insets(10, 20, 20, 20));
        mainContent.setStyle("-fx-background-color: #FFFFFF;");
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        ScrollPane scrollPane = Panel_Scrollbar.createScrollPane(mainContent);

        rightSide.setCenter(scrollPane);
        root.setCenter(rightSide);

        return root;
    }

    private static VBox createSubPanel(MainWindow mainWindow, String title, Map<String, String> data, boolean showEditButton) {

        String lightGreyHex = UI_Utilities.toHexString(Color.rgb(250, 250, 250));

        VBox panel = new VBox(10);
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

        if (showEditButton) {

            Button editButton = new Button("EDIT");

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

            editButton.setOnAction(e ->
                    System.out.println("EDIT button clicked for " + title)
            );

            headerContainer.setRight(editButton);
        }

        panel.getChildren().add(headerContainer);

        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(15);
        grid.setPadding(new Insets(10, 0, 0, 0));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col1.setHgrow(Priority.ALWAYS);
        col1.setHalignment(HPos.LEFT);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        col2.setHgrow(Priority.ALWAYS);
        col2.setHalignment(HPos.LEFT);

        grid.getColumnConstraints().addAll(col1, col2);

        int row = 0;
        int col = 0;

        for (Map.Entry<String, String> entry : data.entrySet()) {

            Label dataLabel = new Label(entry.getKey());
            dataLabel.setFont(Font.font(UI_Utilities.FONT_FAMILY, UI_Utilities.FONT_SIZE_EXTRA_SMALL));
            dataLabel.setTextFill(DATA_LABEL_COLOR);

            Label valueLabel = entry.getKey().toLowerCase().contains("password")
                    ? new Label("••••••••")
                    : new Label(entry.getValue());

            valueLabel.setFont(Font.font(
                    UI_Utilities.FONT_FAMILY,
                    FontWeight.NORMAL,
                    UI_Utilities.FONT_SIZE_EXTRA_SMALL
            ));
            valueLabel.setTextFill(UI_Utilities.BLACK);
            valueLabel.setWrapText(true);

            VBox fieldBox = new VBox(2, dataLabel, valueLabel);
            grid.add(fieldBox, col, row);

            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }

        panel.getChildren().add(grid);
        return panel;
    }
}
