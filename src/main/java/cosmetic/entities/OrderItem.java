package cosmetic.entities;

public class OrderItem {
    private Product product;
    private int quantity;
    private double price; // Thêm biến này để lưu giá lúc mua

    public OrderItem(Product product, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Số lượng phải > 0");
        this.product = product;
        this.quantity = quantity;
        // Mặc định lấy giá hiện tại của sản phẩm
        this.price = product != null ? product.getPrice() : 0;
    }

    // Constructor dùng khi load từ Database (có giá lịch sử)
    public OrderItem(Product product, int quantity, double historicalPrice) {
        this.product = product;
        this.quantity = quantity;
        this.price = historicalPrice;
    }

    public double getSubtotal() {
        // Tính tổng dựa trên giá đã lưu (price), KHÔNG phải product.getPrice()
        return this.price * this.quantity;
    }

    // Getters & Setters
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    // Logic tăng giảm số lượng giữ nguyên...
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }
}