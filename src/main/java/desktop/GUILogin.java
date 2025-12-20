package desktop;

import javax.swing.*;
import java.awt.*;
import adapters.Subscriber;
import adapters.user.login.*;
import adapters.user.register.*;
import cosmetic.repository.*;
import cosmetic.usecase.user.login.LoginUseCase;
import cosmetic.usecase.user.register.RegisterUseCase;

public class GUILogin extends JFrame implements Subscriber {
    private JTextField txtUser = new JTextField("admin"); // Test admin
    private JPasswordField txtPass = new JPasswordField("123456");
    private JButton btnLogin = new JButton("Đăng Nhập");
    private JButton btnRegister = new JButton("Đăng Ký");

    private final LoginController controller;
    private final LoginViewModel viewModel;

    public GUILogin(LoginController controller, LoginViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addSubscriber(this);
        setupUI();
    }

    private void setupUI() {
        setTitle("Login System");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Username:")); add(txtUser);
        add(new JLabel("Password:")); add(txtPass);
        add(btnRegister); add(btnLogin);

        btnLogin.addActionListener(e -> controller.execute(txtUser.getText(), new String(txtPass.getPassword())));
        
        btnRegister.addActionListener(e -> openRegisterScreen());
    }

    private void openRegisterScreen() {
        try {
            UserRepository repo = new MySQLUserRepository();
            RegisterViewModel vm = new RegisterViewModel();
            RegisterPresenter pres = new RegisterPresenter(vm);
            RegisterUseCase uc = new RegisterUseCase(repo, pres);
            RegisterController ctrl = new RegisterController(uc);
            
            // Bạn cần tạo file GUIRegister (đã cung cấp ở câu trả lời trước)
            new GUIRegister(ctrl, vm, this).setVisible(true);
            this.setVisible(false);
        } catch(Exception ex) { ex.printStackTrace(); }
    }

    @Override
    public void update() {
        if (viewModel.isSuccess) {
            JOptionPane.showMessageDialog(this, "Xin chào " + viewModel.role + " (ID: " + viewModel.userId + ")");
            this.dispose();
            openMainApp(viewModel.userId, viewModel.role);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi: " + viewModel.message);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Init Login Dependencies
                UserRepository repo = new MySQLUserRepository();
                LoginViewModel vm = new LoginViewModel();
                LoginPresenter pres = new LoginPresenter(vm);
                LoginUseCase uc = new LoginUseCase(repo, pres);
                LoginController ctrl = new LoginController(uc);
                new GUILogin(ctrl, vm).setVisible(true);
            } catch (Exception e) { e.printStackTrace(); }
        });
    }

    private void openMainApp(Long userId, String role) {
        try {
            // 1. Repositories
            ProductRepository prodRepo = new MySQLProductRepository();
            CartRepository cartRepo = new MySQLCartRepository();
            OrderRepository orderRepo = new MySQLOrderRepository();
            CategoryRepository catRepo = new MySQLCategoryRepository(); // Cần cho Admin thêm SP

            // 2. Product List & Search
            adapters.product.getlist.GetListProductViewModel listVM = new adapters.product.getlist.GetListProductViewModel();
            adapters.product.getlist.GetListProductPresenter listPres = new adapters.product.getlist.GetListProductPresenter(listVM);
            cosmetic.usecase.products.getlist.GetListProductUseCase listUC = new cosmetic.usecase.products.getlist.GetListProductUseCase(prodRepo, listPres);
            adapters.product.getlist.GetListProductController listCtrl = new adapters.product.getlist.GetListProductController(listUC);

            // 3. Add To Cart
            adapters.cart.add.AddToCartViewModel addVM = new adapters.cart.add.AddToCartViewModel();
            adapters.cart.add.AddToCartPresenter addPres = new adapters.cart.add.AddToCartPresenter(addVM);
            cosmetic.usecase.cart.add.AddToCartUseCase addUC = new cosmetic.usecase.cart.add.AddToCartUseCase(cartRepo, prodRepo, addPres);
            adapters.cart.add.AddToCartController addCtrl = new adapters.cart.add.AddToCartController(addUC);

            // 4. View Cart
            adapters.cart.view.ViewCartViewModel viewCartVM = new adapters.cart.view.ViewCartViewModel();
            adapters.cart.view.ViewCartPresenter viewCartPres = new adapters.cart.view.ViewCartPresenter(viewCartVM);
            cosmetic.usecase.cart.view.ViewCartUseCase viewCartUC = new cosmetic.usecase.cart.view.ViewCartUseCase(cartRepo, viewCartPres);
            adapters.cart.view.ViewCartController viewCartCtrl = new adapters.cart.view.ViewCartController(viewCartUC);

            // 5. Create Order
            adapters.order.create.CreateOrderViewModel createOrderVM = new adapters.order.create.CreateOrderViewModel();
            adapters.order.create.CreateOrderPresenter createOrderPres = new adapters.order.create.CreateOrderPresenter(createOrderVM);
            cosmetic.usecase.order.create.CreateOrderUseCase createOrderUC = new cosmetic.usecase.order.create.CreateOrderUseCase(orderRepo, cartRepo, prodRepo, createOrderPres);
            adapters.order.create.CreateOrderController createOrderCtrl = new adapters.order.create.CreateOrderController(createOrderUC);
            
            // 6. Order History (Lịch sử đơn hàng)
            adapters.order.getlist.GetListViewModel historyVM = new adapters.order.getlist.GetListViewModel();
            adapters.order.getlist.GetListPresenter historyPres = new adapters.order.getlist.GetListPresenter(historyVM);
            cosmetic.usecase.order.getlist.GetListUseCase historyUC = new cosmetic.usecase.order.getlist.GetListUseCase(orderRepo, historyPres);
            adapters.order.getlist.GetListController historyCtrl = new adapters.order.getlist.GetListController(historyUC);

            // 7. Admin Create Product (Chỉ khởi tạo nếu là admin hoặc cứ khởi tạo để sẵn)
            adapters.product.create.CreateProductViewModel createProdVM = new adapters.product.create.CreateProductViewModel();
            adapters.product.create.CreateProductPresenter createProdPres = new adapters.product.create.CreateProductPresenter(createProdVM);
            cosmetic.usecase.product.create.CreateProductUseCase createProdUC = new cosmetic.usecase.product.create.CreateProductUseCase(prodRepo, catRepo, createProdPres);
            adapters.product.create.CreateProductController createProdCtrl = new adapters.product.create.CreateProductController(createProdUC);

            // Đóng gói dependencies vào mảng object để truyền gọn
            Object[] extraDeps = {
                createOrderCtrl, createOrderVM, // 0, 1: Order Creation
                historyCtrl, historyVM,         // 2, 3: Order History
                createProdCtrl, createProdVM    // 4, 5: Admin Create Product
            };

            // Mở màn hình chính
            new GUIProductList(userId, role, listCtrl, listVM, addCtrl, addVM, viewCartCtrl, viewCartVM, extraDeps).setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khởi tạo: " + e.getMessage());
        }
    }
}