import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.input.KeyCombination;

public class MainWindow {
    private Stage primaryStage;
    private String selectedRole;
    private String userId;
    private String userType; // SUPERADMIN, ADMIN, USER
    private Scene scene;

    public MainWindow(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("FMDCINVision System");

        if (UI_Utilities.FULLSCREEN) {
            this.primaryStage.setFullScreen(true);
            this.primaryStage.setFullScreenExitHint(""); // Remove exit hint
            this.primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC
        }
    }

    private void setRoot(BorderPane root) {
        if (scene != null) {
            scene.setRoot(root);
        } else {
            scene = new Scene(root);
            scene.setFill(javafx.scene.paint.Color.WHITE);
            primaryStage.setScene(scene);
        }
    }

    private void setRoot(HBox root) {
        if (scene != null) {
            scene.setRoot(root);
        } else {
            scene = new Scene(root);
            scene.setFill(javafx.scene.paint.Color.WHITE);
            primaryStage.setScene(scene);
        }
    }

    // NEW METHOD: Show server connection screen first
    public void showServerConnection() {
        setRoot(Page_ServerConnection.createConnectionRoot(this));
    }

    public void showRoleSelection() {
        setRoot(Page_RoleSelection.createRoleSelectionRoot(this));
    }

    public void showLogin(String role) {
        this.selectedRole = role;
        setRoot(Page_Login.createLoginRoot(this));
    }

    public void showHome(String userId) {
        this.userId = userId;
        if (this.selectedRole == null) {
            this.selectedRole = "USER";
        }

        // Redirect based on user type
        if (userType != null) {
            switch(userType) {
                case "SUPERADMIN":
                    setRoot(Page_Home.createPage(this)); // Full access
                    break;
                case "ADMIN":
                    setRoot(Page_Projects.createPage(this)); // Start at Projects
                    break;
                case "USER":
                    setRoot(Page_Projects.createPage(this)); // Read-only Projects
                    break;
                default:
                    setRoot(Page_Home.createPage(this));
            }
        } else {
            setRoot(Page_Home.createPage(this));
        }
    }

    // --- NAVIGATION METHODS WITH ACCESS CONTROL ---

    public void showAccounts() {
        // Only SUPERADMIN can access accounts
        if (isSuperAdmin()) {
            setRoot(Page_Accounts.createPage(this));
        } else {
            showAccessDenied();
        }
    }

    public void showAccount() {
        // Only SUPERADMIN can access individual accounts
        if (isSuperAdmin()) {
            setRoot(Page_Account.createPage(this));
        } else {
            showAccessDenied();
        }
    }

    public void showAccountsView() {
        // Only SUPERADMIN can view account details
        if (isSuperAdmin()) {
            setRoot(Page_AccountsView.createPage(this));
        } else {
            showAccessDenied();
        }
    }

    public void showProjects() {
        // All user types can view projects
        setRoot(Page_Projects.createPage(this));
    }

    public void showProjectBlocks(int subdivisionId) {
        // All user types can view blocks
        setRoot(Page_ProjectsViewBlocks.createPage(this, subdivisionId));
    }

    public void showProjectLots(int blockId) {
        // All user types can view lots
        setRoot(Page_ProjectsViewLots.createPage(this, blockId));
    }

    public void showWarehouses() {
        // Only SUPERADMIN and ADMIN can access warehouses
        if (isSuperAdmin() || isAdmin()) {
            setRoot(Page_Warehouses.createPage(this));
        } else {
            showAccessDenied();
        }
    }

    public void showHistory() {
        // Only SUPERADMIN can access history
        if (isSuperAdmin()) {
            setRoot(Page_History.createPage(this));
        } else {
            showAccessDenied();
        }
    }

    // --- ACCESS CONTROL HELPERS ---

    public boolean isSuperAdmin() {
        return "SUPERADMIN".equals(userType);
    }

    public boolean isAdmin() {
        return "ADMIN".equals(userType);
    }

    public boolean isUser() {
        return "USER".equals(userType);
    }

    public boolean canEdit() {
        // Only SUPERADMIN and ADMIN can edit
        return isSuperAdmin() || isAdmin();
    }

    public boolean canDelete() {
        // Only SUPERADMIN can delete
        return isSuperAdmin();
    }

    private void showAccessDenied() {
        setRoot(Page_AccessDenied.createPage(this));
    }

    // --- GETTERS AND SETTERS ---

    public void showWindow() {
        primaryStage.show();
    }

    public Stage getStage() {
        return primaryStage;
    }

    public String getUserId() {
        return userId;
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}