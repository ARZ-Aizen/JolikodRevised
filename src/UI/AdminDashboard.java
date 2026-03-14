package UI;

import Database.DataManager;
import java.awt.CardLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
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
    private JButton editReceiptButton;
    private JPanel card4;
    private JTable receiptTable;
    private JButton editButton1;
    private JFrame frame;
    private DataManager dataManager;

    //

    private void deleteUserFromDatabase(int id) {
        if (dataManager.deleteUser(id)) {
            JOptionPane.showMessageDialog(frame, "User deleted successfully!");
            loadUsersToTable();
        } else {
            JOptionPane.showMessageDialog(frame, "Error deleting user.");
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
        String[] columns = {"ID", "Category", "Name", "Price"};
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
        foodTable.getColumnModel().getColumn(0).setWidth(0);
    }

    private void updateFoodPriceInDatabase(int id, double newPrice) {
        if (dataManager.updateFoodPrice(id, newPrice)) {
            JOptionPane.showMessageDialog(frame, "Price updated successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "Database error while updating price.");
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

                    String cleanCurrentPrice = currentPrice.replace("₱", "").trim();

                    String newPriceInput = JOptionPane.showInputDialog(frame,
                            "Editing Price for: " + foodName,
                            cleanCurrentPrice);

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

                    Object[] message = {
                            "Branch Name:", placeField,
                            "Contact Number:", contactField,
                            "Email Address:", emailField
                    };

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

        frame.setVisible(true);
    }
}