package cosmetic.repository;

import cosmetic.entities.Cart;
import cosmetic.entities.CartItem;
import cosmetic.usecase.cart.view.CartDetailDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLCartRepository implements CartRepository {

    @Override
    public Cart findByUserId(Long userId) {
        Cart cart = null;
        String sqlCart = "SELECT * FROM carts WHERE user_id = ?";
        String sqlItems = "SELECT * FROM cart_items WHERE cart_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psCart = conn.prepareStatement(sqlCart)) {
            
            psCart.setLong(1, userId);
            ResultSet rs = psCart.executeQuery();
            
            if (rs.next()) {
                cart = new Cart();
                cart.setId(rs.getInt("id"));
                cart.setUserId(userId);
                
                // Lấy items
                try (PreparedStatement psItems = conn.prepareStatement(sqlItems)) {
                    psItems.setInt(1, cart.getId());
                    ResultSet rsItems = psItems.executeQuery();
                    while (rsItems.next()) {
                        CartItem item = new CartItem();
                        item.setProductId(rsItems.getLong("product_id"));
                        item.setQuantity(rsItems.getInt("quantity"));
                        // Có thể cần join để lấy tên sản phẩm, giá... ở đây
                        cart.getItems().add(item);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

    @Override
    public Cart createCart(Long userId) {
        String sql = "INSERT INTO carts (user_id, created_at) VALUES (?, NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setLong(1, userId);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Cart cart = new Cart();
                cart.setId(rs.getInt(1));
                cart.setUserId(userId);
                return cart;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addItem(int cartId, Long productId, int quantity) {
        // Upsert: Nếu chưa có thì thêm, có rồi thì cộng dồn số lượng
        String sql = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cartId);
            ps.setLong(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi thêm vào giỏ: " + e.getMessage());
        }
    }

    @Override
    public int countItems(int cartId) {
        String sql = "SELECT SUM(quantity) FROM cart_items WHERE cart_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public List<CartDetailDTO> getCartDetails(int cartId) {
        List<CartDetailDTO> list = new ArrayList<>();
        // Join bảng cart_items với products để lấy tên và giá
        String sql = "SELECT p.id, p.name, p.price, p.image, ci.quantity " +
                     "FROM cart_items ci " +
                     "JOIN products p ON ci.product_id = p.id " +
                     "WHERE ci.cart_id = ?";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartDetailDTO dto = new CartDetailDTO();
                dto.productId = rs.getLong("id");
                dto.productName = rs.getString("name");
                dto.price = rs.getBigDecimal("price"); // Giả sử DB dùng DECIMAL
                dto.image = rs.getString("image");
                dto.quantity = rs.getInt("quantity");
                // Tính thành tiền luôn cho tiện
                dto.totalPrice = dto.price.multiply(java.math.BigDecimal.valueOf(dto.quantity));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void updateItemQuantity(int cartId, Long productId, int quantity) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE cart_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartId);
            ps.setLong(3, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi cập nhật số lượng: " + e.getMessage());
        }
    }

    @Override
    public void removeItem(int cartId, Long productId) {
        String sql = "DELETE FROM cart_items WHERE cart_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setLong(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi xóa sản phẩm: " + e.getMessage());
        }
    }
    
    @Override
    public void clearCart(int cartId) {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}