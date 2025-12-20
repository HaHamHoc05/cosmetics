package cosmetic.repository;

import java.util.List;

import cosmetic.entities.Cart;
import cosmetic.usecase.cart.view.CartDetailDTO;

public interface CartRepository {
    Cart findByUserId(Long userId);
    Cart createCart(Long userId);
    void addItem(int cartId, Long productId, int quantity);
    int countItems(int cartId);
    
    List<CartDetailDTO> getCartDetails(int cartId);
    void updateItemQuantity(int cartId, Long productId, int quantity);
    void removeItem(int cartId, Long productId);
    void clearCart(int cartId);
}