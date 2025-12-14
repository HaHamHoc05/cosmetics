package cosmetic.entities;

public class CartItem {
	private Long id;
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        validateProduct(product);
        validateQuantity(quantity, product);
        
        this.product = product;
        this.quantity = quantity;
    }

    // Getters
    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }

    // Setters
    public void setId(Long id) { this.id = id; }

    public void setQuantity(int quantity) {
        validateQuantity(quantity, this.product);
        this.quantity = quantity;
    }

    // Validation
    private void validateProduct(Product product) {
        if (product == null)
            throw new IllegalArgumentException("Sản phẩm không được để trống");
        if (!product.getStatus().canSell())
            throw new IllegalArgumentException("Sản phẩm " + product.getName() + " hiện không thể mua");
    }

    private void validateQuantity(int quantity, Product product) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        if (quantity > 99)
            throw new IllegalArgumentException("Số lượng tối đa là 99");
        if (!product.hasEnoughStock(quantity))
            throw new IllegalArgumentException("Vượt quá tồn kho. Còn lại: " + product.getStock());
    }

    // Business methods
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    public boolean isSameProduct(Long productId) {
        return this.product.getId().equals(productId);
    }

    public void increaseQuantity(int amount) {
        setQuantity(this.quantity + amount);
    }
}