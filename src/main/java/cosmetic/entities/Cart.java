package cosmetic.entities;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private Long userId; 
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public Cart(Long userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
    }

    // BR

    public void addItem(Product product, int quantity) {
        if (quantity <= 0) return;

        // Check tồn kho
        int currentQty = 0;
        CartItem existing = findItem(product.getId());
        if (existing != null) currentQty = existing.getQuantity();
        
        // Ném exception nếu không đủ hàng
        product.checkAvailability(currentQty + quantity);

        //  Thêm vào list
        if (existing != null) {
            existing.increaseQuantity(quantity);
        } else {
            if (items.size() >= 20) throw new RuntimeException("Giỏ hàng đã đầy (tối đa 20 loại)");
            items.add(new CartItem(product, quantity));
        }
    }

    public void updateItemQuantity(Long productId, int newQuantity) {
        CartItem item = findItem(productId);
        if (item == null) return;

        if (newQuantity <= 0) {
            removeItem(productId); 
        } else {
            // Nếu tăng số lượng, phải check kho
            if (newQuantity > item.getQuantity()) {
                item.getProduct().checkAvailability(newQuantity);
            }
            item.setQuantity(newQuantity);
        }
    }

    public void removeItem(Long productId) {
        items.removeIf(i -> i.getProduct().getId().equals(productId));
    }

    public double getTotalAmount() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public void clear() { items.clear(); }
    public boolean isEmpty() { return items.isEmpty(); }
    
    private CartItem findItem(Long productId) {
        return items.stream()
            .filter(i -> i.getProduct().getId().equals(productId))
            .findFirst().orElse(null);
    }


    public List<CartItem> getItems() { return new ArrayList<>(items); }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}