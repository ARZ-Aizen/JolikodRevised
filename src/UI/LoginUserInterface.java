package UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Database.DataManager;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;

public class LoginUserInterface {

    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JButton AdminButton;
    private JFrame frame;

    public LoginUserInterface() {
        frame = new JFrame("Jolikod");
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
                boolean isAuthenticated = manager.login(user, pass);

                if (isAuthenticated) {
                    JOptionPane.showMessageDialog(panel1, "Welcome, " + user + "!");
                    new UserDashboard();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(panel1, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        AdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminDashboard();
                disposeCurrentFrame();
            }
        });
        frame.setVisible(true);
    }

    private void disposeCurrentFrame() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
        if (currentFrame != null) {
            currentFrame.dispose();
        }
    }

    public static void main (String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUserInterface());
    }
}
