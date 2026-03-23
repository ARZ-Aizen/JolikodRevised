package UI;

import Database.DataManager;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class AdminDashboard {

    private JPanel admin;
    private JButton logoutButton;
    private JButton createUsersButton;
    private JButton createFoodButton;
    private JButton transactionHistoryButton;
    private JButton editReceiptButton;
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
    private JScrollPane FoodScroll;
    private JScrollPane UserScroll;
    private JPanel card4;
    private JTable receiptTable;
    private JButton editButton1;
    private JButton removeItemButton;
    private JButton addItemButton;
    private JTable tableHistory;
    private JScrollPane historyScroll;
    private JScrollPane salesScroll;
    private JPanel historyPanel;
    private JPanel historyNamePanel;
    private JPanel HistoryMain;
    private JPanel salesPanel;
    private JPanel salesLabelPanel;
    private JPanel salesMain;
    private JLabel AdminLabel;
    private JFrame frame;
    private JLabel foodImagePreview;
    private DataManager dataManager;
    private JLabel totalSalesValue;
    private JLabel totalOrdersValue;
    private JLabel lifetimeSalesValue;
    private JLabel avgOrderValue;

    // Defined Colors
    private final Color ACTIVE_COLOR = Color.decode("#FAD041");
    private final Color DEFAULT_COLOR = Color.WHITE;

    private void styleTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Inter", Font.PLAIN, 18));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        table.setRowHeight(30);
        table.setFont(new Font("Inter", Font.PLAIN, 16));
        table.setGridColor(new Color(230, 230, 230));
    }

    /**
     * Resets all sidebar buttons to default and highlights the selected one.
     */
    private void handleButtonColor(JButton activeBtn) {
        // List of all sidebar buttons to reset
        JButton[] sideButtons = {createUsersButton, editReceiptButton, createFoodButton, transactionHistoryButton};

        for (JButton btn : sideButtons) {
            if (btn != null) {
                btn.setBackground(DEFAULT_COLOR);
                btn.setFocusPainted(false); // Keeps UI clean
            }
        }
        activeBtn.setBackground(ACTIVE_COLOR);
    }

    private void deleteUserFromDatabase(int id) {
        if (dataManager.deleteUser(id)) {
            JOptionPane.showMessageDialog(frame, "User deleted successfully!");
            loadUsersToTable();
        } else {
            JOptionPane.showMessageDialog(frame, "Error deleting user.");
        }
    }

    private void updateFoodPriceInDatabase(int id, double newPrice) {
        if (dataManager.updateFoodPrice(id, newPrice)) {
            JOptionPane.showMessageDialog(frame, "Price updated successfully!");
            loadFoodToTable();
        } else {
            JOptionPane.showMessageDialog(frame, "Database error while updating price.");
        }
    }

    private void displayImage(JLabel label, String path) {
        if (label == null) return;
        try {
            ImageIcon icon = new ImageIcon(path);
            java.awt.Image img = icon.getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
            label.setText("");
        } catch (Exception e) {
            label.setIcon(null);
            label.setText("Image not found");
        }
    }

    public void loadUsersToTable() {
        String[] columns = {"ID", "Username", "Password"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        List<Object[]> users = dataManager.getAllUsers();
        for (Object[] user : users) { model.addRow(user); }

        userTable.setModel(model);
        styleTable(userTable);
        userTable.getColumnModel().getColumn(0).setMinWidth(0);
        userTable.getColumnModel().getColumn(0).setMaxWidth(0);
        userTable.getColumnModel().getColumn(0).setWidth(0);
    }

    public void loadReceiptToTable() {
        String[] columns = {"ID", "Branch Name", "Contact Number", "Email Address"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        List<Object[]> receipts = dataManager.getAllReceipts();
        for (Object[] receipt : receipts) { model.addRow(receipt); }

        receiptTable.setModel(model);
        styleTable(receiptTable);
        receiptTable.getColumnModel().getColumn(0).setMinWidth(0);
        receiptTable.getColumnModel().getColumn(0).setMaxWidth(0);
        receiptTable.getColumnModel().getColumn(0).setWidth(0);
    }

    public void loadFoodToTable() {
        String[] columns = {"ID", "Category", "Name", "Price", "Image Path"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        List<Object[]> foods = dataManager.getAllFoods();
        for (Object[] food : foods) { model.addRow(food); }

        foodTable.setModel(model);
        styleTable(foodTable);
        foodTable.getColumnModel().getColumn(0).setMinWidth(0);
        foodTable.getColumnModel().getColumn(0).setMaxWidth(0);
        foodTable.getColumnModel().getColumn(4).setMinWidth(0);
        foodTable.getColumnModel().getColumn(4).setMaxWidth(0);
    }

    private void deleteFoodFromDatabase(int id) {
        if (dataManager.deleteFood(id)) {
            JOptionPane.showMessageDialog(frame, "Item removed from menu.");
            loadFoodToTable();
        } else {
            JOptionPane.showMessageDialog(frame, "Error: Could not delete the food item.");
        }
    }

    public void loadTransactionHistory() {
        String[] columns = {"Date", "Total Price", "Cashier", "Amount Paid", "Change", "Receipt Path"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        List<Object[]> history = dataManager.getAllTransactions();
        for (Object[] row : history) { model.addRow(row); }

        tableHistory.setModel(model);
        styleTable(tableHistory);
        if (tableHistory.getColumnCount() > 5) {
            tableHistory.getColumnModel().getColumn(5).setMinWidth(0);
            tableHistory.getColumnModel().getColumn(5).setMaxWidth(0);
            tableHistory.getColumnModel().getColumn(5).setWidth(0);
        }
    }

    public void setupSalesCards() {
        salesPanel.removeAll();
        salesPanel.setLayout(new GridLayout(1, 4, 15, 0));
        salesPanel.setBackground(new Color(245, 245, 245));

        JPanel dailyCard = createStatCard("TODAY'S SALES", Color.decode("#e67e22"));
        totalSalesValue = (JLabel) dailyCard.getClientProperty("valueLabel");
        JPanel lifeCard = createStatCard("LIFETIME SALES", Color.decode("#27ae60"));
        lifetimeSalesValue = (JLabel) lifeCard.getClientProperty("valueLabel");
        JPanel ordersCard = createStatCard("TODAY'S ORDERS", Color.decode("#2980b9"));
        totalOrdersValue = (JLabel) ordersCard.getClientProperty("valueLabel");
        JPanel avgCard = createStatCard("AVG. ORDER", Color.decode("#8e44ad"));
        avgOrderValue = (JLabel) avgCard.getClientProperty("valueLabel");

        salesPanel.add(dailyCard);
        salesPanel.add(lifeCard);
        salesPanel.add(ordersCard);
        salesPanel.add(avgCard);
        salesPanel.revalidate();
        salesPanel.repaint();
    }

    private JPanel createStatCard(String title, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        titleLabel.setForeground(Color.GRAY);
        JLabel valueLabel = new JLabel("₱ 0.00");
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        valueLabel.setForeground(accentColor);
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.putClientProperty("valueLabel", valueLabel);
        return card;
    }

    public void refreshSalesSummary() {
        double todayTotal = dataManager.getTodaysTotalSales();
        double lifeTotal = dataManager.getLifetimeTotalSales();
        int todayCount = dataManager.getTodaysOrderCount();
        double average = (todayCount > 0) ? (todayTotal / todayCount) : 0.0;

        totalSalesValue.setText("₱ " + String.format("%,.2f", todayTotal));
        lifetimeSalesValue.setText("₱ " + String.format("%,.2f", lifeTotal));
        totalOrdersValue.setText(String.valueOf(todayCount));
        avgOrderValue.setText("₱ " + String.format("%,.2f", average));
    }

    public static Font loadCustomFont(float size) {
        try {
            InputStream is = AdminDashboard.class.getResourceAsStream("/logo.otf");
            if (is == null) return new Font("SansSerif", Font.PLAIN, (int) size);
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(size);
        } catch (Exception e) {
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }

    public AdminDashboard() {
        frame = new JFrame("Jolikod - Admin Dashboard");
        frame.setContentPane(this.admin);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        dataManager = new DataManager();

        URL iconURL = getClass().getResource("/iconImage.png");
        if (iconURL != null) {
            ImageIcon logo = new ImageIcon(iconURL);
            frame.setIconImage(logo.getImage());
        }

        createUsersButton.addActionListener(e -> {
            cl.show(contentPanel, "panel1");
            handleButtonColor(createUsersButton);
            loadUsersToTable();
        });

        editReceiptButton.addActionListener(e -> {
            cl.show(contentPanel, "panel4");
            handleButtonColor(editReceiptButton);
            loadReceiptToTable();
        });

        createFoodButton.addActionListener(e -> {
            cl.show(contentPanel, "panel2");
            handleButtonColor(createFoodButton);
            loadFoodToTable();
        });

        transactionHistoryButton.addActionListener(e -> {
            cl.show(contentPanel, "panel3");
            handleButtonColor(transactionHistoryButton);
            if (totalSalesValue == null) setupSalesCards();
            loadTransactionHistory();
            refreshSalesSummary();
        });

        logoutButton.addActionListener(e -> {
            new LoginUserInterface();
            frame.dispose();
        });

        refreshButton.addActionListener(e -> loadUsersToTable());

        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                int userId = Integer.parseInt(userTable.getValueAt(selectedRow, 0).toString());
                if (userId == 1) {
                    JOptionPane.showMessageDialog(frame, "System Error: The Admin account cannot be deleted.", "Access Denied", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (JOptionPane.showConfirmDialog(frame, "Delete user?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    deleteUserFromDatabase(userId);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a user.");
            }
        });

        createUserButton.addActionListener(e -> new CreateAccount(AdminDashboard.this));

        editButton.addActionListener(e -> {
            int selectedRow = foodTable.getSelectedRow();
            if (selectedRow != -1) {
                String foodName = foodTable.getValueAt(selectedRow, 2).toString();
                String cleanPrice = foodTable.getValueAt(selectedRow, 3).toString().replace("₱", "").trim();
                String input = JOptionPane.showInputDialog(frame, "Edit Price for: " + foodName, cleanPrice);
                if (input != null) {
                    try {
                        updateFoodPriceInDatabase(Integer.parseInt(foodTable.getValueAt(selectedRow, 0).toString()), Double.parseDouble(input));
                    } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Invalid price."); }
                }
            }
        });

        editButton1.addActionListener(e -> {
            int selectedRow = receiptTable.getSelectedRow();
            if (selectedRow != -1) {
                JTextField p = new JTextField(receiptTable.getValueAt(selectedRow, 1).toString());
                JTextField c = new JTextField(receiptTable.getValueAt(selectedRow, 2).toString());
                JTextField m = new JTextField(receiptTable.getValueAt(selectedRow, 3).toString());
                Object[] msg = {"Branch:", p, "Contact:", c, "Email:", m};
                if (JOptionPane.showConfirmDialog(frame, msg, "Edit Receipt", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    dataManager.updateReceiptHeader(Integer.parseInt(receiptTable.getValueAt(selectedRow, 0).toString()), p.getText(), c.getText(), m.getText());
                    loadReceiptToTable();
                }
            }
        });

        addItemButton.addActionListener(e -> new CreateFood(AdminDashboard.this));

        removeItemButton.addActionListener(e -> {
            int selectedRow = foodTable.getSelectedRow();
            if (selectedRow != -1) {
                if (JOptionPane.showConfirmDialog(frame, "Remove item?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    deleteFoodFromDatabase(Integer.parseInt(foodTable.getValueAt(selectedRow, 0).toString()));
                }
            }
        });

        tableHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = tableHistory.getSelectedRow();
                    if (row != -1) {
                        try { Desktop.getDesktop().open(new File(tableHistory.getValueAt(row, 5).toString())); } catch (Exception ex) { ex.printStackTrace(); }
                    }
                }
            }
        });

        foodTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = foodTable.getSelectedRow();
                if (row != -1) displayImage(foodImagePreview, foodTable.getValueAt(row, 4).toString());
            }
        });

        frame.setVisible(true);
    }

    private void createUIComponents() {
        AdminLabel = new JLabel();
        AdminLabel.setFont(loadCustomFont(42f));
        AdminLabel.setForeground(Color.decode("#FAD041"));
    }
}