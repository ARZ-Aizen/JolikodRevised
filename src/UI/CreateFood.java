package UI;

import Database.DataManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

public class CreateFood {
    private JPanel mainPanel;
    private JTextField itemName, priceName, pathfield;
    private JComboBox categoryName;
    private JButton saveButton, cancelButton, browseButton;
    private JFrame frame;
    private DataManager dataManager;
    private AdminDashboard parentDashboard;

    public CreateFood(AdminDashboard parent) {
        this.parentDashboard = parent;
        this.dataManager = new DataManager();

        frame = new JFrame("Jolikod - Add New Food Item");
        frame.setContentPane(mainPanel);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.setSize(400, 320);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        URL iconURL = getClass().getResource("/iconImage.png");

        if (iconURL != null) {
            ImageIcon logo = new ImageIcon(iconURL);
            frame.setIconImage(logo.getImage());
        }

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (JPG, PNG, JPEG)", "jpg", "png", "jpeg");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    try {
                        java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(selectedFile);

                        if (img == null) {
                            JOptionPane.showMessageDialog(frame, "The selected file is not a valid image.");
                            return;
                        }

                        if (img.getWidth() == 150 && img.getHeight() == 150) {
                            pathfield.setText(selectedFile.getName());
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid Dimensions! Image must be exactly 150x150 pixels.\n" + "Selected image: " + img.getWidth() + "x" + img.getHeight());
                        }

                    } catch (java.io.IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error reading image file.");
                    }
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToDatabase();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        priceName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
    }


    private void saveToDatabase() {
        try {
            String name = itemName.getText().trim();
            String category = categoryName.getSelectedItem().toString();
            String priceStr = priceName.getText().trim();
            String imagePath = pathfield.getText().trim();

            if (name.isEmpty() || priceStr.isEmpty() || imagePath.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required!");
                return;
            }

            double price = Double.parseDouble(priceStr);

            if (dataManager.addFood(category, name, price, imagePath)) {
                JOptionPane.showMessageDialog(frame, "Successfully added to menu!");
                parentDashboard.loadFoodToTable();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Database error. Failed to add item.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid numeric price.");
        }
    }
}