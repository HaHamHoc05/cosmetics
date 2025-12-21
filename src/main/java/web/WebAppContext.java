package web;

import adapters.BCryptPasswordEncoder;
import cosmetic.entities.PasswordEncoder;
import cosmetic.repository.*;

public class WebAppContext {
    private static ProductRepository productRepo;
    private static UserRepository userRepo;
    private static CartRepository cartRepo;
    private static OrderRepository orderRepo;
    private static CategoryRepository categoryRepo;
    private static PasswordEncoder passwordEncoder;

    // Singleton Pattern: Đảm bảo chỉ khởi tạo 1 lần
    public static void init() {
        if (productRepo == null) {
            try {
                // Khởi tạo các kết nối database
                productRepo = new MySQLProductRepository();
                userRepo = new MySQLUserRepository();
                cartRepo = new MySQLCartRepository();
                orderRepo = new MySQLOrderRepository();
                categoryRepo = new MySQLCategoryRepository();
                passwordEncoder = new BCryptPasswordEncoder();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Getters
    public static ProductRepository getProductRepo() { return productRepo; }
    public static UserRepository getUserRepo() { return userRepo; }
    public static CartRepository getCartRepo() { return cartRepo; }
    public static OrderRepository getOrderRepo() { return orderRepo; }
    public static CategoryRepository getCategoryRepo() { return categoryRepo; }
    public static PasswordEncoder getPasswordEncoder() { return passwordEncoder; }
}