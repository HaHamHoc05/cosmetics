package cosmetic.entities;


public class OrderItem {
    private Long productId;
    private String productName;
    private double priceAtPurchase; // Giá lúc mua
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.priceAtPurchase = product.getPrice();
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return priceAtPurchase * quantity;
    }
    

    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getPriceAtPurchase() { return priceAtPurchase; }
    public int getQuantity() { return quantity; }
}