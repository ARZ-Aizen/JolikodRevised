package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    public int login(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean createUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFood(int id) {
        String sql = "DELETE FROM foods WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addFood(String category, String name, double price, String imagePath) {
        String sql = "INSERT INTO foods (category, name, price, image_path) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category);
            pstmt.setString(2, name);
            pstmt.setDouble(3, price);
            pstmt.setString(4, imagePath);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getItemPrice(String itemName) {
        String sql = "SELECT price FROM foods WHERE name LIKE ?";

        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

        try (Connection conn = DatabaseHelper.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new Object[]{rs.getInt("id"), rs.getString("username"), rs.getString("password")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<Object[]> getAllFoods() {
        List<Object[]> foods = new ArrayList<>();
        String sql = "SELECT id, category, name, price, image_path FROM foods";

        try (Connection conn = DatabaseHelper.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                foods.add(new Object[]{rs.getInt("id"), rs.getString("category"), rs.getString("name"), "₱" + rs.getDouble("price"), rs.getString("image_path")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }

    public boolean updateFoodPrice(int id, double newPrice) {
        String sql = "UPDATE foods SET price = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> getAllReceipts() {
        List<Object[]> list = new java.util.ArrayList<>();
        String query = "SELECT id, place, contactNum, email FROM receipt";

        try (Connection conn = DatabaseHelper.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new Object[]{rs.getInt("id"), rs.getString("place"), rs.getString("contactNum"), rs.getString("email")});
            }
        } catch (SQLException e) {
            System.out.println("Error fetching receipts: " + e.getMessage());
        }
        return list;
    }

    public boolean updateReceiptHeader(int id, String place, String contact, String email) {
        String sql = "UPDATE receipt SET place = ?, contactNum = ?, email = ? WHERE id = ?";

        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, place);
            pstmt.setString(2, contact);
            pstmt.setString(3, email);
            pstmt.setInt(4, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
            return false;
        }
    }

    public List<FoodItem> getMenuData() {
        List<FoodItem> menu = new ArrayList<>();
        String sql = "SELECT name, category, price, image_path FROM foods";

        try (Connection conn = DatabaseHelper.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                menu.add(new FoodItem(rs.getString("name"), rs.getString("category"), rs.getDouble("price"), rs.getString("image_path")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menu;
    }

    public static class FoodItem {
        public String name, category, imagePath;
        public double price;

        public FoodItem(String name, String category, double price, String imagePath) {
            this.name = name;
            this.category = category;
            this.price = price;
            this.imagePath = imagePath;
        }
    }
}