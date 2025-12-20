package cosmetic.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import cosmetic.entities.*;

public class MySQLOrderRepository implements OrderRepository {

    @Override
    public void save(Order order) {
        String sqlOrder = "INSERT INTO orders (user_id, total_amount, status, address, phone, payment_method, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            try {
                // 1. Lưu bảng orders
                PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
                psOrder.setLong(1, order.getUserId());
                psOrder.setDouble(2, order.getTotalAmount());
                psOrder.setString(3, order.getStatus().name());
                psOrder.setString(4, order.getShippingAddress());
                psOrder.setString(5, order.getPhone());
                psOrder.setString(6, order.getPaymentMethod());
                psOrder.setTimestamp(7, Timestamp.valueOf(order.getCreatedAt()));
                psOrder.executeUpdate();

                // Lấy ID vừa tạo
                ResultSet rs = psOrder.getGeneratedKeys();
                if (rs.next()) {
                    order.setId(rs.getLong(1));
                }

                // 2. Lưu bảng order_items
                PreparedStatement psItem = conn.prepareStatement(sqlItem);
                for (OrderItem item : order.getItems()) {
                    psItem.setLong(1, order.getId());
                    psItem.setLong(2, item.getProductId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setDouble(4, item.getPrice());
                    psItem.addBatch();
                }
                psItem.executeBatch();

                conn.commit(); // Xác nhận lưu

            } catch (SQLException e) {
                conn.rollback(); // Nếu lỗi thì hoàn tác
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findUserById(Long userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setUsername(rs.getString("username"));
                // Map thêm các field khác nếu cần
                return u;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Cart findCartByUserId(Long userId) {
        // Code demo đơn giản: Lấy Cart và CartItems
        // Thực tế bạn cần query bảng carts và cart_items
        Cart cart = new Cart();
        cart.setUserId(userId);
        
        String sql = "SELECT * FROM cart_items WHERE user_id = ?"; // Giả sử lưu trực tiếp user_id trong item hoặc join bảng
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            List<CartItem> items = new ArrayList<>();
            while(rs.next()) {
                CartItem item = new CartItem();
                item.setProductId(rs.getLong("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                items.add(item);
            }
            cart.setItems(items);
            if(items.isEmpty()) return null; // Giỏ hàng trống
            
            return cart;
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void deleteCart(Long userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Order> findAllByUserId(Long userId) {
        return new ArrayList<>(); // TODO: Tự implement tương tự các hàm trên
    }

    @Override
    public Order findById(Long id) {
        return null; // TODO: Tự implement tương tự các hàm trên
    }
}