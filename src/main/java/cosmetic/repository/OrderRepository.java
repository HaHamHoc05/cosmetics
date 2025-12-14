package cosmetic.repository;

import java.util.List;

import cosmetic.entities.Order;

public interface OrderRepository {
	Order findById(Long id);
    List<Order> findByUserId(Long userId);
    List<Order> findAll();
    void save(Order order);
    void update(Order order);
}