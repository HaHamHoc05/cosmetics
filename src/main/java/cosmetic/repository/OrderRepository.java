package cosmetic.repository;

import java.util.List;

import cosmetic.entities.Cart;
import cosmetic.entities.Order;
import cosmetic.entities.User;

public interface OrderRepository {
    void save(Order order);
    Order findById(Long id);
    List<Order> findByUserId(Long userId); // Cho lịch sử đơn
    List<Order> findAll();                 // Cho Admin quản lý
    Cart findCartByUserId(Long userId);
    void deleteCart(Long userId);
    User findUserById(Long userId);
}