package desktop;

import javax.swing.*;
import java.awt.*;
import adapters.Subscriber;
import adapters.user.register.*;
import cosmetic.repository.*;
import cosmetic.usecase.user.register.RegisterUseCase;

public class GUIRegister extends JFrame implements Subscriber {
    private JTextField txtUser = new JTextField();
    private JPasswordField txtPass = new JPasswordField();
    private JTextField txtEmail = new JTextField();
    private JButton btnRegister = new JButton("Đăng Ký");
    private JButton btnBack = new JButton("Quay lại");

    private final RegisterController controller;
    private final RegisterViewModel viewModel;
    private final JFrame loginScreen;

    public GUIRegister(RegisterController controller, RegisterViewModel viewModel, JFrame loginScreen) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.loginScreen = loginScreen;
        this.viewModel.addSubscriber(this);
        setupUI();
    }

    private void setupUI() {
        setTitle("Đăng Ký Tài Khoản");
        setSize(300, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Username:")); add(txtUser);
        add(new JLabel("Password:")); add(txtPass);
        add(new JLabel("Email:")); add(txtEmail);
        add(btnBack); add(btnRegister);

        btnRegister.addActionListener(e -> {
            RegisterController.InputDTO input = new RegisterController.InputDTO();
            input.username = txtUser.getText();
            input.password = new String(txtPass.getPassword());
            input.email = txtEmail.getText();
            controller.execute(input);
        });

        btnBack.addActionListener(e -> {
            this.dispose();
            loginScreen.setVisible(true);
        });
    }

    @Override
    public void update() {
        if (viewModel.isSuccess) {
            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Hãy đăng nhập.");
            this.dispose();
            loginScreen.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi: " + viewModel.message);
        }
    }
}