package cosmetic.repository;

import cosmetic.entities.Order;
import java.util.List;

public interface OrderRepository {
    void save(Order order);
    Order findById(Long id);
    List<Order> findByUserId(Long userId); 
    List<Order> findAll(); 
}