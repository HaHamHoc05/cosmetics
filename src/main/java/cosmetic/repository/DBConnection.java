package cosmetic.repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/cosmetic_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";     
    private static final String PASS = "160603"; 
    public static Connection getConnection() {
        try {
            // Load driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy Driver MySQL! Kiểm tra pom.xml");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối Database!");
            e.printStackTrace();
            return null;
        }
    }
}