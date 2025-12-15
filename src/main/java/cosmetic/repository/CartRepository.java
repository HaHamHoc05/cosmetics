package cosmetic.repository;

import cosmetic.entities.Cart;

public interface CartRepository {
    Cart findByUserId(Long userId);   
    void save(Cart cart);
    void deleteByUserId(Long userId);
}