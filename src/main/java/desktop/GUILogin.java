package desktop;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import adapters.BCryptPasswordEncoder;
import adapters.Subscriber;
import adapters.user.login.*;
import adapters.user.register.*;
import cosmetic.repository.*;
import cosmetic.usecase.user.login.LoginUseCase;
import cosmetic.usecase.user.register.RegisterUseCase;
import cosmetic.entities.PasswordEncoder;

public class GUILogin extends JFrame implements Subscriber {
    private JTextField txtUser = new JTextField("admin");
    private JPasswordField txtPass = new JPasswordField("123456");
    private JButton btnLogin = StyleUtils.createButton("ĐĂNG NHẬP");
    private JButton btnRegister = new JButton("Chưa có tài khoản? Đăng ký ngay");

    private final LoginController controller;
    private final LoginViewModel viewModel;

    public GUILogin(LoginController controller, LoginViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addSubscriber(this); // Đã sửa trong LoginViewModel
        setupUI();
    }

    private void setupUI() {
        setTitle("Cosmetic Shop - Login");
        setSize(500, 450); // Tăng chiều cao một chút
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(StyleUtils.SECONDARY_COLOR);
        add(mainPanel);

        JPanel loginBox = new JPanel();
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setBackground(Color.WHITE);
        loginBox.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel lblTitle = new JLabel("WELCOME BACK");
        lblTitle.setFont(StyleUtils.FONT_TITLE);
        lblTitle.setForeground(StyleUtils.PRIMARY_COLOR);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUser.setFont(StyleUtils.FONT_REGULAR);
        txtPass.setFont(StyleUtils.FONT_REGULAR);
        
        // Thêm components
        loginBox.add(lblTitle);
        loginBox.add(Box.createVerticalStrut(20));
        
        JLabel lblUser = createLabel("Tên đăng nhập:");
        loginBox.add(lblUser);
        loginBox.add(Box.createVerticalStrut(5));
        loginBox.add(txtUser);
        
        loginBox.add(Box.createVerticalStrut(15));
        
        JLabel lblPass = createLabel("Mật khẩu:");
        loginBox.add(lblPass);
        loginBox.add(Box.createVerticalStrut(5));
        loginBox.add(txtPass);
        
        loginBox.add(Box.createVerticalStrut(25));
        
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(200, 40));
        loginBox.add(btnLogin);
        
        loginBox.add(Box.createVerticalStrut(15));
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setForeground(StyleUtils.PRIMARY_COLOR);
        btnRegister.setFont(StyleUtils.FONT_REGULAR);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBox.add(btnRegister);

        mainPanel.add(loginBox);

        btnLogin.addActionListener(e -> controller.execute(txtUser.getText(), new String(txtPass.getPassword())));
        btnRegister.addActionListener(e -> openRegisterScreen());
        
        // Enter key support
        getRootPane().setDefaultButton(btnLogin);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(StyleUtils.FONT_BOLD);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void openRegisterScreen() {
        try {
            UserRepository repo = new MySQLUserRepository();
            RegisterViewModel vm = new RegisterViewModel();
            PasswordEncoder encoder = new BCryptPasswordEncoder(); 
            RegisterPresenter p = new RegisterPresenter(vm);
            RegisterUseCase uc = new RegisterUseCase(repo,encoder, p);
            RegisterController c = new RegisterController(uc);
            
            // Giả định GUIRegister có constructor này
            new GUIRegister(c, vm, this).setVisible(true);
            this.setVisible(false);
        } catch(Exception ex) { 
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Chưa cài đặt màn hình đăng ký: " + ex.getMessage());
        }
    }

    @Override
    public void update() {
        if (viewModel.isSuccess) {
            this.dispose();
            openMainApp(viewModel.userId, viewModel.role);
        } else {
            JOptionPane.showMessageDialog(this, viewModel.message, "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openMainApp(Long userId, String role) {
        try {
            // 1. Khởi tạo Repositories
            ProductRepository prodRepo = new MySQLProductRepository();
            CartRepository cartRepo = new MySQLCartRepository();
            OrderRepository orderRepo = new MySQLOrderRepository();
            CategoryRepository catRepo = new MySQLCategoryRepository();

            // 2. Wiring Use Cases & Controllers
            
            // Product List
            var listVM = new adapters.product.getlist.GetListProductViewModel();
            var listPres = new adapters.product.getlist.GetListProductPresenter(listVM);
            var listUC = new cosmetic.usecase.products.getlist.GetListProductUseCase(prodRepo, listPres);
            var listCtrl = new adapters.product.getlist.GetListProductController(listUC);

            // Add to Cart
            var addVM = new adapters.cart.add.AddToCartViewModel();
            var addPres = new adapters.cart.add.AddToCartPresenter(addVM);
            var addUC = new cosmetic.usecase.cart.add.AddToCartUseCase(cartRepo, prodRepo, addPres);
            var addCtrl = new adapters.cart.add.AddToCartController(addUC);

            // View Cart
            var viewCartVM = new adapters.cart.view.ViewCartViewModel();
            var viewCartPres = new adapters.cart.view.ViewCartPresenter(viewCartVM);
            var viewCartUC = new cosmetic.usecase.cart.view.ViewCartUseCase(cartRepo, viewCartPres);
            var viewCartCtrl = new adapters.cart.view.ViewCartController(viewCartUC);

            // Create Order
            var orderVM = new adapters.order.create.CreateOrderViewModel();
            var orderPres = new adapters.order.create.CreateOrderPresenter(orderVM);
            var orderUC = new cosmetic.usecase.order.create.CreateOrderUseCase(orderRepo, cartRepo, prodRepo, orderPres);
            var orderCtrl = new adapters.order.create.CreateOrderController(orderUC);

            // Order History
            var historyVM = new adapters.order.getlist.GetListViewModel();
            var historyPres = new adapters.order.getlist.GetListPresenter(historyVM);
            var historyUC = new cosmetic.usecase.order.getlist.GetListUseCase(orderRepo, historyPres);
            var historyCtrl = new adapters.order.getlist.GetListController(historyUC);

            // Create Product (Admin)
            var createProdVM = new adapters.product.create.CreateProductViewModel();
            var createProdPres = new adapters.product.create.CreateProductPresenter(createProdVM);
            var createProdUC = new cosmetic.usecase.product.create.CreateProductUseCase(prodRepo, catRepo, createProdPres);
            var createProdCtrl = new adapters.product.create.CreateProductController(createProdUC);

            // Gom nhóm dependencies phụ để truyền gọn hơn
            Object[] extraDeps = {orderCtrl, orderVM, historyCtrl, historyVM, createProdCtrl, createProdVM};

            new GUIProductList(userId, role, listCtrl, listVM, addCtrl, addVM, viewCartCtrl, viewCartVM, extraDeps).setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khởi tạo ứng dụng: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                UserRepository repo = new MySQLUserRepository();
                LoginViewModel vm = new LoginViewModel();
                LoginPresenter p = new LoginPresenter(vm);
                
                // Sử dụng BCrypt thật
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                
                LoginUseCase uc = new LoginUseCase(repo, encoder, p);
                LoginController c = new LoginController(uc);
                new GUILogin(c, vm).setVisible(true);
            } catch (Exception e) { 
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi Database: " + e.getMessage());
            }
        });
    }
}