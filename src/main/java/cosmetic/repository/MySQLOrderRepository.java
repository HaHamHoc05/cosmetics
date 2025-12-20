package cosmetic.repository;

import cosmetic.entities.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLOrderRepository implements OrderRepository {

    @Override
    public void save(Order order) {
        String sqlOrder = "INSERT INTO orders (user_id, total_price, status, shipping_address, shipping_phone, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Lưu bảng Orders
            PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setLong(1, order.getUserId());
            psOrder.setDouble(2, order.getTotalAmount());
            psOrder.setString(3, order.getStatus().toString());
            psOrder.setString(4, order.getShippingAddress());
            psOrder.setString(5, order.getPhone());
            psOrder.setTimestamp(6, Timestamp.valueOf(order.getCreatedAt()));
            psOrder.executeUpdate();

            // Lấy ID vừa sinh ra
            ResultSet rs = psOrder.getGeneratedKeys();
            if (rs.next()) {
                order.setId(rs.getLong(1));
            }

            // 2. Lưu bảng OrderItems
            PreparedStatement psItem = conn.prepareStatement(sqlItem);
            for (OrderItem item : order.getItems()) {
                psItem.setLong(1, order.getId());
                psItem.setLong(2, item.getProductId());
                psItem.setInt(3, item.getQuantity());
                psItem.setDouble(4, item.getPrice());
                psItem.addBatch(); // Gom lại chạy 1 lần cho nhanh
            }
            psItem.executeBatch();

            conn.commit(); // Xác nhận lưu thành công
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Lỗi khi lưu đơn hàng: " + e.getMessage());
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getStatus().toString());
            ps.setLong(2, order.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Order findById(Long id) {
        Order order = null;
        String sqlOrder = "SELECT * FROM orders WHERE id = ?";
        String sqlItems = "SELECT * FROM order_items WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            // 1. Lấy thông tin Order
            PreparedStatement ps = conn.prepareStatement(sqlOrder);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                order = mapRowToOrder(rs);
            }

            // 2. Nếu có order, lấy tiếp danh sách Items
            if (order != null) {
                PreparedStatement psItem = conn.prepareStatement(sqlItems);
                psItem.setLong(1, id);
                ResultSet rsItem = psItem.executeQuery();
                List<OrderItem> items = new ArrayList<>();
                while (rsItem.next()) {
                    OrderItem item = new OrderItem(
                        rsItem.getLong("product_id"),
                        rsItem.getInt("quantity"),
                        rsItem.getDouble("price")
                    );
                    items.add(item);
                }
                order.setItems(items);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- Các hàm hỗ trợ Cart & User ---
    
    @Override
    public Cart findCartByUserId(Long userId) {
        // Cần JOIN với bảng products để lấy giá hiện tại
        String sql = "SELECT ci.product_id, ci.quantity, p.price, p.name " +
                     "FROM carts c " +
                     "JOIN cart_items ci ON c.id = ci.cart_id " +
                     "JOIN products p ON ci.product_id = p.id " +
                     "WHERE c.user_id = ?";
        
        Cart cart = new Cart();
        cart.setUserId(userId);
        List<CartItem> items = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Giả sử constructor CartItem(productId, quantity, price)
                // Bạn có thể cần điều chỉnh constructor CartItem hoặc Product bên trong Entity
                CartItem item = new CartItem(); 
                item.setProductId(rs.getLong("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                // Lưu ý: CartItem thường nên giữ thông tin Product để CreateOrder lấy được giá
                // Ở đây tôi giả định bạn set tạm giá vào item hoặc logic tương đương
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Mock tạm dữ liệu nếu Cart chưa có logic product thật
        if(items.isEmpty()) { 
             // Logic thực tế: Return null hoặc empty cart
        }
        
        return cart; // Cần hoàn thiện mapping CartItem dựa trên Entity của bạn
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
                user.setEmail(rs.getString("email"));
                // ... map các trường khác
                return user;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void deleteCart(Long userId) {
        String sql = "DELETE ci FROM cart_items ci JOIN carts c ON ci.cart_id = c.id WHERE c.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Helper mapping
    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));
        order.setTotalAmount(rs.getDouble("total_price"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setPhone(rs.getString("shipping_phone"));
        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        order.setStatus(OrderStatus.valueOf(rs.getString("status"))); // Enum mapping
        return order;
    }
    
    private void closeConnection(Connection conn) {
        if(conn != null) {
             try { conn.close(); } catch(SQLException e) {}
        }
    }
}