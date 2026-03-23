package UI;

import Database.DataManager;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard {

    private JPanel admin;
    private JButton logoutButton; private JButton createUsersButton; private JButton createFoodButton; private JButton transactionHistoryButton;
    private JPanel contentPanel;
    private JPanel LeftPanel; private JPanel card1; private JPanel card2; private JPanel card3;
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
    private JButton editReceiptButton;
    private JPanel card4;
    private JTable receiptTable;
    private JButton editButton1;
    private JButton removeItemButton;
    private JButton addItemButton;
    private JTable tableHistory;
    private JScrollPane historyScroll; private JScrollPane salesScroll;
    private JPanel historyPanel; private JPanel historyNamePanel; private JPanel HistoryMain;
    private JPanel salesPanel; private JPanel salesLabelPanel; private JPanel salesMain;
    private JLabel AdminLabel;
    private JFrame frame;
    private JLabel foodImagePreview;
    private DataManager dataManager;
    private JLabel totalSalesValue; private JLabel totalOrdersValue;
    private JLabel lifetimeSalesValue; private JLabel avgOrderValue;

    //

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
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Object[]> users = dataManager.getAllUsers();
        for (Object[] user : users) {
            model.addRow(user);
        }

        userTable.setModel(model);
        userTable.getColumnModel().getColumn(0).setMinWidth(0);
        userTable.getColumnModel().getColumn(0).setMaxWidth(0);
        userTable.getColumnModel().getColumn(0).setWidth(0);
    }

    public void loadReceiptToTable() {
        String[] columns = {"ID", "Branch Name", "Contact Number", "Email Address"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        List<Object[]> receipts = dataManager.getAllReceipts();
        for (Object[] receipt : receipts) {
            model.addRow(receipt);
        }

        receiptTable.setModel(model);

        receiptTable.getColumnModel().getColumn(0).setMinWidth(0);
        receiptTable.getColumnModel().getColumn(0).setMaxWidth(0);
        receiptTable.getColumnModel().getColumn(0).setWidth(0);
    }

    public void loadFoodToTable() {
        String[] columns = {"ID", "Category", "Name", "Price", "Image Path"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Object[]> foods = dataManager.getAllFoods();
        for (Object[] food : foods) {
            model.addRow(food);
        }

        foodTable.setModel(model);

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
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setRowCount(0);

        List<Object[]> history = dataManager.getAllTransactions();
        for (Object[] row : history) {
            model.addRow(row);
        }

        tableHistory.setModel(model);

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

        JPanel dailyCard = createStatCard("TODAY'S SALES", Color.decode("#e67e22")); // Orange
        totalSalesValue = (JLabel) dailyCard.getClientProperty("valueLabel");

        JPanel lifeCard = createStatCard("LIFETIME SALES", Color.decode("#27ae60")); // Green
        lifetimeSalesValue = (JLabel) lifeCard.getClientProperty("valueLabel");

        JPanel ordersCard = createStatCard("TODAY'S ORDERS", Color.decode("#2980b9")); // Blue
        totalOrdersValue = (JLabel) ordersCard.getClientProperty("valueLabel");

        JPanel avgCard = createStatCard("AVG. ORDER", Color.decode("#8e44ad")); // Purple
        avgOrderValue = (JLabel) avgCard.getClientProperty("valueLabel");

        salesPanel.add(dailyCard);
        salesPanel.add(lifeCard);
        salesPanel.add(ordersCard);
        salesPanel.add(avgCard);

        salesPanel.revalidate();
        salesPanel.repaint();
    }

    private JPanel createStatCard(String title, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
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

    //

    public static Font loadCustomFont(float size) {
        try {
            InputStream is = AdminDashboard.class.getResourceAsStream("/logo.otf");

            if (is == null) {
                System.err.println("Could not find logo.otf in resources folder!");
                return new Font("SansSerif", Font.PLAIN, (int)size);
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, (int)size);
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
                if (totalSalesValue == null) {
                    setupSalesCards();
                }

                loadTransactionHistory();
                refreshSalesSummary();
            }
        });

        editReceiptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(contentPanel, "panel4");
                loadReceiptToTable();
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

                    if (userId == 1) {
                        JOptionPane.showMessageDialog(frame, "System Error: The Admin account cannot be deleted.", "Access Denied", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

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

                    String cleanCurrentPrice = currentPrice.replace("₱", "").trim();

                    String newPriceInput = JOptionPane.showInputDialog(frame, "Editing Price for: " + foodName, cleanCurrentPrice);

                    if (newPriceInput != null && !newPriceInput.trim().isEmpty()) {
                        try {
                            double newPrice = Double.parseDouble(newPriceInput.replaceAll("[^0-9.]", ""));
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

        editButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = receiptTable.getSelectedRow();

                if (selectedRow != -1) {
                    int receiptId = Integer.parseInt(receiptTable.getValueAt(selectedRow, 0).toString());
                    String currentPlace = receiptTable.getValueAt(selectedRow, 1).toString();
                    String currentContact = receiptTable.getValueAt(selectedRow, 2).toString();
                    String currentEmail = receiptTable.getValueAt(selectedRow, 3).toString();

                    JTextField placeField = new JTextField(currentPlace);
                    JTextField contactField = new JTextField(currentContact);
                    JTextField emailField = new JTextField(currentEmail);

                    Object[] message = {"Branch Name:", placeField, "Contact Number:", contactField, "Email Address:", emailField};

                    int option = JOptionPane.showConfirmDialog(frame, message, "Edit Receipt Header", JOptionPane.OK_CANCEL_OPTION);

                    if (option == JOptionPane.OK_OPTION) {
                        String newPlace = placeField.getText().trim();
                        String newContact = contactField.getText().trim();
                        String newEmail = emailField.getText().trim();

                        if (!newPlace.isEmpty() && !newContact.isEmpty() && !newEmail.isEmpty()) {
                            if (dataManager.updateReceiptHeader(receiptId, newPlace, newContact, newEmail)) {
                                JOptionPane.showMessageDialog(frame, "Receipt info updated successfully!");
                                loadReceiptToTable();
                            } else {
                                JOptionPane.showMessageDialog(frame, "Error: Could not update database. Ensure details are unique.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "All fields must be filled out.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a branch from the table first.");
                }
            }
        });

        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateFood(AdminDashboard.this);
            }
        });

        removeItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = foodTable.getSelectedRow();

                if (selectedRow != -1) {
                    Object idValue = foodTable.getValueAt(selectedRow, 0);
                    int foodId = Integer.parseInt(idValue.toString());
                    String foodName = foodTable.getValueAt(selectedRow, 2).toString();

                    int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to remove '" + foodName + "' from the menu?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteFoodFromDatabase(foodId);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a food item from the table first.");
                }
            }
        });

        tableHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = tableHistory.getSelectedRow();
                    if (row != -1) {
                        String path = tableHistory.getValueAt(row, 5).toString();

                        try {
                            File pdfFile = new File(path);
                            if (pdfFile.exists()) {
                                if (Desktop.isDesktopSupported()) {
                                    Desktop.getDesktop().open(pdfFile);
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Desktop not supported");
                                }
                            } else {
                                JOptionPane.showMessageDialog(frame, "File not found: " + path);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Error opening PDF: " + ex.getMessage());
                        }
                    }
                }
            }
        });

        foodTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = foodTable.getSelectedRow();
                if (selectedRow != -1) {
                    Object pathObj = foodTable.getValueAt(selectedRow, 4);
                    if (pathObj != null) {
                        String path = pathObj.toString();
                        displayImage(foodImagePreview, path);
                    }
                }
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