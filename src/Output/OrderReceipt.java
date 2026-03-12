package Output;

import javax.swing.*;

public class OrderReceipt {

    private JPanel receiptPanel;
    private JButton btnprint;
    private JTextArea receiptArea;

    public OrderReceipt() {
        JFrame frame = new JFrame("Receipt");
            frame.setContentPane(this.receiptPanel);
            frame.setSize(400, 800);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);


    }

}


