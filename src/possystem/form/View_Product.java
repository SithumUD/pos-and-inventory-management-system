/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package possystem.form;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import possystem.database.Config;
import java.sql.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author sithu
 */
public class View_Product extends javax.swing.JPanel {

    /**
     * Creates new form View_Product
     */
    public View_Product() {
        initComponents();

        // Columns to display in the main table
        String[] columnNames = {
            "Product Code", "Name", "Category", "Selling Price", "Quantity in Stock", "Expiration Date"
        };

        // Create a DefaultTableModel for the table with the column names
        DefaultTableModel model = new DefaultTableModel(null, columnNames);

        // Set the model to the JTable (assuming tbproduct is the name of your JTable)
        tbproduct.setModel(model);
        tbproduct.setDefaultEditor(Object.class, null);

        // Call the method to populate the table with data
        populateTable();

        // Add a DocumentListener to the search field
        txtsearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                // Call the method to filter products based on search query
                String searchQuery = txtsearch.getText().trim();
                filterProducts(searchQuery);  // You will define this method below
            }
        });
    }

    private void populateTable() {
    DefaultTableModel model = (DefaultTableModel) tbproduct.getModel();
    model.setRowCount(0);  // Clear the table first
    
    // Fetch product data from the database again
    String query = "SELECT * FROM products";  // Replace 'products' with your actual table name

    try (Connection conn = Config.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            // Fetch each product's data and add it to the table
            String productCode = rs.getString("product_id");
            String name = rs.getString("product_name");
            String category = rs.getString("category");
            double sellingPrice = rs.getDouble("selling_price");
            int quantityInStock = rs.getInt("quantity");
            String expirationDate = rs.getString("expiration_date");

            model.addRow(new Object[] {
                productCode, name, category, sellingPrice, quantityInStock, expirationDate
            });
        }
    } catch (SQLException e) {
        System.out.println("Error fetching data: " + e.getMessage());
    }
}


    private void filterProducts(String searchQuery) {
        DefaultTableModel model = (DefaultTableModel) tbproduct.getModel();

        // Clear existing rows
        model.setRowCount(0);

        // Query to filter products based on search query for product name or ID
        String query = "SELECT * FROM products WHERE product_name LIKE ? OR product_id LIKE ?";

        try (Connection conn = Config.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            // Prepare query with LIKE for both product name and ID
            stmt.setString(1, "%" + searchQuery + "%");
            stmt.setString(2, "%" + searchQuery + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                // Loop through the result set and add matching products to the table
                while (rs.next()) {
                    String productCode = rs.getString("product_id");
                    String name = rs.getString("product_name");
                    String category = rs.getString("category");
                    double sellingPrice = rs.getDouble("selling_price");
                    int quantityInStock = rs.getInt("quantity");
                    String expirationDate = rs.getString("expiration_date");

                    // Add row to table
                    model.addRow(new Object[]{
                        productCode, name, category, sellingPrice, quantityInStock, expirationDate
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching filtered data: " + e.getMessage());
        }
    }

    private void viewProductDetails() {
        int selectedRow = tbproduct.getSelectedRow();

        if (selectedRow != -1) {
            // Retrieve the product code (or any unique identifier)
            String productCode = (String) tbproduct.getValueAt(selectedRow, 0);

            // Retrieve full product details from the database
            String[] fullProductDetails = getFullProductDetails(productCode);

            if (fullProductDetails != null) {
                // Create a popup (JOptionPane) to show the product details
                String message = "Product Code: " + fullProductDetails[0] + "\n"
                        + "Name: " + fullProductDetails[1] + "\n"
                        + "Description: " + fullProductDetails[2] + "\n"
                        + "Category: " + fullProductDetails[3] + "\n"
                        + "Brand: " + fullProductDetails[4] + "\n"
                        + "Purchase Price: " + fullProductDetails[5] + "\n"
                        + "Selling Price: " + fullProductDetails[6] + "\n"
                        + "Discount: " + fullProductDetails[7] + "\n"
                        + "Barcode: " + fullProductDetails[8] + "\n"
                        + "Reorder Level: " + fullProductDetails[9] + "\n"
                        + "Quantity in Stock: " + fullProductDetails[10] + "\n"
                        + "Weight: " + fullProductDetails[11] + "\n"
                        + "UOM: " + fullProductDetails[12] + "\n"
                        + "Volume: " + fullProductDetails[13] + "\n"
                        + "Active: " + fullProductDetails[14] + "\n"
                        + "Available: " + fullProductDetails[15] + "\n"
                        + "Product Date: " + fullProductDetails[16] + "\n"
                        + "Expiration Date: " + fullProductDetails[17] + "\n"
                        + "Discount Start Date: " + fullProductDetails[18] + "\n"
                        + "Discount End Date: " + fullProductDetails[19];

                JOptionPane.showMessageDialog(this, message, "Product Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Product details not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to view details.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String[] getFullProductDetails(String productCode) {
        String query = "SELECT * FROM products WHERE product_id = ?";
        String[] details = null;

        try (Connection conn = Config.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, productCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                details = new String[]{
                    rs.getString("product_id"),
                    rs.getString("product_name"),
                    rs.getString("description"),
                    rs.getString("category"),
                    rs.getString("brand"),
                    String.valueOf(rs.getDouble("purchase_price")),
                    String.valueOf(rs.getDouble("selling_price")),
                    String.valueOf(rs.getDouble("discount")),
                    rs.getString("barcode"),
                    String.valueOf(rs.getInt("reorder_level")),
                    String.valueOf(rs.getInt("quantity")),
                    String.valueOf(rs.getDouble("weight")),
                    rs.getString("unit_of_measure"),
                    String.valueOf(rs.getDouble("volume")),
                    String.valueOf(rs.getBoolean("is_active")),
                    String.valueOf(rs.getBoolean("is_available")),
                    rs.getString("production_date"),
                    rs.getString("expiration_date"),
                    rs.getString("discount_start_date"),
                    rs.getString("discount_end_date")
                };
            }
        } catch (SQLException e) {
            System.out.println("Error fetching full product details: " + e.getMessage());
        }

        return details;
    }

    private void deleteProduct() {
        int selectedRow = tbproduct.getSelectedRow();

        if (selectedRow != -1) {
            // Retrieve the product code (or any unique identifier)
            String productCode = (String) tbproduct.getValueAt(selectedRow, 0);

            // Show a confirmation dialog
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete the product with code: " + productCode + "?",
                    "Delete Product", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                // Proceed with the deletion from the database
                boolean isDeleted = deleteProductFromDatabase(productCode);

                if (isDeleted) {
                    JOptionPane.showMessageDialog(this, "Product deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Refresh the table after deletion
                    populateTable(); // Reload the data into the table
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting product. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean deleteProductFromDatabase(String productCode) {
    // Step 1: Check if the product is associated with any sales in the salesdetails table
    String checkSalesQuery = "SELECT COUNT(*) FROM salesdetails WHERE product_id = ?";
    try (Connection conn = Config.getConnection(); 
         PreparedStatement checkStmt = conn.prepareStatement(checkSalesQuery)) {
         
        checkStmt.setString(1, productCode);
        ResultSet rs = checkStmt.executeQuery();
        
        // Get the count of related sales
        if (rs.next()) {
            int salesCount = rs.getInt(1);
            
            // Step 2: If there are sales associated, ask user for action
            if (salesCount > 0) {
                // Show an alert asking whether to delete or set the product to inactive
                // (You need to implement the alert in the UI; for now, assume a user response is obtained)
                boolean userWantsToDelete = showAlertForSalesRelatedProduct();
                
                // Step 3: Perform the user's choice
                if (userWantsToDelete) {
                    // Delete product along with related sales data
                    return deleteProductAndSalesData(productCode);
                } else {
                    // Set product as inactive
                    return setProductInactive(productCode);
                }
            } else {
                // No related sales, safe to delete product
                return deleteProductOnly(productCode);
            }
        }
    } catch (SQLException e) {
        System.out.println("Error checking sales details: " + e.getMessage());
    }
    return false;
}

private boolean showAlertForSalesRelatedProduct() {
    // This method would show an alert in your application, asking whether to delete or set to inactive.
    // For now, we return true for delete or false for inactive based on user choice.
    // Implement your alert logic (UI code) to capture the user choice here.
    // Example: return true if user selects "Delete with sales history", false for "Set inactive".
    return false; // For demonstration purposes, we assume user wants to delete
}

private boolean deleteProductAndSalesData(String productCode) {
    // Delete product and related sales data
    String deleteProductQuery = "DELETE FROM products WHERE product_id = ?";
    String deleteSalesQuery = "DELETE FROM salesdetails WHERE product_id = ?";
    
    try (Connection conn = Config.getConnection(); 
         PreparedStatement deleteProductStmt = conn.prepareStatement(deleteProductQuery);
         PreparedStatement deleteSalesStmt = conn.prepareStatement(deleteSalesQuery)) {
        
        // Delete related sales data first (if any)
        deleteSalesStmt.setString(1, productCode);
        deleteSalesStmt.executeUpdate();
        
        // Now, delete the product from the products table
        deleteProductStmt.setString(1, productCode);
        int rowsAffected = deleteProductStmt.executeUpdate();
        
        return rowsAffected > 0;  // Return true if deletion is successful
    } catch (SQLException e) {
        System.out.println("Error deleting product and sales data: " + e.getMessage());
        return false;
    }
}

private boolean setProductInactive(String productCode) {
    // Set the product as inactive (so it won't be available for sale)
    String query = "UPDATE products SET is_active = false WHERE product_id = ?";
    
    try (Connection conn = Config.getConnection(); 
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, productCode);
        int rowsAffected = stmt.executeUpdate();
        
        return rowsAffected > 0;  // Return true if product was set to inactive
    } catch (SQLException e) {
        System.out.println("Error setting product inactive: " + e.getMessage());
        return false;
    }
}

private boolean deleteProductOnly(String productCode) {
    // Delete the product from the products table (if no related sales)
    String query = "DELETE FROM products WHERE product_id = ?";
    
    try (Connection conn = Config.getConnection(); 
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, productCode);
        int rowsAffected = stmt.executeUpdate();
        
        return rowsAffected > 0;  // Return true if deletion is successful
    } catch (SQLException e) {
        System.out.println("Error deleting product: " + e.getMessage());
        return false;
    }
}

private void updateproduct(){
    int selectedRow = tbproduct.getSelectedRow();

        if (selectedRow != -1) {
            // Retrieve the product code (or any unique identifier)
            String productCode = (String) tbproduct.getValueAt(selectedRow, 0);

            // Retrieve full product details from the database
            String[] fullProductDetails = getFullProductDetails(productCode);

            if (fullProductDetails != null) {
                Update_Product update_product = new Update_Product(fullProductDetails);
                update_product.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Product details not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to view details.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtsearch = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbproduct = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        btnupdate = new javax.swing.JButton();
        btnview = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Update Products");

        jLabel2.setText("Category");

        jLabel4.setText("quantity");

        jLabel5.setText("Search");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setText("sort");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jCheckBox1.setText("Is active");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("Is available");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jCheckBox1)
                        .addGap(58, 58, 58)
                        .addComponent(jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 286, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox1))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        tbproduct.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbproduct);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("DELETE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnupdate.setText("UPDATE");
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnview)
                .addGap(18, 18, 18)
                .addComponent(btnupdate)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(41, 41, 41))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnupdate)
                    .addComponent(btnview))
                .addGap(47, 47, 47))
        );

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

    private void btnviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnviewActionPerformed
        viewProductDetails();
    }//GEN-LAST:event_btnviewActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        deleteProduct();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void btnupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnupdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnupdateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnupdate;
    private javax.swing.JButton btnview;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tbproduct;
    private javax.swing.JTextField txtsearch;
    // End of variables declaration//GEN-END:variables
}
