package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:src/Database/jolikod.db";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}