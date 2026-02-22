import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import java.sql.Connection;

public class Page_ServerConnection {

    public static HBox createConnectionRoot(MainWindow mainWindow) {
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        VBox connectionBox = new VBox(20);
        connectionBox.setAlignment(Pos.CENTER);
        connectionBox.setPadding(new Insets(50));
        connectionBox.setMaxWidth(500);
        connectionBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        // Title
        Label titleLabel = new Label("FMDCINVision System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#1f5a17"));

        Label subtitleLabel = new Label("Database Connection Setup");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitleLabel.setTextFill(Color.web("#666666"));

        // Server IP Field
        Label serverLabel = new Label("Server IP Address:");
        serverLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextField serverIPField = new TextField();
        serverIPField.setPromptText("e.g., 192.168.1.100 or localhost");
        serverIPField.setText("localhost"); // Default value
        serverIPField.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 10;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-radius: 5;"
        );

        // User Type Selection
        Label userTypeLabel = new Label("Select User Type:");
        userTypeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        ComboBox<String> userTypeBox = new ComboBox<>();
        userTypeBox.getItems().addAll("SUPERADMIN", "ADMIN", "USER");
        userTypeBox.setPromptText("Select User Type");
        userTypeBox.setPrefWidth(Double.MAX_VALUE);
        userTypeBox.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-background-radius: 5;"
        );

        // Description Labels
        Label descLabel = new Label();
        descLabel.setFont(Font.font("Arial", 12));
        descLabel.setTextFill(Color.web("#666666"));
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(450);

        userTypeBox.setOnAction(e -> {
            String selected = userTypeBox.getValue();
            if (selected != null) {
                switch(selected) {
                    case "SUPERADMIN":
                        descLabel.setText("Full system access - Manage accounts, projects, warehouses, and view all history.");
                        descLabel.setTextFill(Color.web("#1f5a17"));
                        break;
                    case "ADMIN":
                        descLabel.setText("Limited access - Manage projects and warehouses. Cannot manage user accounts.");
                        descLabel.setTextFill(Color.web("#ff8c00"));
                        break;
                    case "USER":
                        descLabel.setText("Read-only access - View projects and information only. Cannot make changes.");
                        descLabel.setTextFill(Color.web("#0066cc"));
                        break;
                }
            }
        });

        // Connect Button
        Button connectBtn = new Button("CONNECT TO SERVER");
        connectBtn.setPrefWidth(Double.MAX_VALUE);
        connectBtn.setPrefHeight(50);
        connectBtn.setStyle(
                "-fx-background-color: #1f5a17;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 5;"
        );

        // Status Label
        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(450);
        statusLabel.setAlignment(Pos.CENTER);

        // Connect Button Action
        connectBtn.setOnAction(e -> {
            String serverIP = serverIPField.getText().trim();
            String userType = userTypeBox.getValue();

            // Validation
            if (serverIP.isEmpty()) {
                statusLabel.setText("⚠ Please enter server IP address");
                statusLabel.setTextFill(Color.RED);
                return;
            }

            if (userType == null) {
                statusLabel.setText("⚠ Please select user type");
                statusLabel.setTextFill(Color.RED);
                return;
            }

            // Show connecting status
            statusLabel.setText("⏳ Connecting to server...");
            statusLabel.setTextFill(Color.web("#0066cc"));
            connectBtn.setDisable(true);

            // Try to connect in a separate thread to avoid blocking UI
            new Thread(() -> {
                try {
                    // Configure database connection
                    DBConnection.configure(userType, serverIP);

                    // Test connection
                    Connection conn = DBConnection.getConnection();
                    conn.close();

                    // Success - update UI on JavaFX thread
                    javafx.application.Platform.runLater(() -> {
                        statusLabel.setText("✓ Connection successful!");
                        statusLabel.setTextFill(Color.web("#1f5a17"));

                        // Set user type in MainWindow
                        mainWindow.setUserType(userType);

                        // Wait a moment then proceed to role selection
                        javafx.animation.PauseTransition pause =
                                new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
                        pause.setOnFinished(event -> mainWindow.showRoleSelection());
                        pause.play();
                    });

                } catch (Exception ex) {
                    // Connection failed - update UI on JavaFX thread
                    javafx.application.Platform.runLater(() -> {
                        statusLabel.setText("✗ Connection failed: " + ex.getMessage());
                        statusLabel.setTextFill(Color.RED);
                        connectBtn.setDisable(false);
                    });
                    ex.printStackTrace();
                }
            }).start();
        });

        // Assembly
        VBox serverSection = new VBox(5, serverLabel, serverIPField);
        VBox userTypeSection = new VBox(5, userTypeLabel, userTypeBox, descLabel);

        connectionBox.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                new Separator(),
                serverSection,
                userTypeSection,
                connectBtn,
                statusLabel
        );

        root.getChildren().add(connectionBox);
        return root;
    }
}