package UI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

import Database.DataManager;

public class LoginUserInterface {

    private JPanel loginPanel, userWrapper, passwordWrapper, leftPanel, rightPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel eyelabel;
    private JFrame frame;

    public LoginUserInterface() {
        frame = new JFrame("Jolikod - Login");
        frame.setResizable(false);
        frame.setContentPane(this.loginPanel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        URL iconURL = getClass().getResource("/iconImage.png");

        if (iconURL != null) {
            ImageIcon logo = new ImageIcon(iconURL);
            frame.setIconImage(logo.getImage());
        }


        passwordWrapper.setOpaque(false);
        userWrapper.setOpaque(false);

        usernameField.putClientProperty("JComponent.outline", null);
        passwordField.putClientProperty("JComponent.outline", null);

        loginButton.putClientProperty("JButton.buttonType", "roundRect");
        usernameField.putClientProperty("JTextField.padding", new Insets(0, 10, 0, 0));
        passwordField.putClientProperty("JTextField.padding", new Insets(0, 10, 0, 0));

        usernameField.setBackground(Color.WHITE);
        passwordField.setBackground(Color.WHITE);

        usernameField.putClientProperty("JTextField.placeholderText", "Enter your username");
        passwordField.putClientProperty("JTextField.placeholderText", "Enter your password");

        if (eyelabel != null) {
            try {
                URL imgUrl = getClass().getResource("/eyelabel.png");
                if (imgUrl != null) {
                    ImageIcon icon = new ImageIcon(imgUrl);
                    Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    eyelabel.setIcon(new ImageIcon(scaled));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            passwordField.putClientProperty("JTextField.trailingComponent", eyelabel);

            eyelabel.setText("");
            eyelabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            eyelabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));

            eyelabel.addMouseListener(new java.awt.event.MouseAdapter() {
                private boolean isHidden = true;

                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    isHidden = !isHidden;
                    passwordField.setEchoChar(isHidden ? '•' : (char) 0);
                }
            });
        }

        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            DataManager manager = new DataManager();
            int userId = manager.login(user, pass);

            if (userId != -1) {
                JOptionPane.showMessageDialog(loginPanel, "Welcome, " + user + "!");
                if (userId == 1) new AdminDashboard();
                else new UserDashboard(user);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(loginPanel, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
            UIManager.put("Button.arc", 999);
            UIManager.put("Component.arc", 15);
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("ScrollBar.thumbArc", 999);

        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginUserInterface());
    }

}