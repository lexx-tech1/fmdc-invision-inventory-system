import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Panel_Sidebar {

    public static VBox createSidebar(MainWindow mainWindow, String activePage) {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #1f5a17;");
        VBox.setVgrow(sidebar, Priority.ALWAYS);

        // Logo Section
        VBox logoBox = new VBox(10);
        logoBox.setPadding(new Insets(30, 20, 20, 20));
        logoBox.setAlignment(Pos.CENTER);

        try {
            ImageView logo = new ImageView(new Image(UI_Utilities.LOGO_PATH));
            logo.setFitHeight(80);
            logo.setFitWidth(80);
            logo.setPreserveRatio(true);
            logoBox.getChildren().add(logo);
        } catch (Exception e) {
            System.err.println("Logo not found: " + e.getMessage());
        }

        Label titleLabel = new Label("FMDCINVision");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: white;");
        logoBox.getChildren().add(titleLabel);

        // User Type Badge
        String userType = mainWindow.getUserType();
        if (userType != null) {
            Label userTypeBadge = new Label(userType);
            userTypeBadge.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            userTypeBadge.setPadding(new Insets(5, 15, 5, 15));

            switch(userType) {
                case "SUPERADMIN":
                    userTypeBadge.setStyle(
                            "-fx-background-color: #ffd700;" +
                                    "-fx-text-fill: #1f5a17;" +
                                    "-fx-background-radius: 15;"
                    );
                    break;
                case "ADMIN":
                    userTypeBadge.setStyle(
                            "-fx-background-color: #ff8c00;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-background-radius: 15;"
                    );
                    break;
                case "USER":
                    userTypeBadge.setStyle(
                            "-fx-background-color: #0066cc;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-background-radius: 15;"
                    );
                    break;
            }
            logoBox.getChildren().add(userTypeBadge);
        }

        sidebar.getChildren().add(logoBox);

        // Navigation Menu
        VBox menuBox = new VBox(5);
        menuBox.setPadding(new Insets(20, 10, 20, 10));
        VBox.setVgrow(menuBox, Priority.ALWAYS);

        // Get user access level
        boolean isSuperAdmin = mainWindow.isSuperAdmin();
        boolean isAdmin = mainWindow.isAdmin();
        boolean isUser = mainWindow.isUser();

        // HOME - Only for SuperAdmin
        if (isSuperAdmin) {
            menuBox.getChildren().add(
                    createMenuItem("🏠 Home", "Home", activePage, () -> mainWindow.showHome(mainWindow.getUserId()))
            );
        }

        // ACCOUNTS - Only for SuperAdmin
        if (isSuperAdmin) {
            menuBox.getChildren().add(
                    createMenuItem("👥 Accounts", "Accounts", activePage, mainWindow::showAccounts)
            );
        }

        // PROJECTS - All users can access
        menuBox.getChildren().add(
                createMenuItem("🏗 Projects", "Projects", activePage, mainWindow::showProjects)
        );

        // WAREHOUSES - SuperAdmin and Admin only
        if (isSuperAdmin || isAdmin) {
            menuBox.getChildren().add(
                    createMenuItem("📦 Warehouses", "Warehouses", activePage, mainWindow::showWarehouses)
            );
        }

        // HISTORY - Only for SuperAdmin
        if (isSuperAdmin) {
            menuBox.getChildren().add(
                    createMenuItem("📜 History", "History", activePage, mainWindow::showHistory)
            );
        }

        // Show access level info for non-SuperAdmin users
        if (!isSuperAdmin) {
            VBox infoBox = new VBox(10);
            infoBox.setPadding(new Insets(20, 10, 10, 10));
            infoBox.setAlignment(Pos.CENTER);

            Label infoLabel = new Label(isAdmin ?
                    "Limited Access\nManage Projects & Warehouses" :
                    "Read-Only Access\nView Projects Only"
            );
            infoLabel.setFont(Font.font("Arial", 11));
            infoLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.7); -fx-text-alignment: center;");
            infoLabel.setWrapText(true);
            infoLabel.setMaxWidth(200);

            infoBox.getChildren().add(infoLabel);
            menuBox.getChildren().add(infoBox);
        }

        sidebar.getChildren().add(menuBox);

        // Logout Button at bottom
        VBox bottomBox = new VBox();
        bottomBox.setPadding(new Insets(10));

        Button logoutBtn = new Button("🚪 Logout");
        logoutBtn.setPrefWidth(Double.MAX_VALUE);
        logoutBtn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.1);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 5;"
        );
        logoutBtn.setOnAction(e -> {
            // Return to server connection screen
            mainWindow.showServerConnection();
        });

        bottomBox.getChildren().add(logoutBtn);
        sidebar.getChildren().add(bottomBox);

        return sidebar;
    }

    private static Button createMenuItem(String text, String pageName, String activePage, Runnable action) {
        Button btn = new Button(text);
        btn.setPrefWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(15, 20, 15, 20));

        boolean isActive = pageName.equals(activePage);

        if (isActive) {
            btn.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.2);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 14px;" +
                            "-fx-cursor: hand;" +
                            "-fx-background-radius: 5;"
            );
        } else {
            btn.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: rgba(255,255,255,0.9);" +
                            "-fx-font-size: 14px;" +
                            "-fx-cursor: hand;" +
                            "-fx-background-radius: 5;"
            );

            btn.setOnMouseEntered(e -> btn.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.1);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-cursor: hand;" +
                            "-fx-background-radius: 5;"
            ));

            btn.setOnMouseExited(e -> btn.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: rgba(255,255,255,0.9);" +
                            "-fx-font-size: 14px;" +
                            "-fx-cursor: hand;" +
                            "-fx-background-radius: 5;"
            ));
        }

        btn.setOnAction(e -> action.run());

        return btn;
    }
}