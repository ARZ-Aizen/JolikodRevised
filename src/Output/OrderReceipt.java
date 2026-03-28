package Output;

import Database.DataManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class OrderReceipt {

    private JPanel receiptPanel;
    private JButton btnprint;
    private JTable table1;
    private JTextField textField3, textField1, textField2, textField5, textField4;
    private JLabel labelTime, labelDate, cashierLabel, labelAddress, labelContact, labelEmail;
    private JPanel wrapper;

    private DataManager dataManager = new DataManager();

    public void exportToPDF(JPanel panel, String path) {
        try {
            File file = new File(path);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            Rectangle pagesize = new Rectangle(panel.getWidth(), panel.getHeight());
            Document document = new Document(pagesize);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

            document.open();

            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate tp = cb.createTemplate(panel.getWidth(), panel.getHeight());
            Graphics2D g2 = tp.createGraphics(panel.getWidth(), panel.getHeight());

            panel.print(g2);
            g2.dispose();

            cb.addTemplate(tp, 0, 0);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "PDF Error: " + e.getMessage());
        }
    }

    public OrderReceipt(DefaultTableModel orderModel, String sub, String vat, String total, String cash, String change, String userName, String fullPath) {
        JFrame frame = new JFrame("Jolikod - Receipt");
        frame.setContentPane(this.receiptPanel);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss a");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

        if (labelTime != null) labelTime.setText(now.format(timeFormat));
        if (labelDate != null) labelDate.setText(now.format(dateFormat));
        this.cashierLabel.setText(userName);

        java.util.List<Object[]> businessInfo = dataManager.getAllReceipts();
        if (!businessInfo.isEmpty()) {
            Object[] info = businessInfo.get(0);
            labelAddress.setText(info[1].toString());
            labelContact.setText("Contact no: " + info[2].toString());
            labelEmail.setText("Email: " + info[3].toString());
        }

        DefaultTableModel receiptModel = new DefaultTableModel(new Object[]{"Item Name", "Qty", "Price"}, 0);
        for (int i = 0; i < orderModel.getRowCount(); i++) {
            receiptModel.addRow(new Object[]{
                    orderModel.getValueAt(i, 0),
                    orderModel.getValueAt(i, 2),
                    orderModel.getValueAt(i, 3)
            });
        }
        table1.setModel(receiptModel);

        textField1.setText(sub);
        textField2.setText(vat);
        textField3.setText(total);
        textField4.setText(cash);
        textField5.setText(change);

        frame.setSize(400, 800);
        frame.setLocationRelativeTo(null);



        btnprint.addActionListener(e -> {
            exportToPDF(receiptPanel, fullPath);

            JOptionPane.showMessageDialog(frame, "PDF Receipt Saved!");
            frame.dispose();
        });

        frame.setVisible(true);
    }
}