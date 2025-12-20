package cosmetic.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cosmetic.entities.Product;
import cosmetic.entities.ProductStatus;

public class MySQLProductRepository implements ProductRepository {

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM products WHERE status = 'ACTIVE'";
        List<Product> list = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToProduct(rs));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    @Override
    public Product findById(Long id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToProduct(rs);
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return null;
    }

    @Override
    public List<Product> searchByName(String keyword) {
        String sql = "SELECT * FROM products WHERE name LIKE ? AND status = 'ACTIVE'";
        List<Product> list = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + keyword + "%");
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToProduct(rs));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    @Override
    public List<Product> findByCategory(Long categoryId) {
        String sql = "SELECT * FROM products WHERE category_id = ? AND status = 'ACTIVE'";
        List<Product> list = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToProduct(rs));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    @Override
    public void updateQuantity(Long productId, int newQuantity) {
        String sql = "UPDATE products SET quantity = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, newQuantity);
            ps.setLong(2, productId);
            ps.executeUpdate();
            
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }

    // Helper: Map dữ liệu từ SQL sang Entity
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("name"));
        p.setPrice(rs.getDouble("price"));
        p.setQuantity(rs.getInt("quantity"));
        p.setDescription(rs.getString("description"));
        p.setImageUrl(rs.getString("image_url"));
        
        // SỬA: Bổ sung ánh xạ category_id
        p.setCategoryId(rs.getLong("category_id"));
        
        // Map Status an toàn hơn
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            try {
                p.setStatus(ProductStatus.valueOf(statusStr));
            } catch (IllegalArgumentException e) {
                // Nếu DB lưu giá trị lạ, mặc định về ACTIVE hoặc log warning
                p.setStatus(ProductStatus.ACTIVE); 
            }
        } else {
             p.setStatus(ProductStatus.ACTIVE);
        }
        
        return p;
    }
}