package desktop;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.math.BigDecimal;
import adapters.Subscriber;
import adapters.product.create.*;
import adapters.category.getlist.*;
import cosmetic.entities.Category;
import cosmetic.repository.MySQLCategoryRepository;

/**
 * Màn hình quản lý sản phẩm dành cho Admin
 */
public class GUIAdminProduct extends JFrame implements Subscriber {
    // UI Components
    private JTextField txtName;
    private JTextField txtPrice;
    private JTextField txtQty;
    private JTextArea txtDesc;
    private JTextField txtImage;
    private JComboBox<CategoryItem> cbCategory;
    private JButton btnAdd;
    private JButton btnClear;
    private JLabel lblStatus;
    
    // Dependencies
    private final CreateProductController controller;
    private final CreateProductViewModel viewModel;

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(220, 53, 69);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color WHITE = Color.WHITE;

    public GUIAdminProduct(CreateProductController controller, CreateProductViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addSubscriber(this);
        
        initComponents();
        setupUI();
        loadCategories();
        addListeners();
    }

    private void initComponents() {
        txtName = new JTextField(20);
        txtPrice = new JTextField(10);
        txtQty = new JTextField(5);
        txtDesc = new JTextArea(4, 20);
        txtImage = new JTextField(20);
        cbCategory = new JComboBox<>();
        btnAdd = new JButton("✓ Thêm sản phẩm");
        btnClear = new JButton("✗ Xóa");
        lblStatus = new JLabel(" ");
        
        // Styling
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 13);
        
        txtName.setFont(mainFont);
        txtPrice.setFont(mainFont);
        txtQty.setFont(mainFont);
        txtDesc.setFont(mainFont);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtImage.setFont(mainFont);
        cbCategory.setFont(mainFont);
        
        btnAdd.setBackground(SUCCESS_COLOR);
        btnAdd.setForeground(WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        
        btnClear.setBackground(new Color(108, 117, 125));
        btnClear.setForeground(WHITE);
        btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnClear.setFocusPainted(false);
        btnClear.setBorderPainted(false);
        
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setupUI() {
        setTitle("Quản Lý Sản Phẩm - Admin");
        setSize(500, 600);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BACKGROUND_COLOR);
        JLabel lblTitle = new JLabel("🛍️ Thêm Sản Phẩm Mới");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(PRIMARY_COLOR);
        headerPanel.add(lblTitle);
        
        // Form
        JPanel formPanel = createFormPanel();
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(222, 226, 230), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        int row = 0;
        
        // Name
        addFormField(panel, "Tên sản phẩm *:", txtName, row++, gbc);
        
        // Price
        addFormField(panel, "Giá (VNĐ) *:", txtPrice, row++, gbc);
        
        // Quantity
        addFormField(panel, "Số lượng *:", txtQty, row++, gbc);
        
        // Category
        addFormField(panel, "Danh mục *:", cbCategory, row++, gbc);
        
        // Image URL
        addFormField(panel, "Link ảnh:", txtImage, row++, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblDesc = new JLabel("Mô tả:");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(lblDesc, gbc);
        
        gbc.gridx = 1;
        JScrollPane scrollDesc = new JScrollPane(txtDesc);
        scrollDesc.setPreferredSize(new Dimension(300, 80));
        panel.add(scrollDesc, gbc);
        
        return panel;
    }

    private void addFormField(JPanel panel, String labelText, Component field, int row, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Status
        lblStatus.setForeground(PRIMARY_COLOR);
        panel.add(lblStatus, BorderLayout.NORTH);
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(BACKGROUND_COLOR);
        
        btnClear.setPreferredSize(new Dimension(100, 40));
        btnAdd.setPreferredSize(new Dimension(180, 40));
        
        btnPanel.add(btnClear);
        btnPanel.add(btnAdd);
        
        panel.add(btnPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private void loadCategories() {
        try {
            var catRepo = new MySQLCategoryRepository();
            var catVM = new GetListCategoryViewModel();
            var catPres = new GetListCategoryPresenter(catVM);
            var catUC = new cosmetic.usecase.category.getlist.GetListCategoryUseCase(catRepo, catPres);
            var catCtrl = new GetListCategoryController(catUC);
            
            catVM.subscribe(new Subscriber() {
                @Override
                public void update() {
                    if (catVM.isSuccess && catVM.categories != null) {
                        cbCategory.removeAllItems();
                        for (Category cat : catVM.categories) {
                            cbCategory.addItem(new CategoryItem(cat.getId(), cat.getName()));
                        }
                    }
                }
            });
            
            catCtrl.execute();
        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("⚠ Không thể tải danh mục");
            lblStatus.setForeground(PRIMARY_COLOR);
        }
    }

    private void addListeners() {
        btnAdd.addActionListener(e -> handleAdd());
        btnClear.addActionListener(e -> handleClear());
    }

    private void handleAdd() {
        try {
            // Validate
            String name = txtName.getText().trim();
            String priceStr = txtPrice.getText().trim();
            String qtyStr = txtQty.getText().trim();
            
            if (name.isEmpty()) {
                showError("Vui lòng nhập tên sản phẩm!");
                txtName.requestFocus();
                return;
            }
            
            if (priceStr.isEmpty()) {
                showError("Vui lòng nhập giá!");
                txtPrice.requestFocus();
                return;
            }
            
            if (qtyStr.isEmpty()) {
                showError("Vui lòng nhập số lượng!");
                txtQty.requestFocus();
                return;
            }
            
            if (cbCategory.getSelectedItem() == null) {
                showError("Vui lòng chọn danh mục!");
                return;
            }
            
            // Parse data
            BigDecimal price = new BigDecimal(priceStr);
            int quantity = Integer.parseInt(qtyStr);
            CategoryItem selected = (CategoryItem) cbCategory.getSelectedItem();
            
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                showError("Giá phải lớn hơn 0!");
                return;
            }
            
            if (quantity < 0) {
                showError("Số lượng không được âm!");
                return;
            }
            
            // Create DTO
            CreateProductController.InputDTO input = new CreateProductController.InputDTO();
            input.name = name;
            input.price = price;
            input.quantity = quantity;
            input.description = txtDesc.getText().trim();
            input.image = txtImage.getText().trim();
            input.categoryId = selected.getId();
            
            // Disable button
            btnAdd.setEnabled(false);
            btnAdd.setText("Đang xử lý...");
            lblStatus.setText("Đang thêm sản phẩm...");
            lblStatus.setForeground(new Color(108, 117, 125));
            
            // Execute
            controller.execute(input);
            
        } catch (NumberFormatException ex) {
            showError("Giá hoặc số lượng không hợp lệ!");
            btnAdd.setEnabled(true);
            btnAdd.setText("✓ Thêm sản phẩm");
        }
    }

    private void handleClear() {
        txtName.setText("");
        txtPrice.setText("");
        txtQty.setText("");
        txtDesc.setText("");
        txtImage.setText("");
        if (cbCategory.getItemCount() > 0) {
            cbCategory.setSelectedIndex(0);
        }
        lblStatus.setText(" ");
        txtName.requestFocus();
    }

    private void showError(String message) {
        lblStatus.setText("⚠ " + message);
        lblStatus.setForeground(PRIMARY_COLOR);
    }

    @Override
    public void update() {
        btnAdd.setEnabled(true);
        btnAdd.setText("✓ Thêm sản phẩm");
        
        if (viewModel.isSuccess) {
            lblStatus.setText("✓ Thêm sản phẩm thành công! ID: " + viewModel.productId);
            lblStatus.setForeground(SUCCESS_COLOR);
            
            JOptionPane.showMessageDialog(this,
                "Sản phẩm đã được thêm thành công!\n" +
                "ID: " + viewModel.productId + "\n" +
                "Tên: " + txtName.getText(),
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
            
            handleClear();
        } else {
            lblStatus.setText("✗ " + viewModel.message);
            lblStatus.setForeground(PRIMARY_COLOR);
        }
    }

    // Helper class
    private static class CategoryItem {
        private Long id;
        private String name;
        
        public CategoryItem(Long id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public Long getId() { return id; }
        
        @Override
        public String toString() { return name; }
    }
}