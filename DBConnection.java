import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static String DATABASE_NAME = "fmdcinvision_db"; // Change to your database name

    // Configure the connection based on user type and server IP
    public static void configure(String userType, String serverIP) {
        // Build the database URL
        // For XAMPP, default port is 3306
        DB_URL = "jdbc:mysql://" + serverIP + ":3306/" + DATABASE_NAME +
                "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

        // Set credentials based on user type
        switch(userType) {
            case "SUPERADMIN":
                DB_USER = "superadmin";
                DB_PASSWORD = "super123"; // Change to your password
                break;

            case "ADMIN":
                DB_USER = "admin";
                DB_PASSWORD = "admin123"; // Change to your password
                break;

            case "USER":
                DB_USER = "user";
                DB_PASSWORD = "user123"; // Change to your password
                break;

            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }

        System.out.println("Configured database connection:");
        System.out.println("  URL: " + DB_URL);
        System.out.println("  User: " + DB_USER);
    }

    // Get a database connection
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Return the connection
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-java to your project.", e);
        }
    }

    // Test the connection
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get current database name
    public static String getDatabaseName() {
        return DATABASE_NAME;
    }

    // Get current user
    public static String getCurrentUser() {
        return DB_USER;
    }
}