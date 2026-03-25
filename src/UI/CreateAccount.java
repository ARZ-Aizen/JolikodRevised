package UI;

import Database.DataManager;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;

public class CreateAccount {

    private JPanel panelCreate;
    private JTextField newUsername;
    private JPasswordField newPassword;
    private JButton createUserButton;
    private JLabel labelName;
    private JLabel subLabel;
    private JPanel LoginPanel;
    private JFrame frame;
    private AdminDashboard parentDashboard;

    public static Font loadCustomFont(float size) {
        try {
            InputStream is = CreateAccount.class.getResourceAsStream("/logo.otf");
            if (is == null) return new Font("SansSerif", Font.PLAIN, (int) size);
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(size);
        } catch (Exception e) {
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }

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

        createUserButton.addActionListener(e -> {
            String user = newUsername.getText().trim();
            String pass = new String(newPassword.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
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

    private void createUIComponents() {
        labelName = new JLabel();
        labelName.setFont(loadCustomFont(42f));
        labelName.setForeground(Color.decode("#FAD041"));

        subLabel = new JLabel("Create User Account");
        subLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    }
}