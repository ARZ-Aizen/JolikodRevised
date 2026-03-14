package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import Database.DataManager;
import Output.OrderReceipt;

public class UserDashboard {

    private JPanel user;
    private JButton logoutButton;
    private JLabel welcomeLabel;
    private JButton mainDishButton;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JButton sideDishButton;
    private JButton drinksButton;
    private JButton desertButton;
    private JPanel panelDish;
    private JButton addButton2;
    private JSpinner spinner1;
    private JPanel id1;
    private JPanel id2;
    private JPanel id3;
    private JPanel id4;
    private JPanel id5;
    private JPanel id6;
    private JButton addButton3;
    private JSpinner spinner2;
    private JButton addButton4;
    private JSpinner spinner3;
    private JButton addButton5;
    private JSpinner spinner4;
    private JButton addButton6;
    private JSpinner spinner5;
    private JButton addButton7;
    private JSpinner spinner6;
    private JPanel id7;
    private JButton addButton8;
    private JSpinner spinner7;
    private JTable table1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton payButton;
    private JButton removeButton;
    private JButton addButton9;
    private JSpinner spinner8;
    private JPanel id8;
    private JPanel id9;
    private JPanel id10;
    private JPanel id11;
    private JPanel id12;
    private JSpinner spinner9;
    private JSpinner spinner10;
    private JButton addButton11;
    private JSpinner spinner11;
    private JButton addButton12;
    private JSpinner spinner12;
    private JButton addButton13;
    private JPanel id14;
    private JPanel id13;
    private JPanel id15;
    private JPanel id16;
    private JPanel id17;
    private JPanel id18;
    private JPanel id19;
    private JButton addButton14;
    private JButton addButton15;
    private JButton addButton10;
    private JButton addButton17;
    private JButton addButton18;
    private JButton addButton19;
    private JButton addButton20;
    private JSpinner spinner19;
    private JSpinner spinner18;
    private JSpinner spinner17;
    private JSpinner spinner16;
    private JSpinner spinner15;
    private JSpinner spinner14;
    private JSpinner spinner13;
    private JPanel id20;
    private JPanel id21;
    private JPanel id22;
    private JPanel id23;
    private JPanel id24;
    private JPanel id25;
    private JButton addButton21;
    private JButton addButton22;
    private JButton addButton23;
    private JButton addButton24;
    private JButton addButton25;
    private JButton addButton26;
    private JSpinner spinner25;
    private JSpinner spinner24;
    private JSpinner spinner23;
    private JSpinner spinner22;
    private JSpinner spinner21;
    private JSpinner spinner20;
    private JButton addButton16;
    private JFrame frame;
    private DataManager dataManager;

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
            ((JFormattedTextField) textField).setFormatterFactory(
                    new DefaultFormatterFactory(formatter)
            );
        }
    }

    public void addItemToTable(String itemName, int quantity) {
        double price = dataManager.getItemPrice(itemName);

        if (price != -1.0) {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
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
            table1.revalidate();
            table1.repaint();
            updateCalculations();
        } else {
            JOptionPane.showMessageDialog(frame, "Item not found in database: " + itemName);
        }
    }

    public void updateCalculations() {
        double subtotal = 0;
        DefaultTableModel model = (DefaultTableModel) table1.getModel();

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

    public UserDashboard() {
        frame = new JFrame("Jolikod - User Dashboard");
        frame.setContentPane(this.user);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        dataManager = new DataManager();

        JSpinner[] allSpinners = {
                spinner1, spinner2, spinner3, spinner4, spinner5,
                spinner6, spinner7, spinner8, spinner9, spinner10,
                spinner11, spinner12, spinner13, spinner14, spinner15,
                spinner16, spinner17, spinner18, spinner19, spinner20,
                spinner21, spinner22, spinner23, spinner24, spinner25
        };

        JTextField[] currencyFields = {textField1, textField2, textField3, textField4, textField5};
        welcomeLabel.setText("Welcome, user");
        CardLayout cl = (CardLayout) panelDish.getLayout();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Item Name", "Price", "Qty", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table1.setModel(model);

        for (JSpinner s : allSpinners) {
            if (s != null) {
                ConfigurePositiveSpinner(s);
            }
        }
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
                cl.show(panelDish, "Card1");
            }
        });
        sideDishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(panelDish, "Card2");
            }
        });
        drinksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(panelDish, "Card3");
            }
        });
        desertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(panelDish, "Card4");
            }
        });

        textField4.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });

        // Main Dishes
        addButton2.addActionListener(e -> addItemToTable("Fried Chicken", (int) spinner1.getValue()));
        addButton3.addActionListener(e -> addItemToTable("1pc Fried Porkchop", (int) spinner2.getValue()));
        addButton4.addActionListener(e -> addItemToTable("4pc Chicken Nuggets", (int) spinner3.getValue()));
        addButton5.addActionListener(e -> addItemToTable("6pc Chicken Nuggets", (int) spinner4.getValue()));
        addButton6.addActionListener(e -> addItemToTable("Joli Spaghetti", (int) spinner5.getValue()));
        addButton7.addActionListener(e -> addItemToTable("Joli Pancit", (int) spinner6.getValue()));
        addButton8.addActionListener(e -> addItemToTable("Joli Palabok", (int) spinner7.getValue()));
        addButton9.addActionListener(e -> addItemToTable("Burger Steak", (int) spinner8.getValue()));
        // Side Dishes
        addButton10.addActionListener(e -> addItemToTable("Joli Hotdog", (int) spinner9.getValue()));
        addButton11.addActionListener(e -> addItemToTable("Joli Burger", (int) spinner10.getValue()));
        addButton12.addActionListener(e -> addItemToTable("Chicken Sandwich", (int) spinner11.getValue()));
        addButton13.addActionListener(e -> addItemToTable("Cheesy Joli Hotdog", (int) spinner12.getValue()));
        // Drinks
        addButton14.addActionListener(e -> addItemToTable("Pineapple Juice", (int) spinner13.getValue()));
        addButton15.addActionListener(e -> addItemToTable("Iced Tea", (int) spinner14.getValue()));
        addButton16.addActionListener(e -> addItemToTable("Coke Float", (int) spinner15.getValue()));
        addButton17.addActionListener(e -> addItemToTable("Coke", (int) spinner16.getValue()));
        addButton18.addActionListener(e -> addItemToTable("Sprite", (int) spinner17.getValue()));
        addButton19.addActionListener(e -> addItemToTable("Gulaman", (int) spinner18.getValue()));
        addButton20.addActionListener(e -> addItemToTable("Orange Juice", (int) spinner19.getValue()));
        // Desert
        addButton21.addActionListener(e -> addItemToTable("Halo-Halo", (int) spinner20.getValue()));
        addButton22.addActionListener(e -> addItemToTable("Buko Pandan", (int) spinner21.getValue()));
        addButton23.addActionListener(e -> addItemToTable("Leche Flan", (int) spinner22.getValue()));
        addButton24.addActionListener(e -> addItemToTable("Peach Mango Pie", (int) spinner23.getValue()));
        addButton25.addActionListener(e -> addItemToTable("Choco Sundae", (int) spinner24.getValue()));
        addButton26.addActionListener(e -> addItemToTable("Strawberry Sundae", (int) spinner25.getValue()));

        removeButton.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                ((DefaultTableModel) table1.getModel()).removeRow(selectedRow);
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

                double cash = Double.parseDouble(textField4.getText().trim());

                if (cash >= total) {
                    double change = cash - total;
                    textField5.setText(String.format("%.2f", change));

                    DefaultTableModel currentModel = (DefaultTableModel) table1.getModel();
                    String sub = textField1.getText();
                    String vat = textField2.getText();
                    String totalVal = textField3.getText();
                    String cashVal = textField4.getText();
                    String changeVal = textField5.getText();

                    JOptionPane.showMessageDialog(frame, "Transaction Complete!\nChange: ₱" + String.format("%.2f", change));
                    new OrderReceipt(currentModel, sub, vat, totalVal, cashVal, changeVal);
                    ((DefaultTableModel) table1.getModel()).setRowCount(0);
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