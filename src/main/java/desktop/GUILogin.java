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
        this.viewModel.addSubscriber(this);
        setupUI();
    }

    private void setupUI() {
        setTitle("Cosmetic Shop - Login");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main Panel với màu nền
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(StyleUtils.SECONDARY_COLOR);
        add(mainPanel);

        // Box đăng nhập trắng
        JPanel loginBox = new JPanel();
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setBackground(Color.WHITE);
        loginBox.setBorder(new EmptyBorder(30, 40, 30, 40)); // Padding

        // Logo/Title
        JLabel lblTitle = new JLabel("WELCOME BACK");
        lblTitle.setFont(StyleUtils.FONT_TITLE);
        lblTitle.setForeground(StyleUtils.PRIMARY_COLOR);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Inputs
        txtUser.setFont(StyleUtils.FONT_REGULAR);
        txtPass.setFont(StyleUtils.FONT_REGULAR);
        
        // Thêm các component vào box
        loginBox.add(lblTitle);
        loginBox.add(Box.createVerticalStrut(20));
        loginBox.add(createLabel("Tên đăng nhập:"));
        loginBox.add(txtUser);
        loginBox.add(Box.createVerticalStrut(10));
        loginBox.add(createLabel("Mật khẩu:"));
        loginBox.add(txtPass);
        loginBox.add(Box.createVerticalStrut(20));
        
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(200, 40));
        loginBox.add(btnLogin);
        
        loginBox.add(Box.createVerticalStrut(10));
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setForeground(StyleUtils.PRIMARY_COLOR);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBox.add(btnRegister);

        mainPanel.add(loginBox);

        // Events
        btnLogin.addActionListener(e -> controller.execute(txtUser.getText(), new String(txtPass.getPassword())));
        btnRegister.addActionListener(e -> openRegisterScreen());
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
            new GUIRegister(c, vm, this).setVisible(true);
            this.setVisible(false);
        } catch(Exception ex) { ex.printStackTrace(); }
    }

    @Override
    public void update() {
        if (viewModel.isSuccess) {
            this.dispose();
            openMainApp(viewModel.userId, viewModel.role);
        } else {
            JOptionPane.showMessageDialog(this, viewModel.message, "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ... [GIỮ NGUYÊN PHẦN openMainApp VÀ main NHƯ CŨ] ...
    // Copy hàm openMainApp và main từ câu trả lời trước vào đây
    private void openMainApp(Long userId, String role) {
        try {
            // Khởi tạo Repositories
            ProductRepository prodRepo = new MySQLProductRepository();
            CartRepository cartRepo = new MySQLCartRepository();
            OrderRepository orderRepo = new MySQLOrderRepository();
            CategoryRepository catRepo = new MySQLCategoryRepository();

            // Khởi tạo Use Cases & Controllers (Giống hệt code cũ, chỉ copy lại)
            var listVM = new adapters.product.getlist.GetListProductViewModel();
            var listPres = new adapters.product.getlist.GetListProductPresenter(listVM);
            var listUC = new cosmetic.usecase.products.getlist.GetListProductUseCase(prodRepo, listPres);
            var listCtrl = new adapters.product.getlist.GetListProductController(listUC);

            var addVM = new adapters.cart.add.AddToCartViewModel();
            var addPres = new adapters.cart.add.AddToCartPresenter(addVM);
            var addUC = new cosmetic.usecase.cart.add.AddToCartUseCase(cartRepo, prodRepo, addPres);
            var addCtrl = new adapters.cart.add.AddToCartController(addUC);

            var viewCartVM = new adapters.cart.view.ViewCartViewModel();
            var viewCartPres = new adapters.cart.view.ViewCartPresenter(viewCartVM);
            var viewCartUC = new cosmetic.usecase.cart.view.ViewCartUseCase(cartRepo, viewCartPres);
            var viewCartCtrl = new adapters.cart.view.ViewCartController(viewCartUC);

            var orderVM = new adapters.order.create.CreateOrderViewModel();
            var orderPres = new adapters.order.create.CreateOrderPresenter(orderVM);
            var orderUC = new cosmetic.usecase.order.create.CreateOrderUseCase(orderRepo, cartRepo, prodRepo, orderPres);
            var orderCtrl = new adapters.order.create.CreateOrderController(orderUC);

            var historyVM = new adapters.order.getlist.GetListViewModel();
            var historyPres = new adapters.order.getlist.GetListPresenter(historyVM);
            var historyUC = new cosmetic.usecase.order.getlist.GetListUseCase(orderRepo, historyPres);
            var historyCtrl = new adapters.order.getlist.GetListController(historyUC);

            var createProdVM = new adapters.product.create.CreateProductViewModel();
            var createProdPres = new adapters.product.create.CreateProductPresenter(createProdVM);
            var createProdUC = new cosmetic.usecase.product.create.CreateProductUseCase(prodRepo, catRepo, createProdPres);
            var createProdCtrl = new adapters.product.create.CreateProductController(createProdUC);

            Object[] extraDeps = {orderCtrl, orderVM, historyCtrl, historyVM, createProdCtrl, createProdVM};

            new GUIProductList(userId, role, listCtrl, listVM, addCtrl, addVM, viewCartCtrl, viewCartVM, extraDeps).setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set UI System Look and Feel để mượt hơn
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                UserRepository repo = new MySQLUserRepository();
                LoginViewModel vm = new LoginViewModel();
                LoginPresenter p = new LoginPresenter(vm);
                PasswordEncoder encoder = new PasswordEncoder() {
                    @Override public String encode(String s) { return s; }
                    @Override public boolean matches(String r, String e) { return r != null && r.equals(e); }
                };
                LoginUseCase uc = new LoginUseCase(repo, encoder, p);
                LoginController c = new LoginController(uc);
                new GUILogin(c, vm).setVisible(true);
            } catch (Exception e) { e.printStackTrace(); }
        });
    }
}