package UI;

import java.awt.CardLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard {

    private JPanel admin;
    private JButton logoutButton;
    private JButton createUsersButton;
    private JButton createFoodButton;
    private JButton transactionHistoryButton;
    private JPanel contentPanel;
    private JPanel LeftPanel;
    private JPanel card1;
    private JPanel card2;
    private JPanel card3;
    private JButton createUserButton;
    private JPanel btnCreateUser;
    private JTable userTable;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton editButton;
    private JButton loadFoodToTable;
    private JPanel OrderPanel;
    private JTable foodTable;
    private JTable HistoryTable;
    private JScrollPane HisortyScroll;
    private JScrollPane FoodScroll;
    private JScrollPane UserScroll;
    private JFrame frame;

    //

    private void deleteUserFromDatabase(int id) {
        String url = "jdbc:sqlite:src/Database/jolikod.db";
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(frame, "User deleted successfully!");
                loadUsersToTable();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error deleting user: " + ex.getMessage());
        }
    }

    public void loadUsersToTable() {
        String[] columns = {"ID", "Username", "Password"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String url = "jdbc:sqlite:src/Database/jolikod.db";
        String sql = "SELECT id, username, password FROM users";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                });
            }
            userTable.setModel(model);
            userTable.getColumnModel().getColumn(0).setMinWidth(0);
            userTable.getColumnModel().getColumn(0).setMaxWidth(0);
            userTable.getColumnModel().getColumn(0).setWidth(0);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        }
    }

    public void loadFoodToTable() {
        String[] columns = {"ID", "Category", "Name", "Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String url = "jdbc:sqlite:src/Database/jolikod.db";
        String sql = "SELECT id, category, name, price FROM foods";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getString("name"),
                        "₱" + rs.getDouble("price")
                });
            }
            foodTable.setModel(model);
            foodTable.getColumnModel().getColumn(0).setMinWidth(0);
            foodTable.getColumnModel().getColumn(0).setMaxWidth(0);
            foodTable.getColumnModel().getColumn(0).setWidth(0);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        }
    }

    private void updateFoodPriceInDatabase(int id, double newPrice) {
        String url = "jdbc:sqlite:src/Database/jolikod.db";
        String sql = "UPDATE foods SET price = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(frame, "Price updated successfully!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
        }
    }

    //

    public AdminDashboard() {
        frame = new JFrame("Jolikod - Admin Dashboard");
        frame.setContentPane(this.admin);
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        CardLayout cl = (CardLayout) contentPanel.getLayout();

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginUserInterface();
                frame.dispose();
            }
        });

        createUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(contentPanel, "panel1");
                loadUsersToTable();
            }
        });

        createFoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(contentPanel, "panel2");
                loadFoodToTable();
            }
        });

        transactionHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(contentPanel, "panel3");
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUsersToTable();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();

                if (selectedRow != -1) {
                    Object idValue = userTable.getValueAt(selectedRow, 0);
                    int userId = Integer.parseInt(idValue.toString());
                    int confirm = JOptionPane.showConfirmDialog(frame,
                            "Are you sure you want to delete this user:?",
                            "Confirm Delete", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteUserFromDatabase(userId);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a user from the table first.");
                }
            }
        });

        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateAccount(AdminDashboard.this);
            }
        });

        loadFoodToTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFoodToTable();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = foodTable.getSelectedRow();

                if (selectedRow != -1) {
                    Object idValue = foodTable.getValueAt(selectedRow, 0);
                    String foodName = foodTable.getValueAt(selectedRow, 2).toString();
                    String currentPrice = foodTable.getValueAt(selectedRow, 3).toString();

                    String newPriceInput = JOptionPane.showInputDialog(frame,
                            "Editing Price for: " + foodName,
                            currentPrice);

                    if (newPriceInput != null && !newPriceInput.trim().isEmpty()) {
                        try {
                            double newPrice = Double.parseDouble(newPriceInput);
                            int foodId = Integer.parseInt(idValue.toString());
                            updateFoodPriceInDatabase(foodId, newPrice);
                            loadFoodToTable();

                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Please enter a valid number (e.g., 150.50)");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a food item from the table first.");
                }
            }
        });

        frame.setVisible(true);
    }
}