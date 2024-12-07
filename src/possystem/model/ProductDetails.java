/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package possystem.model;

/**
 *
 * @author sithu
 */
public class ProductDetails {
    private String productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double discount;
    private double subtotal;

    // Constructor
    public ProductDetails(String productId, String productName,int quantity, double unitPrice, double discount) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public ProductDetails() {
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    // Method to calculate and set the subtotal
    public void setSubtotal(int quantity) {
    // Calculate the total price before discount
    double totalPrice = unitPrice * quantity;

    // If the discount is a percentage
    double discountAmount = totalPrice * (discount / 100);

    // Calculate the subtotal by applying the discount
    this.subtotal = totalPrice - discountAmount;
}

}

