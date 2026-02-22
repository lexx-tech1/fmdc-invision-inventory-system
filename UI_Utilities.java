import javafx.scene.paint.Color;

public class UI_Utilities {
    // Original Colors
    public static final Color WHITE = Color.web("#FFFFFF");
    public static final Color DARK_GREEN = Color.web("#1a5c1a");
    public static final Color LIGHT_GREEN = Color.web("#2d7a2d");
    public static final Color RED = Color.web("#FF0000");
    // --- ADDED: Black color constant ---
    public static final Color BLACK = Color.web("#000000");

    // Window configuration
    public static final boolean FULLSCREEN = true;
    public static final double SIDEBAR_WIDTH = 280;

    // Icon Sizing
    public static final double ICON_SIZE_MENU = 20.0;

    // --- CHANGE: Increased User Icon Size ---
    public static final double USER_ICON_SIZE = 60.0;

    // Base path constant (CRITICAL: Ensure this path is correct on your OS)
    private static final String BASE_ICON_PATH = "file:///C://Users//adria//Downloads//FMDCINVision-20251217T172442Z-1-001//FMDCINVision//icons/";

    // File paths - Logo and Icons
    public static final String LOGO_PATH = BASE_ICON_PATH + "Logo_FMDC.png";

    // Main Menu Icons
    public static final String HOME_SELECTED_PATH = BASE_ICON_PATH + "Icon_HomeSelected.png";
    public static final String HOME_UNSELECTED_PATH = BASE_ICON_PATH + "Icon_HomeUnselected.png";

    public static final String ACCOUNTS_SELECTED_PATH = BASE_ICON_PATH + "Icon_AccountsSelected.png";
    public static final String ACCOUNTS_UNSELECTED_PATH = BASE_ICON_PATH + "Icon_AccountsUnselected.png";

    public static final String PROJECTS_SELECTED_PATH = BASE_ICON_PATH + "Icon_ProjectsSelected.png";
    public static final String PROJECTS_UNSELECTED_PATH = BASE_ICON_PATH + "Icon_ProjectsUnselected.png";

    public static final String WAREHOUSES_SELECTED_PATH = BASE_ICON_PATH + "Icon_InventorySelected.png";
    public static final String WAREHOUSES_UNSELECTED_PATH = BASE_ICON_PATH + "Icon_InventoryUnselected.png";

    public static final String HISTORY_SELECTED_PATH = BASE_ICON_PATH + "Icon_HistorySelected.png";
    public static final String HISTORY_UNSELECTED_PATH = BASE_ICON_PATH + "Icon_HistoryUnselected.png";

    // Bottom Menu Icons
    public static final String LOGOUT_PATH = BASE_ICON_PATH + "Icon_Logout.png";
    public static final String EXIT_PATH = BASE_ICON_PATH + "Icon_Exit.png";

    // Header Icon (Used for User ID field in login)
    public static final String USER_ICON_PATH = BASE_ICON_PATH + "Icon_User.png";

    // FIX: Updated Password Icon Path
    public static final String PASSWORD_ICON_PATH = BASE_ICON_PATH + "Icon_Password.png";

    // Font configuration
    public static final String FONT_FAMILY = "Arial";
    public static final int FONT_SIZE_LARGE = 28;
    // --- ADDED: Font size 24 as EXTRA_MEDIUM ---
    public static final int FONT_SIZE_EXTRA_MEDIUM = 24;
    public static final int FONT_SIZE_MEDIUM = 22;
    // --- ADDED: FONT_SIZE_SMALL to fix compilation error ---
    public static final int FONT_SIZE_SMALL = 16;
    public static final int FONT_SIZE_ERROR = 20;
    public static final int FONT_SIZE_EXTRA_SMALL = 18;

    public static String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }
}