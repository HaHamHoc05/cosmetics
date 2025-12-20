package cosmetic.entities;

public class CartItem {
    private Long id;
    private Long cartId;
    private Long productId;
    private String productName; // Lưu tên để hiển thị nhanh
    private int quantity;
    private double price;       // Giá tại thời điểm thêm vào giỏ

    public CartItem() {}

    public CartItem(Long productId, String productName, int quantity, double price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public double getSubTotal() {
        return this.price * this.quantity;
    }

    // Getters Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}