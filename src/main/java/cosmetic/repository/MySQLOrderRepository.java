package cosmetic.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import cosmetic.entities.*;

public class MySQLOrderRepository implements OrderRepository {

    // ... (Giữ nguyên hàm save, findUserById, findCartByUserId, deleteCart đã sửa ở câu trả lời trước) ...
    // NẾU BẠN CHƯA CÓ, HÃY COPY LẠI TỪ CÂU TRẢ LỜI TRƯỚC CHO ĐẦY ĐỦ

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
                // Các field khác nếu cần dùng cho validation
                return o;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Bạn cần thêm method này vào interface OrderRepository trước
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
    
    // CÁC HÀM save, deleteCart, findCart... GIỮ NGUYÊN NHƯ LẦN TRƯỚC
    @Override
    public void save(Order order) { /* ... Code cũ ... */ }
    @Override
    public User findUserById(Long userId) { /* ... Code cũ ... */ return null; }
    @Override
    public Cart findCartByUserId(Long userId) { /* ... Code cũ ... */ return null; }
    @Override
    public void deleteCart(Long userId) { /* ... Code cũ ... */ }
}