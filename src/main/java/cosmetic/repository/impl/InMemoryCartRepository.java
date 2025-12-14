package cosmetic.repository.impl;

import cosmetic.entities.Cart;
import cosmetic.repository.CartRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCartRepository implements CartRepository {
    private final Map<Long, Cart> carts = new ConcurrentHashMap<>();

    @Override
    public Cart findByUserId(Long userId) {
        return carts.get(userId);
    }

    @Override
    public void save(Cart cart) {
        carts.put(cart.getUserId(), cart);
    }
}