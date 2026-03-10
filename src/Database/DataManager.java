package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    public boolean login(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getItemPrice(String itemName) {
        String sql = "SELECT price FROM foods WHERE name LIKE ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + itemName.trim() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1.0;
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> getAllUsers() {
        List<Object[]> users = new ArrayList<>();
        String sql = "SELECT id, username, password FROM users";

        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<Object[]> getAllFoods() {
        List<Object[]> foods = new ArrayList<>();
        String sql = "SELECT id, category, name, price FROM foods";

        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                foods.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getString("name"),
                        "₱" + rs.getDouble("price")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }

    public boolean updateFoodPrice(int id, double newPrice) {
        String sql = "UPDATE foods SET price = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}