package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.awt.Color;
import javax.swing.BorderFactory;


import Database.DataManager;

public class LoginUserInterface {

    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JPanel passwordWraapper;
    private JLabel eyelabel;
    private JPanel userWrapper;
    private JLabel emptylabel;
    private JFrame frame;

    public LoginUserInterface() {
        frame = new JFrame("Jolikod - Login");
        frame.setResizable(false);
        frame.setContentPane(this.panel1);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        passwordWraapper.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        passwordWraapper.setBackground(Color.BLACK);

        passwordField1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        passwordField1.setBackground(Color.WHITE);

        userWrapper.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        userWrapper.setBackground(Color.BLACK);

        textField1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        textField1.setBackground(Color.WHITE);

        emptylabel.setBackground(Color.WHITE);
        emptylabel.setOpaque(true);

        eyelabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
        eyelabel.setBackground(Color.WHITE);
        eyelabel.setOpaque(true);

        try {
            URL imgUrl = getClass().getResource("/resources/eyelabel.png");
            if (imgUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imgUrl);
                Image scaled = originalIcon.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
                eyelabel.setIcon(new ImageIcon(scaled));
                eyelabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                System.out.println("Image not found at /resources/eyelabel.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        eyelabel.addMouseListener(new java.awt.event.MouseAdapter() {
            private boolean isHidden = true;
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (isHidden) {
                    passwordField1.setEchoChar((char) 0);
                    isHidden = false;
                } else {
                    passwordField1.setEchoChar('•');
                    isHidden = true;
                }
            }
        });

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
