package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.text.NumberFormat;

import Database.DataManager;
import Output.OrderReceipt;
import Database.DataManager.FoodItem;

public class UserDashboard {

    private JPanel userPanel, mainPanel, cardSide, orderSide, panelDish, allDish, mainDish, sideDish, drinkDish, dessertDish, panelButton, payRemove, panelSale, userSubPanel;
    private JButton allButton, mainDishButton, sideDishButton, drinksButton, dessertButton, logoutButton;
    private JTable cartTable;
    private JTextField subTotalField, vatField, totalField, amountField, changeField;
    private JButton payButton, removeButton;
    private JLabel welcomeLabel;
    private JFrame frame;
    private JScrollPane scrollAll, scrollMain, scrollSide, scrollDrink, scrollDessert, cartScroll;
    private DataManager dataManager;

    private final Color ACTIVE_COLOR = Color.decode("#FAD041");
    private final Color DEFAULT_COLOR = new Color(240, 240, 240);

    private void handleMenuSelection(JButton selectedBtn) {
        JButton[] menuButtons = {allButton, mainDishButton, sideDishButton, drinksButton, dessertButton};
        for (JButton btn : menuButtons) {
            if (btn != null) {
                btn.setBackground(DEFAULT_COLOR);
                btn.setFocusPainted(false);
            }
        }
        selectedBtn.setBackground(ACTIVE_COLOR);
    }

    public void ConfigurePositiveSpinner(JSpinner spinner) {
        spinner.setModel(new SpinnerNumberModel(1, 1, 99, 1));

        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setEditable(false);
            textField.setBackground(Color.WHITE);
            textField.setFocusable(false);
            textField.setHorizontalAlignment(JTextField.CENTER);
        }
    }

    public void ConfigureCurrencyField(JTextField textField) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setAllowsInvalid(false);

        if (textField instanceof JFormattedTextField) {
            ((JFormattedTextField) textField).setFormatterFactory(new DefaultFormatterFactory(formatter));
        }
    }

    private JPanel createFoodCard(String name, double price, String imgPath) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(180, 220));

        JSpinner qtySpinner = new JSpinner();
        ConfigurePositiveSpinner(qtySpinner); // Apply the fix here
        qtySpinner.setMaximumSize(new Dimension(80, 30));
        qtySpinner.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel imgLabel = new JLabel();
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            java.net.URL imgURL = getClass().getResource("/" + imgPath);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(img));
                imgLabel.setText("");
            } else {
                imgLabel.setText("[ Missing ]");
            }
        } catch (Exception e) {
            imgLabel.setText("Error");
        }

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("₱" + String.format("%.2f", price));
        priceLabel.setForeground(Color.RED);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addBtn = new JButton("Add");
        addBtn.setBackground(new Color(255, 204, 0));
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBtn.addActionListener(e -> {
            int qty = (int) qtySpinner.getValue();
            addItemToTable(name, qty);
        });

        card.add(Box.createVerticalStrut(10));
        card.add(imgLabel);
        card.add(nameLabel);
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(qtySpinner);
        card.add(Box.createVerticalStrut(5));
        card.add(addBtn);
        card.add(Box.createVerticalStrut(10));

        return card;
    }

    private void loadDynamicMenu() {
        allDish.removeAll();
        mainDish.removeAll();
        sideDish.removeAll();
        drinkDish.removeAll();
        dessertDish.removeAll();

        allDish.setLayout(new java.awt.GridLayout(0, 3, 10, 10));
        mainDish.setLayout(new java.awt.GridLayout(0, 3, 10, 10));
        sideDish.setLayout(new java.awt.GridLayout(0, 3, 10, 10));
        drinkDish.setLayout(new java.awt.GridLayout(0, 3, 10, 10));
        dessertDish.setLayout(new java.awt.GridLayout(0, 3, 10, 10));

        List<FoodItem> items = dataManager.getMenuData();
        for (FoodItem item : items) {
            allDish.add(createFoodCard(item.name, item.price, item.imagePath));
            JPanel catCard = createFoodCard(item.name, item.price, item.imagePath);
            if (item.category.equalsIgnoreCase("Main Dish")) mainDish.add(catCard);
            else if (item.category.equalsIgnoreCase("Side Dish")) sideDish.add(catCard);
            else if (item.category.equalsIgnoreCase("Drinks")) drinkDish.add(catCard);
            else if (item.category.equalsIgnoreCase("Dessert")) dessertDish.add(catCard);
        }

        allDish.revalidate(); mainDish.revalidate(); sideDish.revalidate();
        drinkDish.revalidate(); dessertDish.revalidate();
        panelDish.revalidate(); panelDish.repaint();
    }

    public void addItemToTable(String itemName, int quantity) {
        double price = dataManager.getItemPrice(itemName);
        if (price != -1.0) {
            DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
            boolean itemExists = false;
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).equals(itemName)) {
                    int newQty = (int) model.getValueAt(i, 2) + quantity;
                    model.setValueAt(newQty, i, 2);
                    model.setValueAt(price * newQty, i, 3);
                    itemExists = true;
                    break;
                }
            }
            if (!itemExists) model.addRow(new Object[]{itemName, price, quantity, price * quantity});
            updateCalculations();
        }
    }

    public void updateCalculations() {
        double subtotal = 0;
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            subtotal += Double.parseDouble(model.getValueAt(i, 3).toString());
        }
        double vat = subtotal * 0.12;
        double grandTotal = subtotal + vat;

        if (subTotalField instanceof JFormattedTextField) {
            ((JFormattedTextField) subTotalField).setValue(subtotal);
            ((JFormattedTextField) vatField).setValue(vat);
            ((JFormattedTextField) totalField).setValue(grandTotal);
        } else {
            subTotalField.setText(String.format("%.2f", subtotal));
            vatField.setText(String.format("%.2f", vat));
            totalField.setText(String.format("%.2f", grandTotal));
        }
    }

    public UserDashboard(String userName) {
        frame = new JFrame("Jolikod - User Dashboard");
        frame.setContentPane(this.userPanel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);

        URL iconURL = getClass().getResource("/iconImage.png");
        if (iconURL != null) frame.setIconImage(new ImageIcon(iconURL).getImage());

        dataManager = new DataManager();
        loadDynamicMenu();

        scrollAll.getVerticalScrollBar().setUnitIncrement(20);
        this.welcomeLabel.setText("Welcome, " + userName);
        CardLayout cl = (CardLayout) panelDish.getLayout();

        cartTable.setModel(new DefaultTableModel(new Object[]{"Item Name", "Price", "Qty", "Total"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        });
        cartTable.getTableHeader().setFont(cartTable.getTableHeader().getFont().deriveFont(Font.BOLD));
        cartTable.getTableHeader().setReorderingAllowed(false);

        JTextField[] currencyFields = {subTotalField, vatField, totalField, amountField, changeField};
        for (JTextField f : currencyFields) ConfigureCurrencyField(f);


        allButton.addActionListener(e -> {
            cl.show(panelDish, "Card1");
            handleMenuSelection(allButton);
        });

        mainDishButton.addActionListener(e -> {
            cl.show(panelDish, "Card2");
            handleMenuSelection(mainDishButton);
        });

        sideDishButton.addActionListener(e -> {
            cl.show(panelDish, "Card3");
            handleMenuSelection(sideDishButton);
        });

        drinksButton.addActionListener(e -> {
            cl.show(panelDish, "Card4");
            handleMenuSelection(drinksButton);
        });

        dessertButton.addActionListener(e -> {
            cl.show(panelDish, "Card5");
            handleMenuSelection(dessertButton);
        });

        handleMenuSelection(allButton);

        logoutButton.addActionListener(e -> {
            new LoginUserInterface();
            frame.dispose();
        });

        removeButton.addActionListener(e -> {
            int row = cartTable.getSelectedRow();
            if (row != -1) {
                ((DefaultTableModel) cartTable.getModel()).removeRow(row);
                updateCalculations();
            }
        });

        payButton.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frame, "Your cart is empty. Please add items before paying.", "No Order", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                double total = (totalField instanceof JFormattedTextField) ?
                        ((Number) ((JFormattedTextField) totalField).getValue()).doubleValue() :
                        Double.parseDouble(totalField.getText().replaceAll("[^0-9.]", ""));

                String rawCash = amountField.getText().replaceAll(",", "").trim();

                if (rawCash.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter the cash amount received from the customer.", "Missing Payment", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double cash = Double.parseDouble(rawCash);

                if (cash < total) {
                    JOptionPane.showMessageDialog(frame, "Insufficient cash! The total is ₱" + String.format("%.2f", total) + ".", "Payment Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double change = cash - total;
                changeField.setText(String.format("%,.2f", change));

                String dirPath = System.getProperty("userPanel.dir") + File.separator + "receipts";
                File dir = new File(dirPath);
                if (!dir.exists()) dir.mkdirs();

                String path = dirPath + File.separator + "Receipt_" + System.currentTimeMillis() + ".pdf";

                if (dataManager.saveTransaction(total, userName, cash, change, path)) {

                    JOptionPane.showMessageDialog(frame, "Payment Successful!\nChange: ₱" + String.format("%.2f", change), "Transaction Complete", JOptionPane.INFORMATION_MESSAGE);

                    new OrderReceipt(model, subTotalField.getText(), vatField.getText(), totalField.getText(), amountField.getText(), changeField.getText(), userName, path);
                    model.setRowCount(0);
                    updateCalculations();
                    amountField.setText("");
                    changeField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to save the transaction to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for the cash amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An unexpected error occurred.", "System Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}