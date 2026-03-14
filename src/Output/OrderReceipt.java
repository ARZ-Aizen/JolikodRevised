package Output;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OrderReceipt {

    private JPanel receiptPanel;
    private JButton btnprint;
    private JTable table1;
    private JTextField textField3;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField5;
    private JTextField textField4;

    public OrderReceipt(DefaultTableModel orderModel, String sub, String vat, String total, String cash, String change) {
        JFrame frame = new JFrame("Receipt");
        frame.setContentPane(this.receiptPanel);

        DefaultTableModel receiptModel = new DefaultTableModel(new Object[]{"Item Name", "Qty", "Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = 0; i < orderModel.getRowCount(); i++) {
            String name = orderModel.getValueAt(i, 0).toString();
            String qty = orderModel.getValueAt(i, 2).toString();
            String price = orderModel.getValueAt(i, 3).toString();
            receiptModel.addRow(new Object[]{name, qty, price});
        }
        table1.setModel(receiptModel);
        textField1.setText(sub);
        textField2.setText(vat);
        textField3.setText(total);
        textField4.setText(cash);
        textField5.setText(change);


        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        btnprint.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Printing Receipt...");
        });
    }
}