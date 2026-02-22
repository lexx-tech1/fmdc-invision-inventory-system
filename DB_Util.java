import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;

public class DB_Util {

    /**
     * DO NOT CHANGE METHOD NAME
     * Your Page_Login already calls this.
     *
     * @return user primary key if valid, -1 if invalid
     */
    public static int getConnection(String userId, String password) {

        String sql = "SELECT user_id, password_hash FROM users WHERE user_id = ?";

        try (Connection con = (Connection) DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String inputHash = hashPassword(password);

                if (storedHash != null && storedHash.equals(inputHash)) {
                    // ✅ LOGIN SUCCESS: update status
                    setUserStatus(userId, "Online");
                    return 1; // success flag
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // ❌ LOGIN FAILED
    }

    // Password hashing (SHA-256)
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("Password hashing error", e);
        }
    }

    // Update user status
    public static void setUserStatus(String userId, String status) {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        try (Connection con = (Connection) DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
