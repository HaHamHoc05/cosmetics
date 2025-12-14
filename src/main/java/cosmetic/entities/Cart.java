package cosmetic.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.validation.ValidationException;

public class Cart {
	private Long userId;
	private List<CartItem>items;
	
	public Cart(Long userId) {
		if (userId == null) throw new IllegalArgumentException("UserId không được null");
		this.userId = userId;
		this.items = new ArrayList<>();
	}
	
	public Long getUserId() {
		return userId;
	}
	public List<CartItem>getItems(){
		return items;
	}
	
	//br
	public void addItem(Product product, int quantity) throws ValidationException {
        CartItem existingItem = findItemByProductId(product.getId());
        
        if (existingItem != null) {
            existingItem.increaseQuantity(quantity);
        } else {
            if (items.size() >= 50)
                throw new ValidationException("Giỏ hàng đã đạt tối đa 50 loại sản phẩm");
            items.add(new CartItem(product, quantity));
        }
    }

    public void updateItemQuantity(Long productId, int quantity) throws ValidationException {
        CartItem item = findItemByProductId(productId);
        if (item == null)
            throw new ValidationException("Sản phẩm không có trong giỏ hàng");
        
        if (quantity <= 0) {
            removeItem(productId);
        } else {
            item.setQuantity(quantity);
        }
    }

    public void removeItem(Long productId) throws ValidationException {
        boolean removed = items.removeIf(item -> item.isSameProduct(productId));
        if (!removed)
            throw new ValidationException("Sản phẩm không có trong giỏ hàng");
    }

    public CartItem findItemByProductId(Long productId) {
        return items.stream()
                .filter(item -> item.isSameProduct(productId))
                .findFirst()
                .orElse(null);
    }

    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }
}