package desktop;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import adapters.Subscriber;
import adapters.cart.add.*;
import adapters.product.getlist.*;
import adapters.cart.view.*;
import adapters.order.getlist.*;
import adapters.product.create.*;
import adapters.category.getlist.*;
import cosmetic.usecase.products.getlist.GetListProductRes;
import cosmetic.entities.Category;

/**
 * Màn hình danh sách sản phẩm với giao diện grid hiện đại
 */
public class GUIProductList extends JFrame implements Subscriber {
    private final Long userId;
    private final String role;

    // UI Components
    private JTextField txtSearch;
    private JComboBox<CategoryItem> cbCategory;
    private JButton btnSearch, btnCart, btnHistory, btnAdmin, btnLogout;
    private JLabel lblCartBadge;
    private JPanel productsPanel;
    private JScrollPane scrollPane;
    private int cartItemCount = 0;

    // Dependencies
    private final GetListProductController listCtrl;
    private final GetListProductViewModel listVM;
    private final AddToCartController addCtrl;
    private final AddToCartViewModel addVM;
    private final ViewCartController viewCartCtrl;
    private final ViewCartViewModel viewCartVM;
    private final Object[] extraDeps;

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(220, 53, 69);
    private static final Color SECONDARY_COLOR = new Color(108, 117, 125);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color INFO_COLOR = new Color(23, 162, 184);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color WHITE = Color.WHITE;
    private static final Color CARD_SHADOW = new Color(0, 0, 0, 20);

    public GUIProductList(Long userId, String role,
                          GetListProductController listCtrl, GetListProductViewModel listVM,
                          AddToCartController addCtrl, AddToCartViewModel addVM,
                          ViewCartController viewCartCtrl, ViewCartViewModel viewCartVM,
                          Object[] extraDeps) {
        this.userId = userId;
        this.role = role;
        this.listCtrl = listCtrl;
        this.listVM = listVM;
        this.addCtrl = addCtrl;
        this.addVM = addVM;
        this.viewCartCtrl = viewCartCtrl;
        this.viewCartVM = viewCartVM;
        this.extraDeps = extraDeps;

        this.listVM.subscribe(this);
        this.addVM.subscribe(this);

        initComponents();
        setupUI();
        addListeners();
        
        // Load initial data
        listCtrl.execute("", null);
        updateCartCount();
    }

    private void initComponents() {
        txtSearch = new JTextField(25);
        cbCategory = new JComboBox<>();
        btnSearch = new JButton("🔍 Tìm kiếm");
        btnCart = new JButton("🛒");
        btnHistory = new JButton("📋 Lịch sử");
        btnAdmin = new JButton("⚙️ Quản lý");
        btnLogout = new JButton("🚪 Đăng xuất");
        lblCartBadge = new JLabel("0");
        
        // Styling
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font badgeFont = new Font("Segoe UI", Font.BOLD, 11);
        
        txtSearch.setFont(mainFont);
        cbCategory.setFont(mainFont);
        
        // Cart button with badge
        btnCart.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        btnCart.setPreferredSize(new Dimension(60, 40));
        btnCart.setBackground(WHITE);
        btnCart.setBorder(new LineBorder(new Color(206, 212, 218), 1));
        btnCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCart.setFocusPainted(false);
        
        lblCartBadge.setFont(badgeFont);
        lblCartBadge.setForeground(WHITE);
        lblCartBadge.setBackground(PRIMARY_COLOR);
        lblCartBadge.setOpaque(true);
        lblCartBadge.setHorizontalAlignment(SwingConstants.CENTER);
        lblCartBadge.setPreferredSize(new Dimension(20, 20));
        lblCartBadge.setBorder(new LineBorder(WHITE, 2));
        
        // Other buttons
        styleButton(btnSearch, INFO_COLOR);
        styleButton(btnHistory, SECONDARY_COLOR);
        styleButton(btnAdmin, PRIMARY_COLOR);
        styleButton(btnLogout, SECONDARY_COLOR);
        
        // Products panel
        productsPanel = new JPanel();
        productsPanel.setLayout(new GridLayout(0, 3, 20, 20));
        productsPanel.setBackground(BACKGROUND_COLOR);
        productsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 40));
    }

    private void setupUI() {
        setTitle("Cosmetic Shop - " + role);
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Top panel
        JPanel topPanel = createTopPanel();
        
        // Center panel with products
        scrollPane = new JScrollPane(productsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(222, 226, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Left: Logo and welcome
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(WHITE);
        
        JLabel lblLogo = new JLabel("💄 Cosmetic Shop");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setForeground(PRIMARY_COLOR);
        
        JLabel lblWelcome = new JLabel("Xin chào, " + role + " #" + userId);
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblWelcome.setForeground(SECONDARY_COLOR);
        
        leftPanel.add(lblLogo);
        leftPanel.add(Box.createHorizontalStrut(20));
        leftPanel.add(lblWelcome);
        
        // Center: Search
        JPanel centerPanel = createSearchPanel();
        
        // Right: Actions
        JPanel rightPanel = createActionsPanel();
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBackground(WHITE);
        
        // Category dropdown
        cbCategory.addItem(new CategoryItem(null, "Tất cả danh mục"));
        loadCategories();
        
        // Search field
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(206, 212, 218), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtSearch.setPreferredSize(new Dimension(250, 35));
        
        cbCategory.setPreferredSize(new Dimension(180, 35));
        
        panel.add(cbCategory);
        panel.add(txtSearch);
        panel.add(btnSearch);
        
        return panel;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBackground(WHITE);
        
        // Cart button with badge overlay
        JLayeredPane cartPane = new JLayeredPane();
        cartPane.setPreferredSize(new Dimension(60, 40));
        
        btnCart.setBounds(0, 0, 60, 40);
        lblCartBadge.setBounds(40, -5, 20, 20);
        
        cartPane.add(btnCart, JLayeredPane.DEFAULT_LAYER);
        cartPane.add(lblCartBadge, JLayeredPane.PALETTE_LAYER);
        
        panel.add(cartPane);
        panel.add(btnHistory);
        
        if ("ADMIN".equalsIgnoreCase(role)) {
            panel.add(btnAdmin);
        }
        
        panel.add(btnLogout);
        
        return panel;
    }

    private void loadCategories() {
        try {
            var catRepo = new cosmetic.repository.MySQLCategoryRepository();
            var catVM = new GetListCategoryViewModel();
            var catPres = new GetListCategoryPresenter(catVM);
            var catUC = new cosmetic.usecase.category.getlist.GetListCategoryUseCase(catRepo, catPres);
            var catCtrl = new GetListCategoryController(catUC);
            
            catVM.subscribe(new Subscriber() {
                @Override
                public void update() {
                    if (catVM.isSuccess && catVM.categories != null) {
                        for (Category cat : catVM.categories) {
                            cbCategory.addItem(new CategoryItem(cat.getId(), cat.getName()));
                        }
                    }
                }
            });
            
            catCtrl.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addListeners() {
        btnSearch.addActionListener(e -> handleSearch());
        txtSearch.addActionListener(e -> handleSearch());
        
        cbCategory.addActionListener(e -> {
            if (cbCategory.getSelectedIndex() > 0) {
                handleSearch();
            }
        });
        
        btnCart.addActionListener(e -> handleCart());
        btnHistory.addActionListener(e -> handleHistory());
        btnAdmin.addActionListener(e -> handleAdmin());
        btnLogout.addActionListener(e -> handleLogout());
    }

    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        CategoryItem selected = (CategoryItem) cbCategory.getSelectedItem();
        Long categoryId = (selected != null) ? selected.getId() : null;
        
        listCtrl.execute(keyword, categoryId);
    }

    private void handleCart() {
        Object[] orderDeps = {extraDeps[0], extraDeps[1]};
        new GUICart(userId, viewCartCtrl, viewCartVM, orderDeps).setVisible(true);
    }

    private void handleHistory() {
        GetListController histCtrl = (GetListController) extraDeps[2];
        GetListViewModel histVM = (GetListViewModel) extraDeps[3];
        new GUIOrderHistory(userId, histCtrl, histVM).setVisible(true);
    }

    private void handleAdmin() {
        CreateProductController createCtrl = (CreateProductController) extraDeps[4];
        CreateProductViewModel createVM = (CreateProductViewModel) extraDeps[5];
        new GUIAdminProduct(createCtrl, createVM).setVisible(true);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            
            try {
                var repo = new cosmetic.repository.MySQLUserRepository();
                var encoder = new adapters.BCryptPasswordEncoder();
                var vm = new adapters.user.login.LoginViewModel();
                var pres = new adapters.user.login.LoginPresenter(vm);
                var uc = new cosmetic.usecase.user.login.LoginUseCase(repo, encoder, pres);
                var ctrl = new adapters.user.login.LoginController(uc);
                
                new GUILogin(ctrl, vm).setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateCartCount() {
        viewCartVM.subscribe(new Subscriber() {
            @Override
            public void update() {
                if (viewCartVM.isSuccess && viewCartVM.items != null) {
                    cartItemCount = viewCartVM.items.size();
                    lblCartBadge.setText(String.valueOf(cartItemCount));
                }
            }
        });
        viewCartCtrl.execute(userId);
    }

    @Override
    public void update() {
        if (listVM.isSuccess && listVM.products != null) {
            displayProducts(listVM.products);
        }
        
        if (addVM.message != null && !addVM.message.isEmpty()) {
            if (addVM.isSuccess) {
                showNotification("✓ " + addVM.message, SUCCESS_COLOR);
                cartItemCount = addVM.totalItems;
                lblCartBadge.setText(String.valueOf(cartItemCount));
            } else {
                showNotification("✗ " + addVM.message, PRIMARY_COLOR);
            }
            addVM.setState(false, null, 0);
        }
    }

    private void displayProducts(java.util.List<GetListProductRes.ProductDTO> products) {
        productsPanel.removeAll();
        
        if (products.isEmpty()) {
            JLabel lblEmpty = new JLabel("Không tìm thấy sản phẩm nào");
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblEmpty.setForeground(SECONDARY_COLOR);
            lblEmpty.setHorizontalAlignment(SwingConstants.CENTER);
            productsPanel.setLayout(new BorderLayout());
            productsPanel.add(lblEmpty, BorderLayout.CENTER);
        } else {
            productsPanel.setLayout(new GridLayout(0, 3, 20, 20));
            for (GetListProductRes.ProductDTO product : products) {
                productsPanel.add(createProductCard(product));
            }
        }
        
        productsPanel.revalidate();
        productsPanel.repaint();
    }

    private JPanel createProductCard(GetListProductRes.ProductDTO product) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(222, 226, 230), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Image placeholder
        JLabel lblImage = new JLabel("🖼️");
        lblImage.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImage.setPreferredSize(new Dimension(200, 150));
        lblImage.setMaximumSize(new Dimension(200, 150));
        lblImage.setBackground(BACKGROUND_COLOR);
        lblImage.setOpaque(true);
        
        // Product name
        JLabel lblName = new JLabel(product.name);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblName.setForeground(new Color(33, 37, 41));
        
        // Price
        JLabel lblPrice = new JLabel(String.format("%,.0f VNĐ", product.price));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPrice.setForeground(PRIMARY_COLOR);
        lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add to cart button
        JButton btnAdd = new JButton("🛒 Thêm vào giỏ");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.setBackground(PRIMARY_COLOR);
        btnAdd.setForeground(WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnAdd.addActionListener(e -> {
            String qtyStr = JOptionPane.showInputDialog(this, 
                "Nhập số lượng cho: " + product.name, 
                "Số lượng", 
                JOptionPane.PLAIN_MESSAGE);
            
            if (qtyStr != null && !qtyStr.trim().isEmpty()) {
                try {
                    int qty = Integer.parseInt(qtyStr.trim());
                    if (qty <= 0) {
                        showNotification("Số lượng phải lớn hơn 0", PRIMARY_COLOR);
                        return;
                    }
                    
                    AddToCartController.InputDTO dto = new AddToCartController.InputDTO();
                    dto.userId = userId;
                    dto.productId = product.id;
                    dto.quantity = qty;
                    addCtrl.execute(dto);
                } catch (NumberFormatException ex) {
                    showNotification("Số lượng không hợp lệ", PRIMARY_COLOR);
                }
            }
        });
        
        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(PRIMARY_COLOR, 2, true),
                    BorderFactory.createEmptyBorder(14, 14, 14, 14)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(222, 226, 230), 1, true),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
        });
        
        card.add(lblImage);
        card.add(Box.createVerticalStrut(10));
        card.add(lblName);
        card.add(Box.createVerticalStrut(5));
        card.add(lblPrice);
        card.add(Box.createVerticalStrut(10));
        card.add(btnAdd);
        
        return card;
    }

    private void showNotification(String message, Color color) {
        JLabel notification = new JLabel(message);
        notification.setFont(new Font("Segoe UI", Font.BOLD, 13));
        notification.setForeground(color);
        notification.setOpaque(true);
        notification.setBackground(WHITE);
        notification.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 2, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JDialog dialog = new JDialog(this);
        dialog.setUndecorated(true);
        dialog.add(notification);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        
        Timer timer = new Timer(2000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
    }

    // Helper class for category dropdown
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