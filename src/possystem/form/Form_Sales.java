/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package possystem.form;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import possystem.database.ProductSearch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import possystem.database.Config;
import possystem.model.ProductDetails;
import java.sql.*;
import possystem.main.LoginForm;

public class Form_Sales extends javax.swing.JPanel {
    
    private DefaultListModel<String> listModel;
    private DefaultTableModel tableModel;
    private String salesID;

    /**
     * Creates new form Form_Sales
     */
    public Form_Sales() {
        initComponents();
        jLayeredPane1.setVisible(false);
        startDateTimeUpdater();
        initializeProductSearch();
        btncard.setEnabled(false);
        setupChangeCalculation();
        salesID = generateSalesID();
        txtbarcodevalue.setEditable(false);
        txtbarcodevalue.requestFocus();
        barcoderead();
        
        if (checkSalesIDExists(salesID)) {
            // If it exists, generate a new ID
            System.out.println("Sales ID exists, generating a new one...");
            salesID = generateSalesID();
        } else {
            txtsalesid.setText(salesID);
        }
        
        if (isCardReaderAvailable()) {
            btncard.setEnabled(true);
        }

        // Initialize the table model and set it to the salesTable
        tableModel = new DefaultTableModel();
        carttable.setModel(tableModel); // Ensure salesTable is initialized before setting the model

        // Add columns to the table model
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Product Name");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Unit Price");
        tableModel.addColumn("Discount");
        tableModel.addColumn("Subtotal");

        // Add TableModelListener to detect quantity changes
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();  // Get the row where change occurred
                int column = e.getColumn(); // Get the column where change occurred
                int eventType = e.getType();  // Get the event type (INSERT, DELETE, UPDATE)

                // Check if the quantity column (index 2) was updated
                if (column == 2) {
                    // Recalculate subtotal based on the new quantity
                    recalculateSubtotal(row);
                    summingsubtotal();
                }
                
                if (eventType == TableModelEvent.INSERT) {
                    //System.out.println("Row added at index: " + row);
                    summingsubtotal();
                } else if (eventType == TableModelEvent.DELETE) {
                    //System.out.println("Row deleted at index: " + row);
                    summingsubtotal();
                } else if (eventType == TableModelEvent.UPDATE) {
                    //System.out.println("Cell updated at row " + row + ", column " + column);
                    summingsubtotal();
                }
            }
        });
        
        //txtbarcodevalue.addFocusListener(new FocusAdapter() {
  // @Override
  // public void focusLost(FocusEvent e) {
        //Log when focus is lost from the barcode field
     //  System.out.println("Focus lost from barcode field.");
        // If the barcode field loses focus, immediately set focus back
     //   txtbarcodevalue.requestFocus();
     //  System.out.println("Focus returned to barcode field.");
  //  }
//});

// Optional: Add FocusListener to other fields to demonstrate focus behavior
txtsearch.addFocusListener(new FocusAdapter() {
    @Override
    public void focusLost(FocusEvent e) {
        // Log when focus is lost from the search field
        System.out.println("Focus lost from search field.");
        // If focus lost on product name, set focus back to barcode
        txtbarcodevalue.requestFocus();
        System.out.println("Focus returned to barcode field.");
    }
});

//txtpaidamount.addFocusListener(new FocusAdapter() {
   // @Override
   // public void focusLost(FocusEvent e) {
        // Log when focus is lost from the paid amount field
       // System.out.println("Focus lost from paid amount field.");
        // If focus lost on price, set focus back to barcode
       // txtbarcodevalue.requestFocus();
       // System.out.println("Focus returned to barcode field.");
   // }
//});

        
    }
    
    public String generateSalesID() {
        // Generate Sales ID based on current timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String salesID = "SALE" + sdf.format(new Date());
        return salesID;
    }
    
    public boolean checkSalesIDExists(String salesID) {
        String query = "SELECT COUNT(*) FROM sales WHERE sales_id = ?";
        try (Connection conn = Config.getConnection(); // Auto-close connection
                 PreparedStatement statement = conn.prepareStatement(query)) {
            
            statement.setString(1, salesID);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking Sales ID: " + e.getMessage());
        }
        return false;
    }
    
    private void startDateTimeUpdater() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
                
                Date now = new Date();

                // Update the labels
                txtdate.setText(dateFormatter.format(now));
                txttime.setText(timeFormatter.format(now));
            }
        }, 0, 1000); // Update every second
    }
    
    private void initializeProductSearch() {
        listModel = new DefaultListModel<>();
        searchlist.setModel(listModel);  // Make sure the JList uses this model

        txtsearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = txtsearch.getText(); // Get text from search field
                if (query.isEmpty()) {
                    searchlist.setVisible(false);  // Hide JList if search box is empty
                    jLayeredPane1.setVisible(false);
                } else {
                    searchlist.setVisible(true);  // Show JList when typing
                    jLayeredPane1.setVisible(true);
                    ProductSearch.searchProducts(query, listModel, searchlist, jLayeredPane1);  // Search products and update JList
                }
            }
        });

        // Add listener for item selection in the JList
        searchlist.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                
                int quantity = 1;
                // Check if an item is selected
                if (!e.getValueIsAdjusting()) {
                    String selectedItem = searchlist.getSelectedValue(); // Get selected product

                    if (selectedItem != null) {
                        String productId = selectedItem.split(":")[0].trim(); // Extract product ID
                        String productName = selectedItem.split(":")[1].trim(); // Extract product name

                        // Add to sales table (this will add a row to the table)
                        addToSalesTable(productId, productName, quantity);
                    }
                }
            }
        });
    }
    
    private void barcoderead(){
        
        txtbarcodevalue.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                txtbarcodevalue.requestFocus();
                String barcode = txtbarcodevalue.getText();
                // Check if the barcode is not empty and length is valid
            if (barcode.length() > 0) {
                // Call a method to fetch product name from the database
                getProductNameByBarcode(barcode);
            }
            }
        });
    }
    
    private void getProductNameByBarcode(String barcode) {
    // Database connection variable
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        // Get the connection from the Config class
        conn = Config.getConnection();

        // SQL query to find product by barcode
        String query = "SELECT product_name FROM products WHERE product_id = ?";  // Assuming barcode is the product_id
        stmt = conn.prepareStatement(query);
        stmt.setString(1, barcode); // Set barcode value in the query

        // Execute the query
        rs = stmt.executeQuery();

        // Check if a result was returned
        if (rs.next()) {
            // If found, get the product name
            String productName = rs.getString("product_name");
            int quantity = 1;

            addToSalesTable(barcode,productName,quantity); 
            
        } else {
            // If no matching barcode, show a message or do something else
            JOptionPane.showMessageDialog(this, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle any database errors
    } finally {
        // Close the database resources
        Config.closeConnection(conn);
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

    
    private void addToSalesTable(String productId, String productName, int quantity) {
        // Check if the product already exists in the sales table
        int rowIndex = -1;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String existingProductId = tableModel.getValueAt(i, 0).toString(); // Assuming Product ID is in column 0
            if (existingProductId.equals(productId)) {
                rowIndex = i;  // If found, store the row index
                break;
            }
        }
        
        if (rowIndex == -1) {
            // Product not found, add new row
            ProductDetails productDetails = ProductSearch.getProductDetails(productId, quantity);
            if (productDetails != null) {
                Object[] rowData = {
                    productId,
                    productName,
                    quantity,
                    productDetails.getUnitPrice(),
                    productDetails.getDiscount(),
                    productDetails.getSubtotal()
                };
                
                System.out.println("Adding New Row: " + productDetails.getSubtotal()); // Debug
                tableModel.addRow(rowData); // Add row

                // Trigger subtotal update
                summingsubtotal();

                // Trigger TableModelEvent manually
                TableModelEvent event = new TableModelEvent(tableModel, tableModel.getRowCount() - 1, tableModel.getRowCount() - 1, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
                tableModel.fireTableChanged(event);
            } else {
                JOptionPane.showMessageDialog(this, "Product details not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Product already exists, update quantity
            int existingQuantity = Integer.parseInt(tableModel.getValueAt(rowIndex, 2).toString());
            int newQuantity = existingQuantity + quantity;
            
            tableModel.setValueAt(newQuantity, rowIndex, 2); // Update quantity
            recalculateSubtotal(rowIndex); // Update subtotal
            summingsubtotal(); // Update total
        }
        
        jLayeredPane1.setVisible(false);
        txtsearch.setText(null); // Clear search
    }
    
    private void recalculateSubtotal(int row) {
        try {
            // Get values from the row to recalculate subtotal
            double quantity = Double.parseDouble(tableModel.getValueAt(row, 2).toString()); // Assuming Quantity is in column 2
            double unitPrice = Double.parseDouble(tableModel.getValueAt(row, 3).toString()); // Assuming Unit Price is in column 3
            double discount = Double.parseDouble(tableModel.getValueAt(row, 4).toString()); // Assuming Discount is in column 4

            // Calculate the new subtotal
            double subtotal = quantity * (unitPrice - discount);

            // Set the new calculated subtotal into the table (in column 5)
            tableModel.setValueAt(String.format("%.2f", subtotal), row, 5); // Format subtotal to 2 decimal places

        } catch (NumberFormatException e) {
            // Handle invalid input if needed
            JOptionPane.showMessageDialog(this, "Error in calculating subtotal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void summingsubtotal() {
        double total = 0.0; // Initialize the total sum

        // Get the number of rows in the table
        int rowCount = tableModel.getRowCount();

        // Debug: print the row count
        System.out.println("Row Count: " + rowCount);

        // Iterate through each row
        for (int row = 0; row < rowCount; row++) {
            // Get the value from the "subtotal" column (column index 5 in this example)
            Object value = tableModel.getValueAt(row, 5); // Adjust column index if needed

            // Debug: print the value
            System.out.println("Row " + row + " Subtotal Value: " + value);

            // Parse the value and add to total
            if (value != null) {
                try {
                    if (value instanceof String) {
                        total += Double.parseDouble((String) value); // Parse string to double
                    } else if (value instanceof Number) {
                        total += ((Number) value).doubleValue(); // Use Number's doubleValue
                    }
                } catch (NumberFormatException e) {
                    // Debug: log any parsing errors
                    System.out.println("Error parsing subtotal value at row " + row + ": " + value);
                }
            }
        }

        // Update the total in the UI
        txtsubtotal.setText(String.format("%.2f", total)); // Format total to 2 decimal places
        txttotal.setText(String.format("%.2f", total));

        // Debug: print the calculated total
        System.out.println("Total Sum of Subtotals: " + total);
    }

    // Method to check if a card reader is available
    public static boolean isCardReaderAvailable() {
        try {
            // Simulate card reader check
            boolean cardReaderConnected = false; // Replace with actual hardware check
            return cardReaderConnected;
        } catch (Exception e) {
            System.err.println("Error checking card reader availability: " + e.getMessage());
            return false;
        }
    }

    // Handle cash payment selection
    private void handleCashPayment() {
        resetButtonColors(); // Reset all button colors
        btncash.setBackground(Color.GREEN); // Set background color to green for cash payment
        JOptionPane.showMessageDialog(this, "Cash payment selected.", "Payment Method", JOptionPane.INFORMATION_MESSAGE);
    }

    // Handle card payment selection
    private void handleCardPayment() {
        if (btncard.isEnabled()) {
            resetButtonColors(); // Reset all button colors
            btncard.setBackground(Color.GREEN); // Set background color to green for card payment
            JOptionPane.showMessageDialog(this, "Card payment selected.", "Payment Method", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Card reader is not available. Please select another payment method.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Reset button colors to default
    private void resetButtonColors() {
        btncash.setBackground(null); // Reset to default background
        btncard.setBackground(null); // Reset to default background
    }
    
    private void setupChangeCalculation() {
        // Add a DocumentListener to txtpaidamount
        txtpaidamount.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calchange();
            }
            
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calchange();
            }
            
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calchange();
            }
        });
    }
    
    private void calchange() {
        try {
            // Parse the total amount from txttotal
            double total = Double.parseDouble(txttotal.getText());

            // Parse the amount given by the customer from txtpaidamount
            String paidAmountText = txtpaidamount.getText().trim();
            if (paidAmountText.isEmpty()) {
                txtchange.setText(""); // Clear the change if paid amount is empty
                return;
            }
            
            double amountGiven = Double.parseDouble(paidAmountText);

            // Check if the amount given is sufficient
            if (amountGiven >= total) {
                // Calculate the change
                double change = amountGiven - total;

                // Display the calculated change in txtchange
                txtchange.setText(String.format("%.2f", change));
            } else {
                // Clear txtchange if the amount is insufficient
                txtchange.setText("");
            }
        } catch (NumberFormatException e) {
            // Clear txtchange and ignore invalid input
            txtchange.setText("");
        }
    }
    
    private void handleSaleComplete() {
    // Step 1: Validate if total and paid amount are valid
    double totalAmount = Double.parseDouble(txttotal.getText());
    double paidAmount = 0.0;
    
    try {
        paidAmount = Double.parseDouble(txtpaidamount.getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid amount entered for paid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Step 2: Check if paid amount is sufficient
    if (paidAmount < totalAmount) {
        JOptionPane.showMessageDialog(this, "Paid amount is less than total. Please enter a valid paid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Step 3: Calculate change
    double change = paidAmount - totalAmount;
    txtchange.setText(String.format("%.2f", change));  // Display the change

    // Step 4: Insert sale data into the database
    try (Connection conn = Config.getConnection()) {
        // Insert sale header (sales information)
        String insertSaleQuery = "INSERT INTO sales (sales_id, sales_date, sales_time, total_amount, paid_amount, change_amount) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSaleQuery)) {
            ps.setString(1, salesID);
            ps.setString(2, txtdate.getText());  // Current date
            ps.setString(3, txttime.getText());  // Current time
            ps.setDouble(4, totalAmount);
            ps.setDouble(5, paidAmount);
            ps.setDouble(6, change);
            ps.executeUpdate();
        }

        // Insert sale items (product details)
        String insertItemQuery = "INSERT INTO salesdetails (sales_id, product_id, product_name, quantity, unit_price, discount, subtotal) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertItemQuery)) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String productId = tableModel.getValueAt(i, 0).toString();
                String productName = tableModel.getValueAt(i, 1).toString();
                int quantity = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                double unitPrice = Double.parseDouble(tableModel.getValueAt(i, 3).toString());
                double discount = Double.parseDouble(tableModel.getValueAt(i, 4).toString());
                double subtotal = Double.parseDouble(tableModel.getValueAt(i, 5).toString());

                // Set values in prepared statement
                ps.setString(1, salesID);
                ps.setString(2, productId);
                ps.setString(3, productName);
                ps.setInt(4, quantity);
                ps.setDouble(5, unitPrice);
                ps.setDouble(6, discount);
                ps.setDouble(7, subtotal);
                ps.addBatch(); // Add to batch
            }

            // Execute batch update for sale items
            ps.executeBatch();
        }
        
        // Step 5: Show confirmation dialog and receipt
        JOptionPane.showMessageDialog(this, "Sale completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        
        // Optionally, print or show the receipt (customize as needed)
        generateReceipt(totalAmount, paidAmount, change);

        // Step 6: Reset fields for the next sale
        resetSale();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error completing sale: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void generateReceipt(double totalAmount, double paidAmount, double change) {
    // You can customize the receipt format, for now it's just a simple print statement
    String receipt = "------ Receipt ------\n";
    receipt += "Sales ID: " + salesID + "\n";
    receipt += "Date: " + txtdate.getText() + "\n";
    receipt += "Time: " + txttime.getText() + "\n";
    receipt += "--------------------\n";

    // Loop through the table to add products to the receipt
    for (int i = 0; i < tableModel.getRowCount(); i++) {
        receipt += "Product: " + tableModel.getValueAt(i, 1) + "\n";
        receipt += "Quantity: " + tableModel.getValueAt(i, 2) + "\n";
        receipt += "Unit Price: " + tableModel.getValueAt(i, 3) + "\n";
        receipt += "Discount: " + tableModel.getValueAt(i, 4) + "\n";
        receipt += "Subtotal: " + tableModel.getValueAt(i, 5) + "\n";
        receipt += "--------------------\n";
    }

    receipt += "Total: " + totalAmount + "\n";
    receipt += "Paid Amount: " + paidAmount + "\n";
    receipt += "Change: " + change + "\n";
    receipt += "--------------------\n";
    
    // You can either print this string or show it in a dialog
    System.out.println(receipt);  // Debug: Print receipt to console
    
    // For example, to show in a dialog:
    JOptionPane.showMessageDialog(this, receipt, "Receipt", JOptionPane.INFORMATION_MESSAGE);
}

private void resetSale() {
    // Reset the sales form for the next transaction
    tableModel.setRowCount(0);  // Clear table
    txtsubtotal.setText("0.00");  // Reset subtotal
    txttotal.setText("0.00");  // Reset total
    txtpaidamount.setText("");  // Reset paid amount
    txtchange.setText("");  // Reset change
    salesID = generateSalesID();  // Generate a new Sales ID for the next transaction
    txtsalesid.setText(salesID);  // Display the new Sales ID
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        searchlist = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        txtdate = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txttime = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        txtsalesid = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtsearch = new javax.swing.JTextField();
        txtbarcodevalue = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtsubtotal = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txttotal = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        btncash = new javax.swing.JButton();
        btncard = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        txtpaidamount = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        txtchange = new javax.swing.JLabel();
        btncomplete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        carttable = new javax.swing.JTable();
        btnlogout = new javax.swing.JButton();
        btnfullscreen = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        searchlist.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(searchlist);

        jLayeredPane1.setLayer(jScrollPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 340, 160));

        jLabel9.setText("ADMIN");

        jLabel8.setText("Cashier Name");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtdate.setText("dfdsf");

        jLabel11.setText("Date :");

        jLabel27.setText("Time :");

        txttime.setText("11:45 AM");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtdate)
                .addGap(18, 18, 18)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txttime)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtdate)
                    .addComponent(jLabel11)
                    .addComponent(jLabel27)
                    .addComponent(txttime))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtsalesid.setText("SALES 01");

        jLabel13.setText("Sales ID");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(txtsalesid)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtsalesid)
                    .addComponent(jLabel13))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel29.setText("0");

        jLabel30.setText("Transactions");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(jLabel29)
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel15.setText("Product Code");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(txtbarcodevalue, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15)
                        .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtbarcodevalue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(12, Short.MAX_VALUE))))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 44, 1010, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("MATHALE STORES");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 6, -1, -1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Subtotal");

        txtsubtotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtsubtotal.setText("0.00");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 185, Short.MAX_VALUE)
                .addComponent(txtsubtotal)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtsubtotal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setText("Discount");

        jLabel5.setText("0.00");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel7.setText("0.00");

        jLabel6.setText("Tax");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel18.setText("TOTAL");

        txttotal.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        txttotal.setText("0.00");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(txttotal)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txttotal)))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setText("Payment Method");

        btncash.setText("CASH");
        btncash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncashActionPerformed(evt);
            }
        });

        btncard.setText("CARD");
        btncard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncardActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addGap(35, 35, 35)
                .addComponent(btncash)
                .addGap(18, 18, 18)
                .addComponent(btncard)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(btncash)
                    .addComponent(btncard))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Amount Paid");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtpaidamount, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtpaidamount, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(jLabel23))
                .addContainerGap())
        );

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setText("Change");

        txtchange.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtchange.setText("0.00");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtchange)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtchange))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btncomplete.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btncomplete.setText("Sale Complete");
        btncomplete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncompleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btncomplete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btncomplete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6)))
                .addContainerGap())
        );

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 426, -1, -1));

        carttable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Product code", "Product Name", "Quantity", "Unit Price", "Discount", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(carttable);
        if (carttable.getColumnModel().getColumnCount() > 0) {
            carttable.getColumnModel().getColumn(0).setResizable(false);
            carttable.getColumnModel().getColumn(1).setResizable(false);
            carttable.getColumnModel().getColumn(2).setResizable(false);
            carttable.getColumnModel().getColumn(3).setResizable(false);
            carttable.getColumnModel().getColumn(4).setResizable(false);
            carttable.getColumnModel().getColumn(5).setResizable(false);
            carttable.getColumnModel().getColumn(6).setResizable(false);
        }

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 140, 1012, 280));

        btnlogout.setText("Logout");
        btnlogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlogoutActionPerformed(evt);
            }
        });
        add(btnlogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(814, 6, -1, -1));

        btnfullscreen.setText("fullscreen");
        btnfullscreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfullscreenActionPerformed(evt);
            }
        });
        add(btnfullscreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(912, 6, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btncashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncashActionPerformed
        handleCashPayment();
    }//GEN-LAST:event_btncashActionPerformed

    private void btncardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncardActionPerformed
        handleCardPayment();
    }//GEN-LAST:event_btncardActionPerformed

    private void btncompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncompleteActionPerformed
        handleSaleComplete();
    }//GEN-LAST:event_btncompleteActionPerformed

    private void btnlogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlogoutActionPerformed
        // Close the current (main) form
    java.awt.Window win = SwingUtilities.getWindowAncestor(this);
    if (win instanceof JFrame) {
        ((JFrame) win).dispose();  // Close the main JFrame
    }
    
    // Create and show the login form
    LoginForm loginForm = new LoginForm(); // Create an instance of the login form
    loginForm.setVisible(true); // Make the login form visible
    }//GEN-LAST:event_btnlogoutActionPerformed

    private void btnfullscreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfullscreenActionPerformed
       
    }//GEN-LAST:event_btnfullscreenActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncard;
    private javax.swing.JButton btncash;
    private javax.swing.JButton btncomplete;
    private javax.swing.JButton btnfullscreen;
    private javax.swing.JButton btnlogout;
    private javax.swing.JTable carttable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> searchlist;
    private javax.swing.JTextField txtbarcodevalue;
    private javax.swing.JLabel txtchange;
    private javax.swing.JLabel txtdate;
    private javax.swing.JTextField txtpaidamount;
    private javax.swing.JLabel txtsalesid;
    private javax.swing.JTextField txtsearch;
    private javax.swing.JLabel txtsubtotal;
    private javax.swing.JLabel txttime;
    private javax.swing.JLabel txttotal;
    // End of variables declaration//GEN-END:variables
}
