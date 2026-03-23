package UI;

import Database.DataManager;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class CreateAccount {

    private JPanel panelCreate;
    private JTextField newUsername;
    private JPasswordField newPassword;
    private JButton createUserButton;
    private JLabel label;
    private JLabel subLabel;
    private JPanel LoginPanel;
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
        label = new JLabel("JOLIKOD");

        try {
            InputStream is = getClass().getResourceAsStream("/logo.otf");
            if (is != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
                label.setFont(customFont.deriveFont(48f));
            } else {
                System.out.println("Critical: logo.otf not found in resources!");
                label.setFont(new Font("SansSerif", Font.BOLD, 48));
            }
        } catch (Exception e) {
            label.setFont(new Font("SansSerif", Font.BOLD, 48));
        }

        label.setForeground(Color.decode("#FAD041"));
        subLabel = new JLabel("Create User Account");
        subLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    }
}