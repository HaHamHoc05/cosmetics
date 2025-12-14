package cosmetic.entities;

public class OrderItem {
	private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private double unitPrice;

    public OrderItem(Product product, int quantity) {
        validateProduct(product);
        validateQuantity(quantity);
        
        this.productId = product.getId();
        this.productName = product.getName();
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
    }

    // Constructor từ database
    public OrderItem(Long id, Long productId, String productName, int quantity, double unitPrice) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters
    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }

    // Setters
    public void setId(Long id) { this.id = id; }

    // Validation
    private void validateProduct(Product product) {
        if (product == null)
            throw new IllegalArgumentException("Sản phẩm không được để trống");
        if (product.getId() == null)
            throw new IllegalArgumentException("Sản phẩm chưa được lưu");
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
    }

    // Business methods
    public double getSubtotal() {
        return unitPrice * quantity;
    }
}