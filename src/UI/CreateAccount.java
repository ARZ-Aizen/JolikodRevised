package UI;

import Database.DataManager;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccount {

    private JPanel panelCreate;
    private JTextField newUsername;
    private JPasswordField newPassword;
    private JButton createUserButton;
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

        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = newUsername.getText() .trim();
                String pass = new String(newPassword.getPassword());

                if (user.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                    return;
                }

                DataManager manager = new DataManager();
                if (manager.createUser(user, pass)) {
                    JOptionPane.showMessageDialog(frame, "Account Created Successfully!");

                    if (parentDashboard != null) {
                        parentDashboard.loadUsersToTable();
                    }
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Error: Could not create account.");
                }

            }
        });

        frame.setVisible(true);
    }
}
