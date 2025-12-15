package cosmetic.entities;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        setQuantity(quantity);
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new RuntimeException("Số lượng không hợp lệ");
        this.quantity = quantity;
    }
    
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
}