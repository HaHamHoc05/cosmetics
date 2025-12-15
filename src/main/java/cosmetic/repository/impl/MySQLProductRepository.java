package cosmetic.repository.impl;

import cosmetic.entities.Category;
import cosmetic.entities.Product;
import cosmetic.repository.ProductRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLProductRepository implements ProductRepository {

    private static final String BASE_SELECT = 
        "SELECT p.*, c.name as cat_name, c.description as cat_desc " +
        "FROM products p " +
        "LEFT JOIN categories c ON p.category_id = c.id ";

    // MỚI: Implement hàm trừ kho an toàn
    @Override
    public boolean reduceStockAtomic(Long productId, int quantity) {
        // Chỉ cập nhật nếu stock >= quantity (tránh âm kho)
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, quantity);
                stmt.setLong(2, productId);
                stmt.setInt(3, quantity);
                
                int rowsUpdated = stmt.executeUpdate();
                return rowsUpdated > 0; // Trả về true nếu trừ thành công
            }
            // Lưu ý: Không đóng 'conn' ở đây vì nó được quản lý bởi DatabaseConnection (Transaction)
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi DB khi trừ kho: " + e.getMessage());
        }
    }

    @Override
    public Product findById(Long id) {
        String sql = BASE_SELECT + "WHERE p.id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) return mapRowToProduct(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY p.id DESC";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) products.add(mapRowToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> findByKeyword(String keyword) {
        return search(keyword, null);
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return search(null, categoryId);
    }

    @Override
    public List<Product> search(String keyword, Long categoryId) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT + "WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
            String kw = "%" + keyword.toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
        }

        if (categoryId != null) {
            sql.append("AND p.category_id = ? ");
            params.add(categoryId);
        }

        sql.append("ORDER BY p.id DESC");

        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) products.add(mapRowToProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public void save(Product product) {
        String sql = "INSERT INTO products (name, description, price, stock, category_id, status) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, product.getName());
                stmt.setString(2, product.getDescription());
                stmt.setDouble(3, product.getPrice());
                stmt.setInt(4, product.getStock());
                if (product.getCategory() != null) stmt.setLong(5, product.getCategory().getId());
                else stmt.setNull(5, Types.BIGINT);
                stmt.setString(6, product.getStatus().name());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products SET name=?, description=?, price=?, stock=?, category_id=?, status=? WHERE id=?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, product.getName());
                stmt.setString(2, product.getDescription());
                stmt.setDouble(3, product.getPrice());
                stmt.setInt(4, product.getStock());
                if (product.getCategory() != null) stmt.setLong(5, product.getCategory().getId());
                else stmt.setNull(5, Types.BIGINT);
                stmt.setString(6, product.getStatus().name());
                stmt.setLong(7, product.getId());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
    }

    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String desc = rs.getString("description");
        double price = rs.getDouble("price");
        int stock = rs.getInt("stock");
        
        Category category = null;
        Long catId = rs.getLong("category_id");
        if (!rs.wasNull()) {
            category = new Category(rs.getString("cat_name"), rs.getString("cat_desc"));
            category.setId(catId);
        }
        return new Product(id, name, desc, price, stock, category);
    }
}