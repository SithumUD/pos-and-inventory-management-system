/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package possystem.database;

/**
 *
 * @author sithu
 */
import java.sql.*;
import javax.swing.DefaultListModel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import possystem.model.ProductDetails;

public class ProductSearch {

    public static void searchProducts(String query, DefaultListModel<String> listModel, JList<String> searchlist, JLayeredPane jLayeredPane1) {
        System.out.println("Search Query: " + query);

        Connection conn = Config.getConnection();
        if (conn != null) {
            try {
                String sql = "SELECT product_id, product_name FROM products WHERE product_name LIKE ? OR product_id LIKE ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                String searchQuery = "%" + query + "%";  // Match any product name or ID
                stmt.setString(1, searchQuery);
                stmt.setString(2, searchQuery);

                ResultSet rs = stmt.executeQuery();

                // Clear the list before adding new data
                listModel.clear();

                boolean hasResults = false;
                while (rs.next()) {
                    String productId = rs.getString("product_id");
                    String productName = rs.getString("product_name");
                    listModel.addElement(productId + " : " + productName);  // Update JList with search result
                    hasResults = true;
                }

                // If no results were found, hide the JList
                if (!hasResults) {
                    searchlist.setVisible(false);
                    jLayeredPane1.setVisible(false);
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to establish database connection.");
        }
    }
    
   // Method to get product details from the database
    public static ProductDetails getProductDetails(String productId, int quantity) {
        // Define the ProductDetails object to store retrieved data
        ProductDetails details = new ProductDetails();

        // Establish a database connection
        Connection conn = Config.getConnection();
        if (conn != null) {
            try {
                // Query to retrieve product details from the database
                String sql = "SELECT product_name, selling_price, discount FROM products WHERE product_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, productId);

                // Execute the query
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // Retrieve data from the result set
                    String productName = rs.getString("product_name");
                    double unitPrice = rs.getDouble("selling_price");
                    double discount = rs.getDouble("discount");

                    // Set the values in the ProductDetails object
                    details.setProductId(productId);
                    details.setProductName(productName);
                    details.setUnitPrice(unitPrice);
                    details.setDiscount(discount);

                    // Calculate and set the subtotal
                    details.setSubtotal(quantity);  // Assuming the method calculates the subtotal based on unit price, quantity, and discount
                }

                // Close the resources
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to establish database connection.");
        }

        // Return the populated ProductDetails object
        return details;
    }
}
