package cosmetic.repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Lưu ý: Nên đưa cấu hình này vào file properties hoặc biến môi trường trong môi trường Production
    private static final String URL = "jdbc:mysql://localhost:3306/cosmetic_db?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    private static final String USER = "root";     
    private static final String PASS = "160603"; 

    public static Connection getConnection() {
        try {
            // Load driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            // Ném lỗi runtime để dừng chương trình nếu thiếu driver
            throw new RuntimeException("Không tìm thấy Driver MySQL! Hãy kiểm tra dependency trong pom.xml", e);
        } catch (SQLException e) {
            // Ném lỗi runtime để báo lỗi kết nối database rõ ràng
            throw new RuntimeException("Lỗi kết nối Database! Vui lòng kiểm tra URL, User, Pass hoặc MySQL Server đang chạy.", e);
        }
    }
}