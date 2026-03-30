package UI;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import Database.DataManager;

public class LoginUserInterface {

    private JPanel panel1, userWrapper, passwordWraapper;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JLabel eyelabel;
    private JFrame frame;

    public LoginUserInterface() {
        frame = new JFrame("Jolikod - Login");
        frame.setResizable(false);
        frame.setContentPane(this.panel1);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        URL iconURL = getClass().getResource("/iconImage.png");

        if (iconURL != null) {
            ImageIcon logo = new ImageIcon(iconURL);
            frame.setIconImage(logo.getImage());
        }


        passwordWraapper.setOpaque(false);
        userWrapper.setOpaque(false);

        textField1.putClientProperty("JComponent.outline", null);
        passwordField1.putClientProperty("JComponent.outline", null);

        loginButton.putClientProperty("JButton.buttonType", "roundRect");
        textField1.putClientProperty("JTextField.padding", new Insets(0, 10, 0, 0));
        passwordField1.putClientProperty("JTextField.padding", new Insets(0, 10, 0, 0));

        textField1.setBackground(Color.WHITE);
        passwordField1.setBackground(Color.WHITE);

        textField1.putClientProperty("JTextField.placeholderText", "Enter your username");
        passwordField1.putClientProperty("JTextField.placeholderText", "Enter your password");

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

            passwordField1.putClientProperty("JTextField.trailingComponent", eyelabel);

            eyelabel.setText("");
            eyelabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            eyelabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));

            eyelabel.addMouseListener(new java.awt.event.MouseAdapter() {
                private boolean isHidden = true;

                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    isHidden = !isHidden;
                    passwordField1.setEchoChar(isHidden ? '•' : (char) 0);
                }
            });
        }

        loginButton.addActionListener(e -> {
            String user = textField1.getText();
            String pass = new String(passwordField1.getPassword());
            DataManager manager = new DataManager();
            int userId = manager.login(user, pass);

            if (userId != -1) {
                JOptionPane.showMessageDialog(panel1, "Welcome, " + user + "!");
                if (userId == 1) new AdminDashboard();
                else new UserDashboard(user);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(panel1, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
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