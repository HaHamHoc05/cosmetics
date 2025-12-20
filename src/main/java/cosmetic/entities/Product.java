package cosmetic.entities;

public class Product {
    private Long id;
    private String name;
    private double price;
    private int quantity; // Tồn kho
    private String description;
    private String imageUrl;
    private Long categoryId;
    private ProductStatus status;

    public Product() {
        this.status = ProductStatus.ACTIVE;
    }

    // --- BUSINESS LOGIC (SỬA: Thêm phần này để UseCase gọi không bị lỗi) ---
    
    // 1. Kiểm tra xem có đủ hàng để bán không
    public boolean canSell(int amount) {
        return this.status == ProductStatus.ACTIVE && this.quantity >= amount;
    }

    // 2. Trừ kho khi bán hàng
    public void decreaseStock(int amount) {
        if (!canSell(amount)) {
            throw new RuntimeException("Sản phẩm " + this.name + " không đủ hàng hoặc đã ngừng kinh doanh.");
        }
        this.quantity -= amount;
        if (this.quantity == 0) {
            this.status = ProductStatus.OUT_OF_STOCK;
        }
    }

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }
}