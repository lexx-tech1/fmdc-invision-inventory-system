import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

public class Page_AccessDenied {

    public static BorderPane createPage(MainWindow mainWindow) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        VBox centerBox = new VBox(30);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(100));
        centerBox.setMaxWidth(600);
        centerBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        // Icon (you can replace with an actual icon)
        Label iconLabel = new Label("🚫");
        iconLabel.setFont(Font.font(80));

        // Title
        Label titleLabel = new Label("Access Denied");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.web("#d32f2f"));

        // Message
        Label messageLabel = new Label("You do not have permission to access this page.");
        messageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        messageLabel.setTextFill(Color.web("#666666"));
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(500);
        messageLabel.setAlignment(Pos.CENTER);

        // User Type Info
        String userType = mainWindow.getUserType() != null ? mainWindow.getUserType() : "UNKNOWN";
        Label userTypeLabel = new Label("Your access level: " + userType);
        userTypeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        userTypeLabel.setTextFill(Color.web("#333333"));

        // Description based on user type
        Label descLabel = new Label();
        descLabel.setFont(Font.font("Arial", 14));
        descLabel.setTextFill(Color.web("#666666"));
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(500);
        descLabel.setAlignment(Pos.CENTER);

        switch(userType) {
            case "USER":
                descLabel.setText("As a USER, you have read-only access to projects. Contact your administrator for higher access.");
                break;
            case "ADMIN":
                descLabel.setText("As an ADMIN, you can manage projects and warehouses but cannot access user accounts or full system history.");
                break;
            case "SUPERADMIN":
                descLabel.setText("You have full access. If you see this message, there may be a system error.");
                break;
            default:
                descLabel.setText("Please contact your system administrator for access.");
        }

        // Back Button
        Button backBtn = new Button("GO BACK TO PROJECTS");
        backBtn.setPrefWidth(300);
        backBtn.setPrefHeight(50);
        backBtn.setStyle(
                "-fx-background-color: #1f5a17;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 5;"
        );
        backBtn.setOnAction(e -> mainWindow.showProjects());

        centerBox.getChildren().addAll(
                iconLabel,
                titleLabel,
                messageLabel,
                new Separator(),
                userTypeLabel,
                descLabel,
                backBtn
        );

        // Center the box
        VBox wrapper = new VBox(centerBox);
        wrapper.setAlignment(Pos.CENTER);
        root.setCenter(wrapper);

        return root;
    }
}