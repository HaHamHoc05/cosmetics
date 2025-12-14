package cosmetic.repository.impl;

import cosmetic.entities.Cart;
import cosmetic.entities.CartItem;
import cosmetic.entities.Product;
import cosmetic.repository.CartRepository;
import cosmetic.repository.ProductRepository;

import java.sql.*;

public class MySQLCartRepository implements CartRepository {
    // Cần ProductRepo để load lại thông tin sản phẩm khi đọc từ DB
    private final ProductRepository productRepo = new MySQLProductRepository();

    @Override
    public Cart findByUserId(Long userId) {
        Cart cart = null;
        
        // 1. Tìm Cart ID từ bảng carts
        String sqlCart = "SELECT id FROM carts WHERE user_id = ?";
        Long cartId = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlCart)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cartId = rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        if (cartId != null) {
            cart = new Cart(userId);
            // 2. Load các items
            String sqlItems = "SELECT * FROM cart_items WHERE cart_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sqlItems)) {
                stmt.setLong(1, cartId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Long productId = rs.getLong("product_id");
                        int quantity = rs.getInt("quantity");
                        Product product = productRepo.findById(productId);
                        if (product != null) {
                            try {
                                cart.addItem(product, quantity);
                            } catch (Exception e) {
                                // Bỏ qua lỗi validation khi load từ DB
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return cart;
    }

    @Override
    public void save(Cart cart) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Kiểm tra hoặc tạo Cart trong bảng `carts`
            Long cartId = getCartIdByUserId(conn, cart.getUserId());
            if (cartId == null) {
                String insertCart = "INSERT INTO carts (user_id) VALUES (?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertCart, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setLong(1, cart.getUserId());
                    stmt.executeUpdate();
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) cartId = rs.getLong(1);
                    }
                }
            } else {
                // Update timestamp
                String updateCart = "UPDATE carts SET updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateCart)) {
                    stmt.setLong(1, cartId);
                    stmt.executeUpdate();
                }
            }

            // 2. Xóa hết item cũ của cart này (Strategy đơn giản: Delete All -> Insert New)
            String deleteItems = "DELETE FROM cart_items WHERE cart_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteItems)) {
                stmt.setLong(1, cartId);
                stmt.executeUpdate();
            }

            // 3. Insert các item hiện tại
            String insertItem = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertItem)) {
                for (CartItem item : cart.getItems()) {
                    stmt.setLong(1, cartId);
                    stmt.setLong(2, item.getProduct().getId());
                    stmt.setInt(3, item.getQuantity());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit(); // Hoàn tất Transaction
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    private Long getCartIdByUserId(Connection conn, Long userId) throws SQLException {
        String sql = "SELECT id FROM carts WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getLong("id");
            }
        }
        return null;
    }
}