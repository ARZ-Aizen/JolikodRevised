package UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Database.DataManager;

public class LoginUserInterface {

    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JButton AdminButton;
    private JFrame frame;

    public LoginUserInterface() {
        frame = new JFrame("Jolikod - Login");
        frame.setResizable(false);
        frame.setContentPane(this.panel1);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = textField1.getText();
                String pass = new String(passwordField1.getPassword());

                DataManager manager = new DataManager();
                int userId = manager.login(user, pass);

                if (userId != -1) {
                    JOptionPane.showMessageDialog(panel1, "Welcome, " + user + "!");

                    if (userId == 1) {
                        new AdminDashboard();
                    } else {
                        new UserDashboard(user);
                    }

                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(panel1, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUserInterface());
    }
}
