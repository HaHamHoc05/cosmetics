package cosmetic.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/beauty_ecommerce?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "160603";

    // ThreadLocal giúp giữ Connection riêng biệt cho từng luồng (User) đang chạy
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Không tìm thấy Driver MySQL!", e);
        }
    }

    // 1. Lấy kết nối: Nếu đang trong Transaction thì trả về kết nối chung, nếu không thì tạo mới
    public static Connection getConnection() throws SQLException {
        Connection conn = connectionHolder.get();
        if (conn == null || conn.isClosed()) {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conn;
    }

    // 2. Bắt đầu Transaction
    public static void startTransaction() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(false); // Tắt tự động lưu để quản lý thủ công
        connectionHolder.set(conn);
    }

    // 3. Commit (Lưu thành công)
    public static void commit() throws SQLException {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            conn.commit();
            conn.close();
            connectionHolder.remove(); // Xóa khỏi ThreadLocal
        }
    }

    // 4. Rollback (Hoàn tác khi lỗi)
    public static void rollback() {
        try {
            Connection conn = connectionHolder.get();
            if (conn != null) {
                conn.rollback();
                conn.close();
                connectionHolder.remove();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}