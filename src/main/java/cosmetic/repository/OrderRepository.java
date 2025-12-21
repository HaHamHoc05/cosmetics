package cosmetic.repository;

import java.util.List;

import cosmetic.entities.Cart;
import cosmetic.entities.Order;
import cosmetic.entities.OrderStatus;
import cosmetic.entities.Product;
import cosmetic.entities.User;

public interface OrderRepository {
	void save(Order order);
    List<Order> findAllByUserId(Long userId);
    Order findById(Long id);
    User findUserById(Long userId);
    Cart findCartByUserId(Long userId);
    void deleteCart(Long userId);
    
    void updateStatus(Long orderId, OrderStatus newStatus);
	List<Order> findAll();
	void update(Product product);

}