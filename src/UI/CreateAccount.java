package UI;

import Database.DataManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class CreateAccount {

    private JPanel panelCreate, LoginPanel;
    private JTextField newUsername;
    private JPasswordField newPassword, newPasswordConfirm;
    private JButton createUserButton;
    private JLabel labelName, subLabel, showPass, showConfirmPass;
    private JFrame frame;
    private AdminDashboard parentDashboard;

    public CreateAccount(AdminDashboard parent) {
        this.parentDashboard = parent;
        frame = new JFrame("Jolikod - Create Account");
        frame.setContentPane(this.panelCreate);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        URL iconURL = getClass().getResource("/iconImage.png");

        if (iconURL != null) {
            ImageIcon logo = new ImageIcon(iconURL);
            frame.setIconImage(logo.getImage());
        }

        newPassword.setLayout(new BorderLayout());
        newPasswordConfirm.setLayout(new BorderLayout());

        showPass.setHorizontalAlignment(SwingConstants.RIGHT);
        showConfirmPass.setHorizontalAlignment(SwingConstants.RIGHT);

        showPass.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        showConfirmPass.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

        newPassword.add(showPass, BorderLayout.EAST);
        newPasswordConfirm.add(showConfirmPass, BorderLayout.EAST);

        showPass.setOpaque(false);
        showConfirmPass.setOpaque(false);
        showPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showConfirmPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        showPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showConfirmPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        showPass.setOpaque(false);
        showConfirmPass.setOpaque(false);

        char defaultEchoChar = newPassword.getEchoChar();

        showPass.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (newPassword.getEchoChar() != (char) 0) {
                    newPassword.setEchoChar((char) 0); // Show password
                } else {
                    newPassword.setEchoChar(defaultEchoChar); // Hide password
                }
            }
        });

        showConfirmPass.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (newPasswordConfirm.getEchoChar() != (char) 0) {
                    newPasswordConfirm.setEchoChar((char) 0); // Show password
                } else {
                    newPasswordConfirm.setEchoChar(defaultEchoChar); // Hide password
                }
            }
        });

        createUserButton.addActionListener(e -> {
            String user = newUsername.getText().trim();
            String pass = new String(newPassword.getPassword());
            String confirmPass = new String(newPasswordConfirm.getPassword());

            if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }

            if (!pass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match. Please try again.");
                return;
            }

            DataManager manager = new DataManager();
            if (manager.createUser(user, pass)) {
                JOptionPane.showMessageDialog(frame, "Account Created Successfully!");
                if (parentDashboard != null) parentDashboard.loadUsersToTable();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Error: Could not create account.");
            }
        });

        frame.setVisible(true);
    }
}