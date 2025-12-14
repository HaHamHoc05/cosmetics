package desktop;

import cosmetic.entities.Category;
import cosmetic.entities.Product;
import cosmetic.repository.ProductRepository;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CRUDTestDialog extends JDialog {
    private ProductRepository productRepo;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea logArea;
    
    public CRUDTestDialog(JFrame parent, ProductRepository repo) {
        super(parent, "🧪 Test CRUD Operations", true);
        this.productRepo = repo;
        
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        initComponents();
    }
    
    private void initComponents() {
        // Top Panel - Title
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(155, 89, 182));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("🧪 CRUD Test - Quản lý sản phẩm");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center Panel - Split between Table and Log
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        
        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));
        
        String[] columns = {"ID", "Tên", "Giá", "Tồn kho", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        splitPane.setTopComponent(tablePanel);
        
        // Log Panel
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Log hoạt động"));
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(new Color(44, 62, 80));
        logArea.setForeground(Color.WHITE);
        logPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        
        splitPane.setBottomComponent(logPanel);
        add(splitPane, BorderLayout.CENTER);
        
        // Bottom Panel - Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(236, 240, 241));
        
        JButton btnCreate = createButton("➕ CREATE", new Color(46, 204, 113));
        btnCreate.addActionListener(e -> testCreate());
        
        JButton btnRead = createButton("📖 READ", new Color(52, 152, 219));
        btnRead.addActionListener(e -> testRead());
        
        JButton btnUpdate = createButton("✏️ UPDATE", new Color(241, 196, 15));
        btnUpdate.addActionListener(e -> testUpdate());
        
        JButton btnDelete = createButton("🗑️ DELETE", new Color(231, 76, 60));
        btnDelete.addActionListener(e -> testDelete());
        
        JButton btnReadAll = createButton("📋 READ ALL", new Color(52, 73, 94));
        btnReadAll.addActionListener(e -> testReadAll());
        
        JButton btnClearLog = createButton("🧹 Clear Log", new Color(149, 165, 166));
        btnClearLog.addActionListener(e -> logArea.setText(""));
        
        bottomPanel.add(btnCreate);
        bottomPanel.add(btnRead);
        bottomPanel.add(btnUpdate);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnReadAll);
        bottomPanel.add(btnClearLog);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Load initial data
        testReadAll();
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 35));
        return button;
    }
    
    private void testCreate() {
        JDialog dialog = new JDialog(this, "CREATE - Thêm sản phẩm mới", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("➕ Thêm sản phẩm mới");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        dialog.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Tên sản phẩm:"), gbc);
        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Mô tả:"), gbc);
        JTextField descField = new JTextField(20);
        gbc.gridx = 1;
        dialog.add(descField, gbc);
        
        // Price
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Giá:"), gbc);
        JTextField priceField = new JTextField(20);
        gbc.gridx = 1;
        dialog.add(priceField, gbc);
        
        // Stock
        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Tồn kho:"), gbc);
        JTextField stockField = new JTextField(20);
        gbc.gridx = 1;
        dialog.add(stockField, gbc);
        
        // Category
        gbc.gridx = 0; gbc.gridy = 5;
        dialog.add(new JLabel("Danh mục:"), gbc);
        JTextField categoryField = new JTextField(20);
        gbc.gridx = 1;
        dialog.add(categoryField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSave = createButton("💾 Lưu", new Color(46, 204, 113));
        JButton btnCancel = createButton("❌ Hủy", new Color(231, 76, 60));
        
        btnSave.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String desc = descField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                String catName = categoryField.getText();
                
                Category category = new Category(catName, "Mô tả " + catName);
                category.setId(1L);
                
                Product product = new Product(null, name, desc, price, stock, category);
                
                log("=== TEST CREATE ===");
                log("Đang tạo sản phẩm: " + name);
                
                productRepo.save(product);
                
                log("✅ Thành công! Đã thêm sản phẩm: " + name);
                log("------------------\n");
                
                testReadAll();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                log("❌ Lỗi: " + ex.getMessage());
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);
        
        dialog.setVisible(true);
    }
    
    private void testRead() {
        String idStr = JOptionPane.showInputDialog(this, "Nhập ID sản phẩm cần đọc:");
        if (idStr == null || idStr.trim().isEmpty()) return;
        
        try {
            Long id = Long.parseLong(idStr);
            
            log("=== TEST READ ===");
            log("Đang đọc sản phẩm ID: " + id);
            
            Product product = productRepo.findById(id);
            
            if (product != null) {
                log("✅ Tìm thấy sản phẩm:");
                log("  - ID: " + product.getId());
                log("  - Tên: " + product.getName());
                log("  - Giá: " + String.format("%,.0f đ", product.getPrice()));
                log("  - Tồn kho: " + product.getStock());
                log("  - Trạng thái: " + product.getStatus().getDisplayName());
                
                String detail = String.format(
                    "ID: %d\nTên: %s\nMô tả: %s\nGiá: %,.0f đ\nTồn kho: %d\nTrạng thái: %s",
                    product.getId(), product.getName(), product.getDescription(),
                    product.getPrice(), product.getStock(), product.getStatus().getDisplayName()
                );
                JOptionPane.showMessageDialog(this, detail, "Chi tiết sản phẩm", JOptionPane.INFORMATION_MESSAGE);
            } else {
                log("❌ Không tìm thấy sản phẩm với ID: " + id);
                JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
            log("------------------\n");
            
        } catch (Exception ex) {
            log("❌ Lỗi: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void testUpdate() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần cập nhật!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long productId = Long.valueOf(tableModel.getValueAt(selectedRow, 0).toString());
        Product product = productRepo.findById(productId);
        
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(this, "UPDATE - Cập nhật sản phẩm", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("✏️ Cập nhật sản phẩm ID: " + productId);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        dialog.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Tên sản phẩm:"), gbc);
        JTextField nameField = new JTextField(product.getName(), 20);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);
        
        // Price
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Giá:"), gbc);
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()), 20);
        gbc.gridx = 1;
        dialog.add(priceField, gbc);
        
        // Stock
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Tồn kho:"), gbc);
        JTextField stockField = new JTextField(String.valueOf(product.getStock()), 20);
        gbc.gridx = 1;
        dialog.add(stockField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSave = createButton("💾 Cập nhật", new Color(241, 196, 15));
        JButton btnCancel = createButton("❌ Hủy", new Color(231, 76, 60));
        
        btnSave.addActionListener(e -> {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                
                log("=== TEST UPDATE ===");
                log("Đang cập nhật sản phẩm ID: " + productId);
                log("Giá trị cũ: " + product.getName() + " - " + product.getPrice() + " đ");
                
                product.setName(name);
                product.setPrice(price);
                product.setStock(stock);
                
                productRepo.update(product);
                
                log("Giá trị mới: " + name + " - " + price + " đ");
                log("✅ Cập nhật thành công!");
                log("------------------\n");
                
                testReadAll();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                log("❌ Lỗi: " + ex.getMessage());
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);
        
        dialog.setVisible(true);
    }
    
    private void testDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long productId = Long.valueOf(tableModel.getValueAt(selectedRow, 0).toString());
        String productName = tableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn xóa sản phẩm:\n" + productName + "?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                log("=== TEST DELETE ===");
                log("Đang xóa sản phẩm ID: " + productId + " - " + productName);
                
                productRepo.delete(productId);
                
                log("✅ Xóa thành công!");
                log("------------------\n");
                
                testReadAll();
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                log("❌ Lỗi: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void testReadAll() {
        log("=== TEST READ ALL ===");
        log("Đang đọc tất cả sản phẩm...");
        
        var products = productRepo.findAll();
        
        tableModel.setRowCount(0);
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getName(),
                String.format("%,.0f đ", p.getPrice()),
                p.getStock(),
                p.getStatus().getDisplayName()
            });
        }
        
        log("✅ Tìm thấy " + products.size() + " sản phẩm");
        log("------------------\n");
    }
    
    private void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}