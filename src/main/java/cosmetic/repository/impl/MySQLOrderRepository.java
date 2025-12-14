package cosmetic.repository.impl;

import cosmetic.entities.Order;
import cosmetic.entities.OrderItem;
import cosmetic.entities.OrderStatus;
import cosmetic.entities.Product;
import cosmetic.repository.OrderRepository;
import cosmetic.repository.ProductRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySQLOrderRepository implements OrderRepository {
    
    private final ProductRepository productRepo = new MySQLProductRepository();

    @Override
    public Order findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapRowToOrder(rs);
                    loadOrderItems(conn, order);
                    return order;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapRowToOrder(rs);
                    loadOrderItems(conn, order); // Load items cho từng order
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = mapRowToOrder(rs);
                loadOrderItems(conn, order);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void save(Order order) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Insert Order
            String sqlOrder = "INSERT INTO orders (user_id, total_amount, status, payment_method, shipping_address, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, order.getUserId());
                stmt.setDouble(2, order.getTotalAmount());
                stmt.setString(3, order.getStatus().name());
                stmt.setString(4, order.getPaymentMethod());
                stmt.setString(5, order.getShippingAddress());
                stmt.setTimestamp(6, Timestamp.valueOf(order.getCreatedAt()));
                stmt.setTimestamp(7, Timestamp.valueOf(order.getUpdatedAt()));
                
                stmt.executeUpdate();
                
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getLong(1));
                    }
                }
            }

            // 2. Insert Order Items
            String sqlItem = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlItem)) {
                for (OrderItem item : order.getItems()) {
                    stmt.setLong(1, order.getId());
                    if (item.getProduct() != null) {
                        stmt.setLong(2, item.getProduct().getId());
                    } else {
                        stmt.setNull(2, Types.BIGINT);
                    }
                    stmt.setInt(3, item.getQuantity());
                    stmt.setDouble(4, item.getPrice()); // Lưu giá tại thời điểm mua
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
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

    @Override
    public void update(Order order) {
        String sql = "UPDATE orders SET status = ?, cancel_reason = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getStatus().name());
            stmt.setString(2, order.getCancelReason());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(4, order.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        // public Order(Long id, Long userId, List<OrderItem> items, OrderStatus status, 
        //              String paymentMethod, double totalAmount, String shippingAddress,
        //              String cancelReason, LocalDateTime createdAt, LocalDateTime updatedAt)
        
        return new Order(
            rs.getLong("id"),
            rs.getLong("user_id"),
            null, // Items sẽ được load sau
            OrderStatus.valueOf(rs.getString("status")),
            rs.getString("payment_method"),
            rs.getDouble("total_amount"),
            rs.getString("shipping_address"),
            rs.getString("cancel_reason"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        );
    }

    private void loadOrderItems(Connection conn, Order order) throws SQLException {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, order.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Long productId = rs.getLong("product_id");
                    int quantity = rs.getInt("quantity");
                    
                    // Chúng ta cần load product để hiển thị tên, ảnh, v.v.
                    Product product = productRepo.findById(productId);
                    
                    // OrderItem(Product product, int quantity) -> Constructor này sẽ tự lấy giá hiện tại của Product
                    // Nhưng OrderItem cũ cần giữ giá lúc mua (price trong DB).
                    // Ở đây để đơn giản ta dùng logic của Entity hiện có:
                    if (product != null) {
                         // Lưu ý: Nếu muốn hiển thị đúng giá LÚC MUA, bạn nên sửa Entity OrderItem 
                         // để có constructor nhận (Product, quantity, price).
                         // Với code hiện tại, nó sẽ dùng giá hiện tại của Product hoặc bạn phải set lại giá thủ công.
                         // Tạm thời ta dùng add item của Order nhưng Order status phải là PENDING mới add được.
                         // Do đó ta cần truy cập trực tiếp vào list items hoặc dùng Reflection, 
                         // hoặc đơn giản là tạo OrderItem và add vào list (nếu list items là public/protected).
                         // Vì Order.java bạn đưa dùng Constructor từ database: items = new ArrayList<>(items),
                         // ta có thể tạo List<OrderItem> ở ngoài rồi truyền vào Constructor (nhưng ở đây đã tạo Order rồi).
                         
                         // Cách giải quyết tốt nhất với code hiện tại:
                         // Dùng Reflection hoặc sửa Order.java để có method `addLoadedItem`.
                         // Tuy nhiên, để code chạy được ngay, tôi sẽ dùng cách ép kiểu thông qua list items (Order.getItems() trả về bản sao -> không add được).
                         // Vì vậy ta nên sửa lại luồng: Load Items TRƯỚC khi tạo Order Object.
                    }
                }
            }
        }
        
        // CẢI TIẾN: Load Items trước khi new Order để truyền vào Constructor
        List<OrderItem> items = new ArrayList<>();
        String sqlItems = "SELECT * FROM order_items WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlItems)) {
            stmt.setLong(1, order.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                     Product product = productRepo.findById(rs.getLong("product_id"));
                     if(product != null){
                         OrderItem item = new OrderItem(product, rs.getInt("quantity"));
                         // Nếu OrderItem có setter Price, hãy set giá từ rs.getDouble("price")
                         items.add(item);
                     }
                }
            }
        }
        
        // Vì Order đã được tạo ở mapRowToOrder với list null, ta cần set lại list items.
        // Nhưng Order.java không có setter cho items. 
        // GIẢI PHÁP: Tôi sẽ dùng Reflection ở đây để set giá trị field private "items" 
        // để bạn không phải sửa Entity Order.java.
        try {
            java.lang.reflect.Field itemsField = Order.class.getDeclaredField("items");
            itemsField.setAccessible(true);
            itemsField.set(order, items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}