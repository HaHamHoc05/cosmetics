package desktop;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import adapters.Subscriber;
import adapters.user.login.*;
import adapters.user.register.*;
import cosmetic.repository.*;
import cosmetic.usecase.user.login.LoginUseCase;
import cosmetic.usecase.user.register.RegisterUseCase;
import cosmetic.entities.PasswordEncoder;
import adapters.*;

/**
 * Màn hình đăng nhập với giao diện hiện đại
 */
public class GUILogin extends JFrame implements Subscriber {
    // UI Components
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private JCheckBox chkRemember;
    private JLabel lblStatus;
    
    // Dependencies
    private final LoginController controller;
    private final LoginViewModel viewModel;

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(220, 53, 69);
    private static final Color SECONDARY_COLOR = new Color(108, 117, 125);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color WHITE = Color.WHITE;

    public GUILogin(LoginController controller, LoginViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.subscribe(this);
        
        initComponents();
        setupUI();
        addListeners();
    }

    private void initComponents() {
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        btnLogin = new JButton("Đăng Nhập");
        btnRegister = new JButton("Đăng Ký");
        chkRemember = new JCheckBox("Ghi nhớ đăng nhập");
        lblStatus = new JLabel(" ");
        
        // Styling
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
        
        txtUsername.setFont(mainFont);
        txtPassword.setFont(mainFont);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setFont(mainFont);
        chkRemember.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        
        // Button styling
        btnLogin.setBackground(PRIMARY_COLOR);
        btnLogin.setForeground(WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnRegister.setBackground(SECONDARY_COLOR);
        btnRegister.setForeground(WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        lblStatus.setForeground(PRIMARY_COLOR);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setupUI() {
        setTitle("Cosmetic Shop - Đăng Nhập");
        setSize(450, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Form panel
        JPanel formPanel = createFormPanel();
        
        // Footer panel
        JPanel footerPanel = createFooterPanel();
        
        // Add to main
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Icon/Logo
        JLabel lblIcon = new JLabel("💄");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        JLabel lblTitle = new JLabel("Cosmetic Shop");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(PRIMARY_COLOR);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel lblSubtitle = new JLabel("Đăng nhập để tiếp tục");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(SECONDARY_COLOR);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblIcon);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblSubtitle);
        
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(222, 226, 230), 1, true),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Username field
        panel.add(createFieldPanel("👤 Tên đăng nhập", txtUsername));
        panel.add(Box.createVerticalStrut(20));
        
        // Password field
        panel.add(createFieldPanel("🔒 Mật khẩu", txtPassword));
        panel.add(Box.createVerticalStrut(15));
        
        // Remember checkbox
        chkRemember.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkRemember.setBackground(WHITE);
        panel.add(chkRemember);
        panel.add(Box.createVerticalStrut(20));
        
        // Status label
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblStatus);
        panel.add(Box.createVerticalStrut(10));
        
        // Login button
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setPreferredSize(new Dimension(300, 40));
        btnLogin.setMaximumSize(new Dimension(300, 40));
        panel.add(btnLogin);
        
        return panel;
    }

    private JPanel createFieldPanel(String label, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(SECONDARY_COLOR);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(206, 212, 218), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        
        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Divider
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(20));
        
        // Register section
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel lblNewUser = new JLabel("Chưa có tài khoản?");
        lblNewUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblNewUser.setForeground(SECONDARY_COLOR);
        
        btnRegister.setPreferredSize(new Dimension(100, 30));
        
        registerPanel.add(lblNewUser);
        registerPanel.add(btnRegister);
        
        panel.add(registerPanel);
        
        return panel;
    }

    private void addListeners() {
        btnLogin.addActionListener(e -> handleLogin());
        
        btnRegister.addActionListener(e -> handleRegister());
        
        // Enter key to login
        txtPassword.addActionListener(e -> handleLogin());
        txtUsername.addActionListener(e -> txtPassword.requestFocus());
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty()) {
            showStatus("Vui lòng nhập tên đăng nhập", false);
            return;
        }
        
        if (password.isEmpty()) {
            showStatus("Vui lòng nhập mật khẩu", false);
            return;
        }
        
        btnLogin.setEnabled(false);
        btnLogin.setText("Đang đăng nhập...");
        showStatus("Đang xác thực...", true);
        
        controller.execute(username, password);
    }

    private void handleRegister() {
        try {
            UserRepository repo = new MySQLUserRepository();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            RegisterViewModel vm = new RegisterViewModel();
            RegisterPresenter pres = new RegisterPresenter(vm);
            RegisterUseCase uc = new RegisterUseCase(repo, encoder, pres);
            RegisterController ctrl = new RegisterController(uc);
            
            new GUIRegister(ctrl, vm, this).setVisible(true);
            this.setVisible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            showStatus("Lỗi mở màn hình đăng ký", false);
        }
    }

    private void showStatus(String message, boolean isLoading) {
        lblStatus.setText(message);
        lblStatus.setForeground(isLoading ? SECONDARY_COLOR : PRIMARY_COLOR);
    }

    @Override
    public void update() {
        btnLogin.setEnabled(true);
        btnLogin.setText("Đăng Nhập");
        
        if (viewModel.isSuccess) {
            showStatus("✓ Đăng nhập thành công!", true);
            
            // Delay để người dùng thấy thông báo thành công
            Timer timer = new Timer(500, e -> {
                this.dispose();
                openMainApp(viewModel.userId, viewModel.role);
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            showStatus("✗ " + viewModel.message, false);
        }
    }

    private void openMainApp(Long userId, String role) {
        try {
            // Initialize repositories
            ProductRepository prodRepo = new MySQLProductRepository();
            CartRepository cartRepo = new MySQLCartRepository();
            OrderRepository orderRepo = new MySQLOrderRepository();
            CategoryRepository catRepo = new MySQLCategoryRepository();

            // Product list dependencies
            var listVM = new adapters.product.getlist.GetListProductViewModel();
            var listPres = new adapters.product.getlist.GetListProductPresenter(listVM);
            var listUC = new cosmetic.usecase.products.getlist.GetListProductUseCase(prodRepo, listPres);
            var listCtrl = new adapters.product.getlist.GetListProductController(listUC);

            // Add to cart dependencies
            var addVM = new adapters.cart.add.AddToCartViewModel();
            var addPres = new adapters.cart.add.AddToCartPresenter(addVM);
            var addUC = new cosmetic.usecase.cart.add.AddToCartUseCase(cartRepo, prodRepo, addPres);
            var addCtrl = new adapters.cart.add.AddToCartController(addUC);

            // View cart dependencies
            var viewCartVM = new adapters.cart.view.ViewCartViewModel();
            var viewCartPres = new adapters.cart.view.ViewCartPresenter(viewCartVM);
            var viewCartUC = new cosmetic.usecase.cart.view.ViewCartUseCase(cartRepo, viewCartPres);
            var viewCartCtrl = new adapters.cart.view.ViewCartController(viewCartUC);

            // Create order dependencies
            var createOrderVM = new adapters.order.create.CreateOrderViewModel();
            var createOrderPres = new adapters.order.create.CreateOrderPresenter(createOrderVM);
            var createOrderUC = new cosmetic.usecase.order.create.CreateOrderUseCase(orderRepo, prodRepo, createOrderPres);
            var createOrderCtrl = new adapters.order.create.CreateOrderController(createOrderUC);

            // Order history dependencies
            var historyVM = new adapters.order.getlist.GetListViewModel();
            var historyPres = new adapters.order.getlist.GetListPresenter(historyVM);
            var historyUC = new cosmetic.usecase.order.getlist.GetListUseCase(orderRepo, historyPres);
            var historyCtrl = new adapters.order.getlist.GetListController(historyUC);

            // Admin create product dependencies
            var createProdVM = new adapters.product.create.CreateProductViewModel();
            var createProdPres = new adapters.product.create.CreateProductPresenter(createProdVM);
            var createProdUC = new cosmetic.usecase.product.create.CreateProductUseCase(prodRepo, createProdPres);
            var createProdCtrl = new adapters.product.create.CreateProductController(createProdUC);

            Object[] extraDeps = {
                createOrderCtrl, createOrderVM,
                historyCtrl, historyVM,
                createProdCtrl, createProdVM
            };

            new GUIProductList(userId, role, listCtrl, listVM, addCtrl, addVM, 
                             viewCartCtrl, viewCartVM, extraDeps).setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khởi tạo ứng dụng: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                UserRepository repo = new MySQLUserRepository();
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                LoginViewModel vm = new LoginViewModel();
                LoginPresenter pres = new LoginPresenter(vm);
                LoginUseCase uc = new LoginUseCase(repo, encoder, pres);
                LoginController ctrl = new LoginController(uc);
                
                new GUILogin(ctrl, vm).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Không thể kết nối database. Vui lòng kiểm tra:\n" +
                    "1. MySQL Server đang chạy\n" +
                    "2. Database 'cosmetic_db' đã được tạo\n" +
                    "3. Thông tin kết nối trong DBConnection.java",
                    "Lỗi Kết Nối", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}