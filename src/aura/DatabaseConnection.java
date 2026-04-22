package aura;

import java.sql.*;

/**
 * AURA – Database Connection
 * Connects to XAMPP MySQL (localhost:3306, database: aura_db)
 */
public class DatabaseConnection {

    private static final String URL  = "jdbc:mysql://localhost:3306/aura_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Manila";
    private static final String USER = "root";
    private static final String PASS = "";   // Default XAMPP has no root password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("[AURA] MySQL driver not found – add mysql-connector-j to project libraries.");
        }
    }

    /** Open and return a new connection. Caller must close it. */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /** Quick ping – used at startup to warn the user if DB is down. */
    public static boolean testConnection() {
        try (Connection c = getConnection()) {
            return c != null && !c.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
