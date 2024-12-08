/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package possystem.swing;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author sithu
 */
public class ProductDetailsDialog extends JDialog {

    public ProductDetailsDialog(Frame parent, String[] productDetails) {
        super(parent, "Product Details", true);
        
        // Set up the layout manager for the dialog
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Define the labels and their values
        String[] labels = {
            "Product Code:", "Name:", "Description:", "Category:", "Brand:", 
            "Purchase Price:", "Selling Price:", "Discount:", "Barcode:", 
            "Reorder Level:", "Quantity in Stock:", "Weight:", "UOM:", 
            "Volume:", "Active:", "Available:", "Product Date:", "Expiration Date:", 
            "Discount Start Date:", "Discount End Date:"
        };

        // Create the panels and labels
        for (int i = 0; i < labels.length; i++) {
            // Create and configure label
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setPreferredSize(new Dimension(180, 30));
            gbc.gridx = 0;
            gbc.gridy = i;
            add(label, gbc);

            // Create and configure value label
            JLabel valueLabel = new JLabel(productDetails[i]);
            valueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            valueLabel.setPreferredSize(new Dimension(200, 30));
            gbc.gridx = 1;
            add(valueLabel, gbc);
        }

        // Close Button
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the dialog
            }
        });

        // Position the close button at the bottom of the dialog
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        add(closeButton, gbc);
        
        // Set the dialog's size and location
        pack();
        setLocationRelativeTo(parent);
    }

    public static void showProductDetails(Frame parent, String[] productDetails) {
        ProductDetailsDialog dialog = new ProductDetailsDialog(parent, productDetails);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        // Sample data for testing
        String[] productDetails = {
            "P123", "Product Name", "This is a sample product description.", 
            "Electronics", "BrandX", "$50.00", "$70.00", "10%", "1234567890", 
            "50", "200", "1.5 kg", "Piece", "0.5 L", "Yes", "Yes", 
            "2024-12-01", "2025-12-01", "2024-12-01", "2025-06-01"
        };

        // Create and show the product details dialog
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showProductDetails(null, productDetails);
            }
        });
    }
}
