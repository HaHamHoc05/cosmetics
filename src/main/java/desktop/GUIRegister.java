package desktop;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import adapters.Subscriber;
import adapters.user.register.*;

/**
 * Màn hình đăng ký với validation đầy đủ
 */
public class GUIRegister extends JFrame implements Subscriber {
    // UI Components
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextArea txtAddress;
    private JButton btnRegister;
    private JButton btnBack;
    private JLabel lblStatus;
    private JProgressBar progressBar;
    
    // Dependencies
    private final RegisterController controller;
    private final RegisterViewModel viewModel;
    private final JFrame loginScreen;

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(220, 53, 69);
    private static final Color SECONDARY_COLOR = new Color(108, 117, 125);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color WHITE = Color.WHITE;

    public GUIRegister(RegisterController controller, RegisterViewModel viewModel, JFrame loginScreen) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.loginScreen = loginScreen;
        this.viewModel.subscribe(this);
        
        initComponents();
        setupUI();
        addListeners();
    }

    private void initComponents() {
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtConfirmPassword = new JPasswordField(20);
        txtFullName = new JTextField(20);
        txtEmail = new JTextField(20);
        txtPhone = new JTextField(20);
        txtAddress = new JTextArea(3, 20);
        btnRegister = new JButton("Đăng Ký");
        btnBack = new JButton("← Quay lại");
        lblStatus = new JLabel(" ");
        progressBar = new JProgressBar();
        
        // Font
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
        
        txtUsername.setFont(mainFont);
        txtPassword.setFont(mainFont);
        txtConfirmPassword.setFont(mainFont);
        txtFullName.setFont(mainFont);
        txtEmail.setFont(mainFont);
        txtPhone.setFont(mainFont);
        txtAddress.setFont(mainFont);
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        
        // Button styling
        btnRegister.setFont(btnFont);
        btnRegister.setBackground(PRIMARY_COLOR);
        btnRegister.setForeground(WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBack.setBackground(SECONDARY_COLOR);
        btnBack.setForeground(WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        
        progressBar.setVisible(false);
        progressBar.setIndeterminate(true);
    }

    private void setupUI() {
        setTitle("Đăng Ký Tài Khoản");
        setSize(500, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Form
        JPanel formPanel = createFormPanel();
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel lblIcon = new JLabel("📝");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitle = new JLabel("Tạo Tài Khoản Mới");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(PRIMARY_COLOR);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitle = new JLabel("Điền thông tin để đăng ký");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Account section
        panel.add(createSectionLabel("Thông Tin Đăng Nhập"));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFieldPanel("👤 Tên đăng nhập *", txtUsername, "Tối thiểu 3 ký tự"));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFieldPanel("🔒 Mật khẩu *", txtPassword, "Tối thiểu 6 ký tự"));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFieldPanel("🔒 Xác nhận mật khẩu *", txtConfirmPassword, "Nhập lại mật khẩu"));
        
        panel.add(Box.createVerticalStrut(25));
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(25));
        
        // Personal info section
        panel.add(createSectionLabel("Thông Tin Cá Nhân"));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFieldPanel("👨 Họ và tên *", txtFullName, "Ví dụ: Nguyễn Văn A"));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFieldPanel("📧 Email *", txtEmail, "example@email.com"));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFieldPanel("📱 Số điện thoại *", txtPhone, "Ví dụ: 0901234567"));
        panel.add(Box.createVerticalStrut(15));
        
        // Address field (special handling for JTextArea)
        JPanel addressPanel = new JPanel();
        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.Y_AXIS));
        addressPanel.setBackground(WHITE);
        addressPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblAddress = new JLabel("🏠 Địa chỉ");
        lblAddress.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblAddress.setForeground(SECONDARY_COLOR);
        lblAddress.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane scrollAddress = new JScrollPane(txtAddress);
        scrollAddress.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollAddress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        scrollAddress.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(206, 212, 218), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        addressPanel.add(lblAddress);
        addressPanel.add(Box.createVerticalStrut(5));
        addressPanel.add(scrollAddress);
        
        panel.add(addressPanel);
        
        return panel;
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(PRIMARY_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createFieldPanel(String label, JTextField field, String placeholder) {
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
        
        // Placeholder-like hint
        JLabel lblHint = new JLabel(placeholder);
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(new Color(173, 181, 189));
        lblHint.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        panel.add(Box.createVerticalStrut(3));
        panel.add(lblHint);
        
        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Progress bar
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        panel.add(progressBar);
        panel.add(Box.createVerticalStrut(10));
        
        // Status label
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblStatus);
        panel.add(Box.createVerticalStrut(15));
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setBackground(BACKGROUND_COLOR);
        
        btnBack.setPreferredSize(new Dimension(120, 40));
        btnRegister.setPreferredSize(new Dimension(150, 40));
        
        btnPanel.add(btnBack);
        btnPanel.add(btnRegister);
        
        panel.add(btnPanel);
        
        return panel;
    }

    private void addListeners() {
        btnRegister.addActionListener(e -> handleRegister());
        btnBack.addActionListener(e -> handleBack());
        
        // Real-time validation hints
        txtPassword.addCaretListener(e -> validatePasswordStrength());
        txtEmail.addCaretListener(e -> validateEmail());
        txtPhone.addCaretListener(e -> validatePhone());
    }

    private void validatePasswordStrength() {
        String password = new String(txtPassword.getPassword());
        // Simple password strength indicator (có thể mở rộng)
    }

    private void validateEmail() {
        String email = txtEmail.getText();
        // Simple email validation (có thể mở rộng)
    }

    private void validatePhone() {
        String phone = txtPhone.getText();
        // Simple phone validation (có thể mở rộng)
    }

    private void handleRegister() {
        // Clear previous status
        lblStatus.setText("");
        
        // Validate
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String address = txtAddress.getText().trim();
        
        // Client-side validation
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || 
            email.isEmpty() || phone.isEmpty()) {
            showStatus("⚠ Vui lòng điền đầy đủ thông tin bắt buộc (*)", false);
            return;
        }
        
        if (username.length() < 3) {
            showStatus("⚠ Tên đăng nhập phải có ít nhất 3 ký tự", false);
            return;
        }
        
        if (password.length() < 6) {
            showStatus("⚠ Mật khẩu phải có ít nhất 6 ký tự", false);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showStatus("⚠ Mật khẩu xác nhận không khớp", false);
            return;
        }
        
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showStatus("⚠ Email không hợp lệ", false);
            return;
        }
        
        if (!phone.matches("^0[35789]\\d{8}$")) {
            showStatus("⚠ Số điện thoại không hợp lệ (VD: 0901234567)", false);
            return;
        }
        
        // Disable button and show progress
        btnRegister.setEnabled(false);
        btnBack.setEnabled(false);
        progressBar.setVisible(true);
        showStatus("Đang xử lý đăng ký...", true);
        
        // Execute
        RegisterController.InputDTO input = new RegisterController.InputDTO();
        input.username = username;
        input.password = password;
        input.confirmPassword = confirmPassword;
        input.fullName = fullName;
        input.email = email;
        input.phone = phone;
        input.address = address;
        
        controller.execute(input);
    }

    private void handleBack() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn quay lại?\nDữ liệu chưa lưu sẽ bị mất.",
            "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            loginScreen.setVisible(true);
        }
    }

    private void showStatus(String message, boolean isProcessing) {
        lblStatus.setText(message);
        lblStatus.setForeground(isProcessing ? SECONDARY_COLOR : PRIMARY_COLOR);
    }

    @Override
    public void update() {
        btnRegister.setEnabled(true);
        btnBack.setEnabled(true);
        progressBar.setVisible(false);
        
        if (viewModel.isSuccess) {
            lblStatus.setText("✓ Đăng ký thành công!");
            lblStatus.setForeground(SUCCESS_COLOR);
            
            JOptionPane.showMessageDialog(this,
                "Chào mừng bạn đến với Cosmetic Shop!\n" +
                "Tài khoản của bạn đã được tạo thành công.\n" +
                "Hãy đăng nhập để bắt đầu mua sắm.",
                "Đăng Ký Thành Công",
                JOptionPane.INFORMATION_MESSAGE);
            
            this.dispose();
            loginScreen.setVisible(true);
        } else {
            showStatus("✗ " + viewModel.message, false);
        }
    }
}