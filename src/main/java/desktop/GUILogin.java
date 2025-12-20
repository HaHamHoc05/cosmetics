package desktop;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import adapters.Subscriber;
import adapters.user.login.LoginController;
import adapters.user.login.LoginPresenter;
import adapters.user.login.LoginViewModel;
import cosmetic.repository.MySQLUserRepository;
import cosmetic.repository.UserRepository;
import cosmetic.usecase.user.login.LoginUseCase;

public class GUILogin extends JFrame implements Subscriber {

    private JTextField txtUser = new JTextField("user1");
    private JPasswordField txtPass = new JPasswordField("123456");
    private JButton btnLogin = new JButton("Đăng Nhập");

    // Giữ Controller và ViewModel làm thuộc tính của class
    private final LoginController controller;
    private final LoginViewModel viewModel;

    // Constructor nhận vào Controller và ViewModel (Dependency Injection)
    public GUILogin(LoginController controller, LoginViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        
        // Đăng ký Subscriber
        this.viewModel.addSubscriber(this);

        setupUI();
    }

    private void setupUI() {
        setTitle("Đăng Nhập");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("Username:")); add(txtUser);
        add(new JLabel("Password:")); add(txtPass);
        add(new JLabel("")); add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String u = txtUser.getText();
                String p = new String(txtPass.getPassword());
                controller.execute(u, p);
            }
        });
    }

    @Override
    public void update() {
        if (viewModel.isSuccess) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công! UserID: " + viewModel.userId);
            
            // --- CHUYỂN MÀN HÌNH ---
            // Tại đây, bạn sẽ khởi tạo màn hình tiếp theo (GUIProductList) 
            // và truyền dependencies cho nó tương tự như cách làm ở hàm main bên dưới.
            
            this.dispose(); // Đóng màn hình Login
            openProductListScreen(viewModel.userId); // Hàm mở màn hình danh sách
            
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi: " + viewModel.message);
        }
    }

    // Hàm main: Nơi khởi tạo mọi thứ theo đúng mẫu giáo viên
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Khởi tạo Repository (Database)
            UserRepository userRepo = new MySQLUserRepository();

            // 2. Khởi tạo ViewModel
            LoginViewModel viewModel = new LoginViewModel();

            // 3. Khởi tạo Presenter (kết nối với ViewModel)
            LoginPresenter presenter = new LoginPresenter(viewModel);

            // 4. Khởi tạo UseCase (kết nối Repo và Presenter)
            LoginUseCase useCase = new LoginUseCase(userRepo, presenter);

            // 5. Khởi tạo Controller (kết nối UseCase)
            LoginController controller = new LoginController(useCase);

            // 6. Khởi tạo GUI và chạy
            GUILogin screen = new GUILogin(controller, viewModel);
            screen.setVisible(true);
        });
    }

    // Helper để mở màn hình sau khi login thành công
    private void openProductListScreen(Long userId) {
        // Bạn sẽ cần copy đoạn khởi tạo (Repo -> VM -> Presenter -> UseCase -> Controller) 
        // của màn hình ProductList vào đây hoặc tạo một class Main riêng để quản lý luồng này.
        // Để đơn giản theo bài mẫu, ta có thể khởi tạo trực tiếp ở đây:
        
        // Ví dụ (Cần import các class tương ứng):
        /*
        ProductRepository prodRepo = new MySQLProductRepository();
        CartRepository cartRepo = new MySQLCartRepository();
        
        // ... Khởi tạo ViewModel, Presenter, Controller cho ProductList ...
        
        GUIProductList listScreen = new GUIProductList(prodListController, prodListViewModel, userId);
        listScreen.setVisible(true);
        */
        
        // Tạm thời hiển thị thông báo
        System.out.println("Mở màn hình danh sách sản phẩm cho User ID: " + userId);
    }
}