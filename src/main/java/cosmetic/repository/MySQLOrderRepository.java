package cosmetic.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import cosmetic.entities.*;

public class MySQLOrderRepository implements OrderRepository {

    @Override
    public void save(Order order) {
        String sql = "INSERT INTO orders (user_id, total_amount, status, address, phone, payment_method, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setLong(1, order.getUserId());
            ps.setDouble(2, order.getTotalAmount());
            ps.setString(3, order.getStatus().name());
            ps.setString(4, order.getShippingAddress());
            ps.setString(5, order.getPhone());
            ps.setString(6, order.getPaymentMethod());
            ps.setTimestamp(7, Timestamp.valueOf(order.getCreatedAt()));
            
            ps.executeUpdate();
            
            // Lấy ID vừa tạo
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                order.setId(rs.getLong(1));
            }
            
            // Lưu OrderItems
            saveOrderItems(order);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lưu đơn hàng.");
        }
    }
    
    private void saveOrderItems(Order order) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            for (OrderItem item : order.getItems()) {
                ps.setLong(1, order.getId());
                ps.setLong(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                ps.setDouble(4, item.getPrice());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public List<Order> findAllByUserId(Long userId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getLong("id"));
                o.setUserId(rs.getLong("user_id"));
                o.setTotalAmount(rs.getDouble("total_amount"));
                o.setShippingAddress(rs.getString("address"));
                o.setPhone(rs.getString("phone"));
                o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                String statusStr = rs.getString("status");
                o.setStatus(statusStr != null ? OrderStatus.valueOf(statusStr) : OrderStatus.PENDING);
                
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    

    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getLong("id"));
                o.setUserId(rs.getLong("user_id"));
                o.setTotalAmount(rs.getDouble("total_amount"));
                o.setShippingAddress(rs.getString("address"));
                o.setPhone(rs.getString("phone"));
                o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                String statusStr = rs.getString("status");
                o.setStatus(statusStr != null ? OrderStatus.valueOf(statusStr) : OrderStatus.PENDING);
                
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Order findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Order o = new Order();
                o.setId(rs.getLong("id"));
                o.setUserId(rs.getLong("user_id"));
                o.setStatus(OrderStatus.valueOf(rs.getString("status")));
                o.setTotalAmount(rs.getDouble("total_amount"));
                o.setShippingAddress(rs.getString("address"));
                o.setPhone(rs.getString("phone"));
                o.setPaymentMethod(rs.getString("payment_method"));
                o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                // Load OrderItems
                o.setItems(findOrderItems(id));
                
                return o;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private List<OrderItem> findOrderItems(Long orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, orderId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getLong("id"));
                item.setOrderId(orderId);
                item.setProductId(rs.getLong("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                items.add(item);
            }
        }
        return items;
    }
    
    @Override
    public void updateStatus(Long orderId, OrderStatus newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newStatus.name());
            ps.setLong(2, orderId);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi cập nhật trạng thái đơn hàng.");
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
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFullName(rs.getString("full_name"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setRoleId(rs.getLong("role_id"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public Cart findCartByUserId(Long userId) {
        String sql = "SELECT * FROM carts WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Cart cart = new Cart();
                cart.setId(rs.getInt("id"));
                cart.setUserId(userId);
                
                // Load CartItems
                cart.setItems(findCartItems(cart.getId()));
                return cart;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private List<CartItem> findCartItems(int cartId) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT * FROM cart_items WHERE cart_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setId(rs.getLong("id"));
                item.setCartId((long) cartId);
                item.setProductId(rs.getLong("product_id"));
                item.setProductName(rs.getString("product_name"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                items.add(item);
            }
        }
        return items;
    }
    
    @Override
    public void deleteCart(Long userId) {
        String sql = "DELETE FROM carts WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, userId);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}