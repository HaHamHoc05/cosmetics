package desktop;

import adapters.product.viewproducts.*;
import adapters.cart.addtocart.*;
import adapters.cart.updatecart.*;
import adapters.user.login.*;
import cosmetic.entities.*;
import cosmetic.repository.*;
import cosmetic.repository.impl.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class CosmeticShopGUI extends JFrame {
    private ProductRepository productRepo;
    private UserRepository userRepo;
    private CartRepository cartRepo;
    private User currentUser;
    
    // Components
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JLabel userLabel;
    private JLabel cartLabel;
    private JTextField searchField;
    private JButton btnLogin, btnLogout, btnViewCart, btnRefresh, btnSearch;
    
    public CosmeticShopGUI() {
        // Initialize repositories
        productRepo = new InMemoryProductRepository();
        userRepo = new InMemoryUserRepository();
        cartRepo = new InMemoryCartRepository();
        
        initComponents();
        loadProducts();
    }
    
    private void initComponents() {
        setTitle("Cosmetic Shop - Hệ thống quản lý mỹ phẩm");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top Panel - Header
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Center Panel - Product Table
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom Panel - Actions
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(255, 107, 157));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Logo & Title
        JLabel titleLabel = new JLabel("🌸 COSMETIC SHOP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        // User Info Panel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setOpaque(false);
        
        userLabel = new JLabel("👤 Chưa đăng nhập");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        cartLabel = new JLabel("🛒 Giỏ hàng: 0");
        cartLabel.setForeground(Color.WHITE);
        cartLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        btnLogin = createStyledButton("Đăng nhập", new Color(255, 255, 255), new Color(255, 107, 157));
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginDialog();
            }
        });
        
        btnLogout = createStyledButton("Đăng xuất", new Color(255, 255, 255), new Color(255, 107, 157));
        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        btnLogout.setVisible(false);
        
        btnViewCart = createStyledButton("Xem giỏ hàng", new Color(255, 255, 255), new Color(255, 107, 157));
        btnViewCart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCartDialog();
            }
        });
        btnViewCart.setVisible(false);
        
        userPanel.add(userLabel);
        userPanel.add(cartLabel);
        userPanel.add(btnLogin);
        userPanel.add(btnLogout);
        userPanel.add(btnViewCart);
        
        topPanel.add(userPanel, BorderLayout.EAST);
        
        return topPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(new Color(236, 240, 241));
        
        JLabel searchLabel = new JLabel("🔍 Tìm kiếm:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        searchField = new JTextField(30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchProducts();
                }
            }
        });
        
        btnSearch = createStyledButton("Tìm kiếm", new Color(255, 107, 157), Color.WHITE);
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchProducts();
            }
        });
        
        btnRefresh = createStyledButton("Làm mới", new Color(52, 152, 219), Color.WHITE);
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadProducts();
            }
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Tên sản phẩm", "Mô tả", "Giá", "Tồn kho", "Trạng thái", "Danh mục"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        productTable = new JTable(tableModel);
        productTable.setFont(new Font("Arial", Font.PLAIN, 13));
        productTable.setRowHeight(30);
        productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        productTable.getTableHeader().setBackground(new Color(52, 73, 94));
        productTable.getTableHeader().setForeground(Color.WHITE);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(new Color(236, 240, 241));
        
        JButton btnAddToCart = createStyledButton("➕ Thêm vào giỏ hàng", new Color(46, 204, 113), Color.WHITE);
        btnAddToCart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });
        
        JButton btnViewDetail = createStyledButton("📋 Xem chi tiết", new Color(52, 152, 219), Color.WHITE);
        btnViewDetail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewProductDetail();
            }
        });
        
        JButton btnTestCRUD = createStyledButton("🧪 Test CRUD", new Color(155, 89, 182), Color.WHITE);
        btnTestCRUD.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCRUDTestDialog();
            }
        });
        
        bottomPanel.add(btnAddToCart);
        bottomPanel.add(btnViewDetail);
        bottomPanel.add(btnTestCRUD);
        
        return bottomPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void loadProducts() {
        ViewProductsPublisher publisher = new ViewProductsPublisher();
        ViewProductsController controller = new ViewProductsController(productRepo, publisher);
        ViewProductsViewModel vm = controller.handle();
        
        tableModel.setRowCount(0);
        for (Product p : vm.getProducts()) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getName(),
                p.getDescription(),
                String.format("%,.0f đ", p.getPrice()),
                p.getStock(),
                p.getStatus().getDisplayName(),
                p.getCategory() != null ? p.getCategory().getName() : "N/A"
            });
        }
    }
    
    private void searchProducts() {
        String keyword = searchField.getText().trim();
        
        if (keyword.isEmpty()) {
            loadProducts();
            return;
        }
        
        tableModel.setRowCount(0);
        for (Product p : productRepo.findByKeyword(keyword)) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getName(),
                p.getDescription(),
                String.format("%,.0f đ", p.getPrice()),
                p.getStock(),
                p.getStatus().getDisplayName(),
                p.getCategory() != null ? p.getCategory().getName() : "N/A"
            });
        }
    }
    
    private void showLoginDialog() {
        JDialog dialog = new JDialog(this, "Đăng nhập", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("🔐 Đăng nhập hệ thống");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        dialog.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        dialog.add(new JLabel("Tên đăng nhập:"), gbc);
        
        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        dialog.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Mật khẩu:"), gbc);
        
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        dialog.add(passwordField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnOk = createStyledButton("Đăng nhập", new Color(46, 204, 113), Color.WHITE);
        JButton btnCancel = createStyledButton("Hủy", new Color(231, 76, 60), Color.WHITE);
        
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                LoginPublisher publisher = new LoginPublisher();
                LoginController controller = new LoginController(userRepo, publisher);
                
                try {
                    LoginViewModel vm = controller.handle(username, password);
                    currentUser = userRepo.findById(vm.getId());
                    updateUIAfterLogin();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(CosmeticShopGUI.this, "Đăng nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);
        
        // Info label
        JLabel infoLabel = new JLabel("<html><i>Demo: admin/Admin@123 hoặc user1/User@123</i></html>");
        infoLabel.setForeground(Color.GRAY);
        gbc.gridy = 4;
        dialog.add(infoLabel, gbc);
        
        dialog.setVisible(true);
    }
    
    private void logout() {
        currentUser = null;
        userLabel.setText("👤 Chưa đăng nhập");
        btnLogin.setVisible(true);
        btnLogout.setVisible(false);
        btnViewCart.setVisible(false);
        updateCartLabel();
        JOptionPane.showMessageDialog(this, "Đã đăng xuất!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateUIAfterLogin() {
        userLabel.setText("👤 " + currentUser.getUsername() + " (" + (currentUser.isAdmin() ? "Admin" : "User") + ")");
        btnLogin.setVisible(false);
        btnLogout.setVisible(true);
        btnViewCart.setVisible(true);
        updateCartLabel();
    }
    
    private void addToCart() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng đăng nhập trước!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long productId = Long.valueOf(tableModel.getValueAt(selectedRow, 0).toString());
        String quantityStr = JOptionPane.showInputDialog(this, "Nhập số lượng:", "1");
        
        if (quantityStr == null) return;
        
        try {
            int quantity = Integer.parseInt(quantityStr);
            
            AddToCartPublisher publisher = new AddToCartPublisher();
            AddToCartController controller = new AddToCartController(cartRepo, productRepo, publisher);
            AddToCartViewModel vm = controller.handle(currentUser.getId(), productId, quantity);
            
            updateCartLabel();
            JOptionPane.showMessageDialog(this, "Đã thêm vào giỏ hàng!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCartLabel() {
        if (currentUser == null) {
            cartLabel.setText("🛒 Giỏ hàng: 0");
            return;
        }
        
        Cart cart = cartRepo.findByUserId(currentUser.getId());
        int count = cart != null ? cart.getTotalItems() : 0;
        cartLabel.setText("🛒 Giỏ hàng: " + count);
    }
    
    private void showCartDialog() {
        if (currentUser == null) return;
        
        Cart cart = cartRepo.findByUserId(currentUser.getId());
        
        final JDialog dialog = new JDialog(this, "Giỏ hàng của bạn", true);
        dialog.setSize(800, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        if (cart == null || cart.isEmpty()) {
            JLabel emptyLabel = new JLabel("Giỏ hàng trống!", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.BOLD, 18));
            dialog.add(emptyLabel, BorderLayout.CENTER);
        } else {
            // Title Panel
            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(new Color(255, 107, 157));
            titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            JLabel titleLabel = new JLabel("🛒 Giỏ hàng của bạn");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(Color.WHITE);
            titlePanel.add(titleLabel);
            dialog.add(titlePanel, BorderLayout.NORTH);
            
            // Cart Table
            String[] columns = {"Sản phẩm", "Giá", "Số lượng", "Thành tiền"};
            final DefaultTableModel cartModel = new DefaultTableModel(columns, 0) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            for (CartItem item : cart.getItems()) {
                cartModel.addRow(new Object[]{
                    item.getProduct().getName(),
                    String.format("%,.0f đ", item.getProduct().getPrice()),
                    item.getQuantity(),
                    String.format("%,.0f đ", item.getSubtotal())
                });
            }
            
            final JTable cartTable = new JTable(cartModel);
            cartTable.setRowHeight(30);
            cartTable.setFont(new Font("Arial", Font.PLAIN, 13));
            cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
            cartTable.getTableHeader().setBackground(new Color(52, 73, 94));
            cartTable.getTableHeader().setForeground(Color.WHITE);
            cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            JScrollPane scrollPane = new JScrollPane(cartTable);
            dialog.add(scrollPane, BorderLayout.CENTER);
            
            // Bottom Panel with Total and Buttons
            JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            bottomPanel.setBackground(new Color(236, 240, 241));
            
            // Total Label
            JLabel totalLabel = new JLabel("💰 Tổng tiền: " + String.format("%,.0f đ", cart.getTotal()));
            totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
            totalLabel.setForeground(new Color(231, 76, 60));
            bottomPanel.add(totalLabel, BorderLayout.WEST);
            
            // Button Panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setOpaque(false);
            
            // Update Quantity Button
            JButton btnUpdate = createStyledButton("✏️ Sửa số lượng", new Color(241, 196, 15), Color.WHITE);
            btnUpdate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = cartTable.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng chọn sản phẩm cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    String productName = cartModel.getValueAt(selectedRow, 0).toString();
                    Cart currentCart = cartRepo.findByUserId(currentUser.getId());
                    CartItem selectedItem = null;
                    
                    for (CartItem item : currentCart.getItems()) {
                        if (item.getProduct().getName().equals(productName)) {
                            selectedItem = item;
                            break;
                        }
                    }
                    
                    if (selectedItem != null) {
                        String newQuantityStr = JOptionPane.showInputDialog(dialog, 
                            "Nhập số lượng mới cho " + productName + ":", 
                            selectedItem.getQuantity());
                        
                        if (newQuantityStr != null) {
                            try {
                                int newQuantity = Integer.parseInt(newQuantityStr);
                                
                                UpdateCartPublisher publisher = new UpdateCartPublisher();
                                UpdateCartController controller = new UpdateCartController(cartRepo, productRepo, publisher);
                                controller.handle(currentUser.getId(), selectedItem.getProduct().getId(), newQuantity);
                                
                                updateCartLabel();
                                dialog.dispose();
                                showCartDialog(); // Refresh cart dialog
                                JOptionPane.showMessageDialog(CosmeticShopGUI.this, "Đã cập nhật số lượng!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            });
            
            // Remove Button
            JButton btnRemove = createStyledButton("🗑️ Xóa sản phẩm", new Color(231, 76, 60), Color.WHITE);
            btnRemove.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = cartTable.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng chọn sản phẩm cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    String productName = cartModel.getValueAt(selectedRow, 0).toString();
                    
                    int confirm = JOptionPane.showConfirmDialog(dialog, 
                        "Bạn có chắc muốn xóa sản phẩm:\n" + productName + "?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        Cart currentCart = cartRepo.findByUserId(currentUser.getId());
                        CartItem selectedItem = null;
                        
                        for (CartItem item : currentCart.getItems()) {
                            if (item.getProduct().getName().equals(productName)) {
                                selectedItem = item;
                                break;
                            }
                        }
                        
                        if (selectedItem != null) {
                            try {
                                // Set quantity to 0 to remove item
                                UpdateCartPublisher publisher = new UpdateCartPublisher();
                                UpdateCartController controller = new UpdateCartController(cartRepo, productRepo, publisher);
                                controller.handle(currentUser.getId(), selectedItem.getProduct().getId(), 0);
                                
                                updateCartLabel();
                                dialog.dispose();
                                showCartDialog(); // Refresh cart dialog
                                JOptionPane.showMessageDialog(CosmeticShopGUI.this, "Đã xóa sản phẩm khỏi giỏ hàng!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            });
            
            // Clear Cart Button
            JButton btnClear = createStyledButton("🧹 Xóa toàn bộ", new Color(192, 57, 43), Color.WHITE);
            btnClear.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(dialog,
                        "Bạn có chắc muốn xóa toàn bộ giỏ hàng?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        Cart currentCart = cartRepo.findByUserId(currentUser.getId());
                        if (currentCart != null) {
                            currentCart.clear();
                            cartRepo.save(currentCart);
                            updateCartLabel();
                            dialog.dispose();
                            JOptionPane.showMessageDialog(CosmeticShopGUI.this, "Đã xóa toàn bộ giỏ hàng!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            });
            
            JButton btnClose = createStyledButton("❌ Đóng", new Color(149, 165, 166), Color.WHITE);
            btnClose.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });
            
            buttonPanel.add(btnUpdate);
            buttonPanel.add(btnRemove);
            buttonPanel.add(btnClear);
            buttonPanel.add(btnClose);
            
            bottomPanel.add(buttonPanel, BorderLayout.EAST);
            dialog.add(bottomPanel, BorderLayout.SOUTH);
        }
        
        dialog.setVisible(true);
    }
    
    private void viewProductDetail() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long productId = Long.valueOf(tableModel.getValueAt(selectedRow, 0).toString());
        Product product = productRepo.findById(productId);
        
        String detail = String.format(
            "ID: %d\nTên: %s\nMô tả: %s\nGiá: %,.0f đ\nTồn kho: %d\nTrạng thái: %s\nDanh mục: %s",
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getStatus().getDisplayName(),
            product.getCategory() != null ? product.getCategory().getName() : "N/A"
        );
        
        JOptionPane.showMessageDialog(this, detail, "Chi tiết sản phẩm", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showCRUDTestDialog() {
        CRUDTestDialog crudDialog = new CRUDTestDialog(this, productRepo);
        crudDialog.setVisible(true);
        loadProducts(); // Refresh after CRUD operations
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CosmeticShopGUI gui = new CosmeticShopGUI();
                gui.setVisible(true);
            }
        });
    }
}