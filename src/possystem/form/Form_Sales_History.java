/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package possystem.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import possystem.database.Config;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author sithu
 */
public class Form_Sales_History extends javax.swing.JPanel {

    /**
     * Creates new form Form_Sales_History
     */
    public Form_Sales_History() {
        initComponents();

        DefaultTableModel model = new DefaultTableModel(new String[]{"Transaction ID", "Date", "Time", "Total Price", "Paid Amount", "Change Amount", "Payment Method", "Cashier"}, 0);
        tbsales.setModel(model);

        loadSalesData();

        // Add this to the constructor or form initialization method
        cbdaterange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected filter from the combo box
                String selectedFilter = cbdaterange.getSelectedItem().toString();
                // Apply the date filter based on the selection
                applyDateFilter(selectedFilter);
            }
        });

        cbpaymentmethod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected filter from the combo box
                String selectedFilter = cbpaymentmethod.getSelectedItem().toString();
                // Apply the date filter based on the selection
                cbpaymentmethod(selectedFilter);
            }
        });

    }

    private void loadSalesData() {
        String query = "SELECT sales_id, sales_date, sales_time, total_amount, paid_amount, change_amount, payment_method, cashier_id FROM sales";
        DefaultTableModel model = (DefaultTableModel) tbsales.getModel();
        model.setRowCount(0); // Clear existing rows

        try (Connection conn = Config.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // Retrieve data from the ResultSet
                String transactionId = rs.getString("sales_id");
                String date = rs.getString("sales_date");
                String time = rs.getString("sales_time");
                double totalPrice = rs.getDouble("total_amount");
                double paidPrice = rs.getDouble("paid_amount");
                double changePrice = rs.getDouble("change_amount");
                String paymentMethod = rs.getString("payment_method");
                String cashier = rs.getString("cashier_id");

                // Add row to the table model
                model.addRow(new Object[]{transactionId, date, time, totalPrice, paidPrice, changePrice, paymentMethod, cashier});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading sales data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchSale() {
        String searchText = txtsearchsale.getText().trim(); // Get text from txtsearchsale

        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Transaction ID.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "SELECT sales_id, sales_date, sales_time, total_amount, paid_amount, change_amount, payment_method, cashier_id FROM sales WHERE sales_id = ?";

        DefaultTableModel model = (DefaultTableModel) tbsales.getModel();
        model.setRowCount(0); // Clear existing rows in the table

        try (Connection conn = Config.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set parameter for the search query
            stmt.setString(1, searchText);

            try (ResultSet rs = stmt.executeQuery()) {
                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    String transactionId = rs.getString("sales_id");
                    String date = rs.getString("sales_date");
                    String time = rs.getString("sales_time");
                    double totalPrice = rs.getDouble("total_amount");
                    double paidPrice = rs.getDouble("paid_amount");
                    double changePrice = rs.getDouble("change_amount");
                    String paymentMethod = rs.getString("payment_method");
                    String cashier = rs.getString("cashier_id");

                    // Add row to the table model
                    model.addRow(new Object[]{transactionId, date, time, totalPrice, paidPrice, changePrice, paymentMethod, cashier});
                }

                if (!hasResults) {
                    JOptionPane.showMessageDialog(this, "No transaction found with the given Transaction ID.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error searching sales data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // State to track whether the filter is applied
    private boolean isFilterApplied = false;

    private void toggleFilter() {
        if (!isFilterApplied) {
            // Apply filter
            filterSalesByDateRange();
            // Update button text and state
            btnapply.setText("Reset Filter");
            isFilterApplied = true;
        } else {
            // Reset filter
            resetSalesData();
            // Update button text and state
            btnapply.setText("Apply Filter");
            isFilterApplied = false;
        }
    }

    private void filterSalesByDateRange() {
        // Get selected start and end dates
        // Retrieve the java.util.Date objects from startdate and enddate
        java.util.Date utilStartDate = startdate.getDate();
        java.util.Date utilEndDate = enddate.getDate();

        if (utilStartDate == null || utilEndDate == null) {
            JOptionPane.showMessageDialog(this, "Please select both Start Date and End Date.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ensure startDate <= endDate
        if (utilStartDate.after(utilEndDate)) {
            JOptionPane.showMessageDialog(this, "Start Date cannot be after End Date.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date startDate = new java.sql.Date(utilStartDate.getTime());
        Date endDate = new java.sql.Date(utilEndDate.getTime());

        String query = "SELECT sales_id, sales_date, sales_time, total_amount, paid_amount, change_amount, payment_method, cashier_id "
                + "FROM sales WHERE sales_date BETWEEN ? AND ?";

        DefaultTableModel model = (DefaultTableModel) tbsales.getModel();
        model.setRowCount(0); // Clear existing rows in the table

        try (Connection conn = Config.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set parameters for the query
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    String transactionId = rs.getString("sales_id");
                    String date = rs.getString("sales_date");
                    String time = rs.getString("sales_time");
                    double totalPrice = rs.getDouble("total_amount");
                    double paidPrice = rs.getDouble("paid_amount");
                    double changePrice = rs.getDouble("change_amount");
                    String paymentMethod = rs.getString("payment_method");
                    String cashier = rs.getString("cashier_id");

                    // Add row to the table model
                    model.addRow(new Object[]{transactionId, date, time, totalPrice, paidPrice, changePrice, paymentMethod, cashier});
                }

                if (!hasResults) {
                    JOptionPane.showMessageDialog(this, "No transactions found within the selected date range.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    resetSalesData();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error filtering sales data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

// Method to reset filter
    private void resetSalesData() {
        // Clear date pickers
        java.util.Date validDate = new java.util.Date();
        startdate.setDate(validDate);
        enddate.setDate(validDate);

        // Reload all sales data
        loadSalesData();
    }

    private void applyDateFilter(String filter) {
        loadSalesData(filter); // Re-load the data based on the selected filter
    }

    private void loadSalesData(String filter) {
        String query = "SELECT sales_id, sales_date, sales_time, total_amount, paid_amount, change_amount, payment_method, cashier_id FROM sales";

        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

        if (filter != null && !filter.equals("Any")) {
            String dateCondition = "";

            switch (filter) {
                case "Today":
                    dateCondition = " WHERE sales_date = ?";
                    break;
                case "Yesterday":
                    long millisInDay = 1000 * 60 * 60 * 24;
                    java.sql.Date yesterday = new java.sql.Date(System.currentTimeMillis() - millisInDay);
                    dateCondition = " WHERE sales_date = ?";
                    break;
                case "This Month":
                    java.sql.Date startOfMonth = new java.sql.Date(today.getYear(), today.getMonth(), 1);
                    java.sql.Date endOfMonth = new java.sql.Date(today.getYear(), today.getMonth() + 1, 0); // Get the last date of the current month
                    dateCondition = " WHERE sales_date BETWEEN ? AND ?";
                    break;
            }
            query += dateCondition;
        }

        DefaultTableModel model = (DefaultTableModel) tbsales.getModel();
        model.setRowCount(0); // Clear existing rows

        try (Connection conn = Config.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            if (filter.equals("Today")) {
                stmt.setDate(1, today);
            } else if (filter.equals("Yesterday")) {
                java.sql.Date yesterday = new java.sql.Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
                stmt.setDate(1, yesterday);
            } else if (filter.equals("This Month")) {
                java.sql.Date startOfMonth = new java.sql.Date(today.getYear(), today.getMonth(), 1);
                java.sql.Date endOfMonth = new java.sql.Date(today.getYear(), today.getMonth() + 1, 0);
                stmt.setDate(1, startOfMonth);
                stmt.setDate(2, endOfMonth);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String transactionId = rs.getString("sales_id");
                    String date = rs.getString("sales_date");
                    String time = rs.getString("sales_time");
                    double totalPrice = rs.getDouble("total_amount");
                    double paidPrice = rs.getDouble("paid_amount");
                    double changePrice = rs.getDouble("change_amount");
                    String paymentMethod = rs.getString("payment_method");
                    String cashier = rs.getString("cashier_id");

                    model.addRow(new Object[]{transactionId, date, time, totalPrice, paidPrice, changePrice, paymentMethod, cashier});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading sales data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cbpaymentmethod(String filter){
        String query = "SELECT sales_id, sales_date, sales_time, total_amount, paid_amount, change_amount, payment_method, cashier_id FROM sales";

    // Apply filter if the filter is not "Any"
    if (filter != null && !filter.equals("Any")) {
        query += " WHERE payment_method = ?";
    }

    DefaultTableModel model = (DefaultTableModel) tbsales.getModel();
    model.setRowCount(0); // Clear existing rows

    try (Connection conn = Config.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
        // Set the filter parameter if it's not "Any"
        if (!filter.equals("Any")) {
            stmt.setString(1, filter); // Set the selected payment method as the filter
        }

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String transactionId = rs.getString("sales_id");
                String date = rs.getString("sales_date");
                String time = rs.getString("sales_time");
                double totalPrice = rs.getDouble("total_amount");
                double paidPrice = rs.getDouble("paid_amount");
                double changePrice = rs.getDouble("change_amount");
                String paymentMethod = rs.getString("payment_method");
                String cashier = rs.getString("cashier_id");

                model.addRow(new Object[]{transactionId, date, time, totalPrice, paidPrice, changePrice, paymentMethod, cashier});
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading sales data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtsearchsale = new javax.swing.JTextField();
        btnsearch = new javax.swing.JButton();
        startdate = new com.toedter.calendar.JDateChooser();
        enddate = new com.toedter.calendar.JDateChooser();
        btnapply = new javax.swing.JButton();
        cbdaterange = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cbpaymentmethod = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbsales = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        btnupdate = new javax.swing.JButton();
        btnview = new javax.swing.JButton();

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Transaction History");

        jLabel12.setText("start date");

        jLabel13.setText("end date");

        jLabel14.setText("Search");

        btnsearch.setText("Search");
        btnsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsearchActionPerformed(evt);
            }
        });

        btnapply.setText("Apply Filter");
        btnapply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnapplyActionPerformed(evt);
            }
        });

        cbdaterange.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Any", "Today", "Yesterday", "This Month" }));

        jLabel15.setText("Specific Date Range");

        jLabel16.setText("Payment Method");

        cbpaymentmethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Any", "Cash", "Card" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(txtsearchsale, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnsearch))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(enddate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnapply)
                        .addGap(127, 127, 127)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbdaterange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbpaymentmethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtsearchsale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnsearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(startdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnapply)
                                .addComponent(cbdaterange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15)
                                .addComponent(jLabel16)
                                .addComponent(cbpaymentmethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel13)
                                .addComponent(enddate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(12, Short.MAX_VALUE))))
        );

        tbsales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbsales);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1389, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton1.setText("DELETE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnupdate.setText("Export as CVS");
        btnupdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnupdateActionPerformed(evt);
            }
        });

        btnview.setText("VIEW");
        btnview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnviewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator3))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnupdate)
                .addGap(81, 81, 81)
                .addComponent(btnview)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(41, 41, 41))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(56, 56, 56)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnupdate)
                    .addComponent(btnview))
                .addGap(47, 47, 47))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnupdateActionPerformed

    }//GEN-LAST:event_btnupdateActionPerformed

    private void btnviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnviewActionPerformed

    }//GEN-LAST:event_btnviewActionPerformed

    private void btnsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsearchActionPerformed
        searchSale();
    }//GEN-LAST:event_btnsearchActionPerformed

    private void btnapplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnapplyActionPerformed
        toggleFilter();
    }//GEN-LAST:event_btnapplyActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnapply;
    private javax.swing.JButton btnsearch;
    private javax.swing.JButton btnupdate;
    private javax.swing.JButton btnview;
    private javax.swing.JComboBox<String> cbdaterange;
    private javax.swing.JComboBox<String> cbpaymentmethod;
    private com.toedter.calendar.JDateChooser enddate;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private com.toedter.calendar.JDateChooser startdate;
    private javax.swing.JTable tbsales;
    private javax.swing.JTextField txtsearch;
    private javax.swing.JTextField txtsearch1;
    private javax.swing.JTextField txtsearchsale;
    // End of variables declaration//GEN-END:variables
}
