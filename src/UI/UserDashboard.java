package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

import Database.DataManager;
import Output.OrderReceipt;
import Database.DataManager.FoodItem;

public class UserDashboard {

    private JPanel user;
    private JPanel main;
    private JPanel cardSide;
    private JPanel orderSide;
    private JPanel panelDish; // The CardLayout container
    private JPanel allDish, mainDish, sideDish, drinkDish, dessertDish;
    private JButton allButton, mainDishButton, sideDishButton, drinksButton, dessertButton, logoutButton;
    private JTable cartTable;
    private JScrollPane cartScroll;
    private JTextField textField1, textField2, textField3, textField4, textField5;
    private JButton payButton, removeButton;
    private JLabel welcomeLabel;
    private JFrame frame;
    private DataManager dataManager;
    private JPanel panelButton;
    private JPanel payRemove, panelSale;
    private JScrollPane scrollAll;
    private JScrollPane scrollMain;
    private JScrollPane scrollSide;
    private JScrollPane scrollDrink;
    private JScrollPane scrollDessert;
    ;

    //

    public void ConfigurePositiveSpinner(JSpinner spinner) {
        spinner.setModel(new SpinnerNumberModel(1, 1, null, 1));
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setEditable(false);
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
                System.out.println("Cannot find: /" + imgPath);
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

        JSpinner qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        qtySpinner.setMaximumSize(new Dimension(60, 25));
        qtySpinner.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        // 1. Clear everything
        allDish.removeAll();
        mainDish.removeAll();
        sideDish.removeAll();
        drinkDish.removeAll();
        dessertDish.removeAll();

        // 2. Set Layouts
        allDish.setLayout(new java.awt.GridLayout(0, 3, 10, 10));
        mainDish.setLayout(new java.awt.GridLayout(0, 3, 10, 10));
        sideDish.setLayout(new java.awt.GridLayout(0, 3, 10, 10));
        drinkDish.setLayout(new java.awt.GridLayout(0, 3, 10, 10));
        dessertDish.setLayout(new java.awt.GridLayout(0, 3, 10, 10));

        List<FoodItem> items = dataManager.getMenuData();

        for (FoodItem item : items) {
            // --- KEY FIX ---
            // Create one card SPECIFICALLY for the "All" view
            JPanel cardForAll = createFoodCard(item.name, item.price, item.imagePath);
            allDish.add(cardForAll);

            // Create a SECOND card for the specific category view
            JPanel cardForCategory = createFoodCard(item.name, item.price, item.imagePath);

            if (item.category.equalsIgnoreCase("Main Dish")) {
                mainDish.add(cardForCategory);
            } else if (item.category.equalsIgnoreCase("Side Dish")) {
                sideDish.add(cardForCategory);
            } else if (item.category.equalsIgnoreCase("Drinks")) {
                drinkDish.add(cardForCategory);
            } else if (item.category.equalsIgnoreCase("Dessert")) {
                dessertDish.add(cardForCategory);
            }
        }

        allDish.revalidate();
        mainDish.revalidate();
        sideDish.revalidate();
        drinkDish.revalidate();
        dessertDish.revalidate();

        panelDish.revalidate();
        panelDish.repaint();
    }

    public void addItemToTable(String itemName, int quantity) {
        double price = dataManager.getItemPrice(itemName);

        if (price != -1.0) {
            DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
            boolean itemExists = false;

            for (int i = 0; i < model.getRowCount(); i++) {
                String existingItemName = (String) model.getValueAt(i, 0);

                if (existingItemName.equals(itemName)) {
                    int currentQty = (int) model.getValueAt(i, 2);
                    int newQty = currentQty + quantity;
                    double newTotal = price * newQty;

                    model.setValueAt(newQty, i, 2);
                    model.setValueAt(newTotal, i, 3);

                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                double total = price * quantity;
                model.addRow(new Object[]{itemName, price, quantity, total});
            }
            cartTable.revalidate();
            cartTable.repaint();
            updateCalculations();
        } else {
            JOptionPane.showMessageDialog(frame, "Item not found in database: " + itemName);
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

        if (textField1 instanceof JFormattedTextField) {
            ((JFormattedTextField) textField1).setValue(subtotal);
            ((JFormattedTextField) textField2).setValue(vat);
            ((JFormattedTextField) textField3).setValue(grandTotal);
        } else {

            textField1.setText(String.format("%.2f", subtotal));
            textField2.setText(String.format("%.2f", vat));
            textField3.setText(String.format("%.2f", grandTotal));
        }
    }

    //

    public UserDashboard(String userName) {
        frame = new JFrame("Jolikod - User Dashboard");
        frame.setContentPane(this.user);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);

        dataManager = new DataManager();
        loadDynamicMenu();

        int scrollSpeed = 20;
        scrollAll.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        scrollMain.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        scrollSide.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        scrollDrink.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        scrollDessert.getVerticalScrollBar().setUnitIncrement(scrollSpeed);

        JTextField[] currencyFields = {textField1, textField2, textField3, textField4, textField5};
        this.welcomeLabel.setText("Welcome, " + userName);
        CardLayout cl = (CardLayout) panelDish.getLayout();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Item Name", "Price", "Qty", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cartTable.setModel(model);

        for (JTextField f : currencyFields) {
            ConfigureCurrencyField(f);
        }

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginUserInterface();
                frame.dispose();
            }
        });
        mainDishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(panelDish, "Card2");
            }
        });
        sideDishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(panelDish, "Card3");
            }
        });
        drinksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(panelDish, "Card4");
            }
        });
        dessertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(panelDish, "Card5");
            }
        });
        allButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(panelDish, "Card1");
            }
        });

        textField4.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }

                textField4.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        String raw = textField4.getText().replaceAll(",", "");

                        if (raw.isEmpty()) return;

                        try {
                            long value = Long.parseLong(raw);

                            java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
                            String formatted = formatter.format(value);

                            textField4.setText(formatted);

                        } catch (NumberFormatException ex) {
                        }
                    }
                });

            }
        });


        removeButton.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow != -1) {
                ((DefaultTableModel) cartTable.getModel()).removeRow(selectedRow);
                updateCalculations();
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an item to remove.");
            }
        });


        payButton.addActionListener(e -> {
            try {
                double total = 0;
                if (textField3 instanceof JFormattedTextField) {
                    Object val = ((JFormattedTextField) textField3).getValue();
                    total = (val instanceof Number) ? ((Number) val).doubleValue() : 0.0;
                } else {
                    total = Double.parseDouble(textField3.getText().replaceAll("[^0-9.]", ""));
                }

                String rawCash = textField4.getText().replaceAll(",", "").trim();
                double cash = Double.parseDouble(rawCash);

                if (cash >= total) {
                    double change = cash - total;

                    textField5.setText(String.format("%,.2f", change));

                    DefaultTableModel currentModel = (DefaultTableModel) cartTable.getModel();
                    String sub = textField1.getText();
                    String vat = textField2.getText();
                    String totalVal = textField3.getText();

                    String cashVal = textField4.getText();
                    String changeVal = textField5.getText();

                    JOptionPane.showMessageDialog(frame, "Transaction Complete!\nChange: ₱" + String.format("%,.2f", change));

                    new OrderReceipt(currentModel, sub, vat, totalVal, cashVal, changeVal, userName);

                    ((DefaultTableModel) cartTable.getModel()).setRowCount(0);
                    updateCalculations();
                    textField4.setText("");
                    textField5.setText("");

                } else {
                    JOptionPane.showMessageDialog(frame, "Insufficient cash!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for cash.");
            }
        });

        frame.setVisible(true);
    }

}