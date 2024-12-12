/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package possystem.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import possystem.database.Config;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.oned.Code128Writer;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sithu
 */
public class Form_AddProduct extends javax.swing.JPanel {

    private String barcode;

    /**
     * Creates new form Form_AddProduct
     */
    public Form_AddProduct() {
        initComponents();
        
        loadCategories();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtname = new javax.swing.JTextField();
        txtdec = new javax.swing.JTextField();
        txtcategory = new javax.swing.JComboBox<>();
        txtbrand = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtcost = new javax.swing.JTextField();
        txtsellingprice = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtdiscount = new javax.swing.JTextField();
        txtreorderlevel = new javax.swing.JTextField();
        txtuom = new javax.swing.JComboBox<>();
        txtweight = new javax.swing.JTextField();
        txtvolume = new javax.swing.JTextField();
        cbactive = new javax.swing.JCheckBox();
        cbavailable = new javax.swing.JCheckBox();
        jLabel27 = new javax.swing.JLabel();
        txtbarcode = new javax.swing.JTextField();
        btngeneratebarcode = new javax.swing.JButton();
        btnsubmit = new javax.swing.JButton();
        btncancel = new javax.swing.JButton();
        txtquantity = new javax.swing.JSpinner();
        dtpexpdate = new com.toedter.calendar.JDateChooser();
        dtpproductdate = new com.toedter.calendar.JDateChooser();
        dtpdiscountstart = new com.toedter.calendar.JDateChooser();
        dtpdiscountend = new com.toedter.calendar.JDateChooser();
        btnprintbarcode = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Add Products");

        jPanel3.setOpaque(false);

        jLabel9.setText("Product name : ");

        jLabel10.setText("Description : ");

        jLabel11.setText("Category : ");

        jLabel12.setText("Brand :");

        jLabel13.setText("Selling Price : ");

        jLabel14.setText("Discount : ");

        txtcategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        txtcategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcategoryActionPerformed(evt);
            }
        });

        jLabel15.setText("Purchase Price : ");

        jLabel17.setText("Reorder Level : ");

        jLabel18.setText("Weight : ");

        jLabel19.setText("Expiration Date : ");

        jLabel20.setText("Discount Start Date : ");

        jLabel22.setText("Quantity : ");

        jLabel23.setText("Unit Of Measure : ");

        jLabel24.setText("Volume : ");

        jLabel25.setText("Production Date : ");

        jLabel26.setText("Discount End Date : ");

        txtreorderlevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtreorderlevelActionPerformed(evt);
            }
        });

        txtuom.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "KG", "G", "L", "ML", "UNIT" }));
        txtuom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtuomActionPerformed(evt);
            }
        });

        cbactive.setText("Is Active");
        cbactive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbactiveActionPerformed(evt);
            }
        });

        cbavailable.setText("Is Available For Sale");

        jLabel27.setText("Barcode : ");

        btngeneratebarcode.setText("Generate Barcode");
        btngeneratebarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngeneratebarcodeActionPerformed(evt);
            }
        });

        btnsubmit.setText("SUBMIT");
        btnsubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsubmitActionPerformed(evt);
            }
        });

        btncancel.setText("CANCEL");
        btncancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelActionPerformed(evt);
            }
        });

        btnprintbarcode.setText("Print Barcode");
        btnprintbarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprintbarcodeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(txtreorderlevel))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dtpdiscountstart, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 70, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(18, 18, 18)
                                .addComponent(txtweight)))
                        .addGap(159, 159, 159))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(18, 18, 18)
                                .addComponent(txtdiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel15)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtcost))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel11))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtname)
                                        .addComponent(txtcategory, 0, 168, Short.MAX_VALUE))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dtpexpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addGap(18, 18, 18)
                                .addComponent(txtvolume, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtuom, 0, 260, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel25)
                                            .addComponent(cbactive))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cbavailable)
                                            .addComponent(dtpproductdate, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10)
                                            .addComponent(jLabel12))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtbrand, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtdec, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel13)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtsellingprice, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel22)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtquantity, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(dtpdiscountend, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(191, 191, 191))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addGap(18, 18, 18)
                        .addComponent(txtbarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btngeneratebarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnprintbarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancel, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnsubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(243, 243, 243))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(txtname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtdec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtcategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(txtbrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txtcost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(txtsellingprice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtdiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27)
                            .addComponent(txtbarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btngeneratebarcode))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtquantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)
                            .addComponent(txtreorderlevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtuom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtweight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtvolume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbavailable)
                            .addComponent(cbactive))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel25)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dtpexpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(dtpproductdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(jLabel26))
                    .addComponent(dtpdiscountstart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dtpdiscountend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnsubmit)
                    .addComponent(btncancel)
                    .addComponent(btnprintbarcode))
                .addContainerGap(141, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1171, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane1.getViewport().setBackground(Color.WHITE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void loadCategories() {
        try {
            // Retrieve categories from the database and set in JComboBox
            Connection conn = Config.getConnection(); // Make sure to replace with your DB connection method
            String query = "SELECT category_name FROM categories"; // Adjust your table and column names
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            // Create a DefaultComboBoxModel to hold the categories
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            
            while (rs.next()) {
                model.addElement(rs.getString("category_name")); // Assuming 'category_name' is the column name
            }
            
            txtcategory.setModel(model);
        } catch (SQLException ex) {
            Logger.getLogger(Form_AddProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void txtcategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcategoryActionPerformed

    private void txtuomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtuomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtuomActionPerformed

    private void cbactiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbactiveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbactiveActionPerformed

    private void btncancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btncancelActionPerformed

    private void btnprintbarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprintbarcodeActionPerformed
        try {
            printBarcode(barcode);
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnprintbarcodeActionPerformed

    private static void printBarcode(String productCode) throws PrinterException {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                try {
                    // Barcode dimensions
                    int barcodeWidth = 300;
                    int barcodeHeight = 100;

                    // Generate barcode
                    Code128Writer barcodeWriter = new Code128Writer();
                    BitMatrix bitMatrix = barcodeWriter.encode(productCode, BarcodeFormat.CODE_128, barcodeWidth, barcodeHeight);

                    // Save barcode as temporary image
                    File barcodeImageFile = new File("barcode.png");
                    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", barcodeImageFile.toPath());

                    // Load the barcode image
                    Image barcodeImage = ImageIO.read(barcodeImageFile);

                    // Draw the barcode on the label
                    g2d.drawImage(barcodeImage, 10, 10, barcodeWidth, barcodeHeight, null);

                    // Add additional label information if needed
                    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                    g2d.drawString("Product Code: " + productCode, 10, barcodeHeight + 20);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return PAGE_EXISTS;
            }
        });

        // Show print dialog and print
        if (printerJob.printDialog()) {
            printerJob.print();
        }
    }
    
    private void btngeneratebarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngeneratebarcodeActionPerformed
        barcode = UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        txtbarcode.setText(barcode);
    }//GEN-LAST:event_btngeneratebarcodeActionPerformed

    private void btnsubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsubmitActionPerformed
        String name = txtname.getText();
        String description = txtdec.getText();
        String category = txtcategory.getSelectedItem().toString();
        String brand = txtbrand.getText();
        String purchasePrice = txtcost.getText().trim();
        String sellingPrice = txtsellingprice.getText().trim();
        String discount = txtdiscount.getText().trim();
        String barcode = txtbarcode.getText();
        String reorderLevel = txtreorderlevel.getText().trim();
        String quantity = txtquantity.getValue().toString();
        String weight = txtweight.getText().trim();
        String uom = (txtuom.getSelectedItem() != null) ? txtuom.getSelectedItem().toString() : null;
        String volume = txtvolume.getText().trim();
        boolean active = cbactive.isSelected();
        boolean available = cbavailable.isSelected();

        // Use SimpleDateFormat to format dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String productDate = (dtpproductdate.getDate() != null) ? sdf.format(dtpproductdate.getDate()) : null;
        String expDate = (dtpexpdate.getDate() != null) ? sdf.format(dtpexpdate.getDate()) : null;
        String discountStart = (dtpdiscountstart.getDate() != null) ? sdf.format(dtpdiscountstart.getDate()) : null;
        String discountEnd = (dtpdiscountend.getDate() != null) ? sdf.format(dtpdiscountend.getDate()) : null;

        // Database connection setup
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            // Establish connection to the database
            conn = Config.getConnection(); // Assuming you have a method to get the connection

            // SQL Insert Statement to add product data into the database
            String sql = "INSERT INTO products (product_id, product_name, category, brand, description, purchase_price, selling_price, discount, discount_start_date, discount_end_date, quantity, reorder_level, unit_of_measure, barcode, weight, volume, is_active, is_available, production_date, expiration_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pst = conn.prepareStatement(sql);
            pst.setString(1, barcode);
            pst.setString(2, name);
            pst.setString(3, category);
            pst.setString(4, brand.isEmpty() ? null : brand);
            pst.setString(5, description.isEmpty() ? null : description);
            pst.setDouble(6, Double.parseDouble(purchasePrice));
            pst.setDouble(7, Double.parseDouble(sellingPrice));
            pst.setDouble(8, discount.isEmpty() ? 0.0 : Double.parseDouble(discount));
            pst.setString(9, discountStart);
            pst.setString(10, discountEnd);
            pst.setInt(11, Integer.parseInt(quantity));
            pst.setInt(12, reorderLevel.isEmpty() ? 0 : Integer.parseInt(reorderLevel));
            pst.setString(13, uom);
            pst.setString(14, barcode);
            pst.setDouble(15, weight.isEmpty() ? 0.0 : Double.parseDouble(weight));
            pst.setDouble(16, volume.isEmpty() ? 0.0 : Double.parseDouble(volume));
            pst.setBoolean(17, active);
            pst.setBoolean(18, available);
            pst.setString(19, productDate);
            pst.setString(20, expDate);

            // Execute the statement
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                // Show success message
                JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Show failure message
                JOptionPane.showMessageDialog(this, "Failed to add product. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving product: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnsubmitActionPerformed

    private void txtreorderlevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtreorderlevelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtreorderlevelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncancel;
    private javax.swing.JButton btngeneratebarcode;
    private javax.swing.JButton btnprintbarcode;
    private javax.swing.JButton btnsubmit;
    private javax.swing.JCheckBox cbactive;
    private javax.swing.JCheckBox cbavailable;
    private com.toedter.calendar.JDateChooser dtpdiscountend;
    private com.toedter.calendar.JDateChooser dtpdiscountstart;
    private com.toedter.calendar.JDateChooser dtpexpdate;
    private com.toedter.calendar.JDateChooser dtpproductdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField txtbarcode;
    private javax.swing.JTextField txtbrand;
    private javax.swing.JComboBox<String> txtcategory;
    private javax.swing.JTextField txtcost;
    private javax.swing.JTextField txtdec;
    private javax.swing.JTextField txtdiscount;
    private javax.swing.JTextField txtname;
    private javax.swing.JSpinner txtquantity;
    private javax.swing.JTextField txtreorderlevel;
    private javax.swing.JTextField txtsellingprice;
    private javax.swing.JComboBox<String> txtuom;
    private javax.swing.JTextField txtvolume;
    private javax.swing.JTextField txtweight;
    // End of variables declaration//GEN-END:variables
}
