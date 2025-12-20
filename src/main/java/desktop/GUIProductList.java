package desktop;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import adapters.Subscriber;
import adapters.cart.add.*;
import adapters.product.getlist.*;
import adapters.cart.view.*;
import adapters.order.getlist.*;
import adapters.product.create.*;
import cosmetic.usecase.products.getlist.GetListProductRes;

public class GUIProductList extends JFrame implements Subscriber {
    private Long userId;
    private String role;
    
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnCart, btnHistory, btnAdmin;

    // Dependencies (Giữ nguyên)
    private GetListProductController listCtrl;
    private GetListProductViewModel listVM;
    private AddToCartController addCtrl;
    private AddToCartViewModel addVM;
    private ViewCartController viewCartCtrl;
    private ViewCartViewModel viewCartVM;
    private Object[] extraDeps;

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

        this.listVM.addSubscriber(this);
        this.addVM.addSubscriber(this);

        setupUI();
        listCtrl.execute("", null);
    }

    private void setupUI() {
        setTitle("Cosmetic Shop - Trang Chủ");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Header Panel (Màu hồng)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleUtils.PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblBrand = new JLabel("COSMETIC STORE");
        lblBrand.setFont(StyleUtils.FONT_TITLE);
        lblBrand.setForeground(Color.WHITE);
        headerPanel.add(lblBrand, BorderLayout.WEST);

        // Panel chứa các nút chức năng bên phải header
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false); // Trong suốt để thấy nền hồng
        
        btnCart = createHeaderButton("Giỏ Hàng");
        btnHistory = createHeaderButton("Lịch Sử");
        actionPanel.add(btnCart);
        actionPanel.add(btnHistory);

        if("ADMIN".equalsIgnoreCase(role)) {
            btnAdmin = createHeaderButton("Quản Lý SP");
            btnAdmin.setBackground(Color.ORANGE); // Nổi bật nút Admin
            actionPanel.add(btnAdmin);
        }
        headerPanel.add(actionPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Center Panel (Search + Table)
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(Color.WHITE);

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        txtSearch = new JTextField(20);
        txtSearch.setFont(StyleUtils.FONT_REGULAR);
        txtSearch.setPreferredSize(new Dimension(200, 30));
        btnSearch = StyleUtils.createButton("Tìm kiếm");
        searchPanel.add(new JLabel("Tìm sản phẩm: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Tên Sản Phẩm", "Giá (VNĐ)", "Kho", "Mô tả"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        StyleUtils.styleTable(table); // Áp dụng style đẹp
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Nút mua hàng to ở dưới
        btnAdd = StyleUtils.createButton("THÊM VÀO GIỎ HÀNG");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(10,0,0,0));
        bottomPanel.add(btnAdd);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // --- Event Listeners ---
        btnSearch.addActionListener(e -> listCtrl.execute(txtSearch.getText(), null));
        
        btnAdd.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0) {
                Long pId = (Long) model.getValueAt(row, 0);
                String q = JOptionPane.showInputDialog(this, "Nhập số lượng muốn mua:", "1");
                if(q != null) {
                    try {
                        AddToCartController.InputDTO dto = new AddToCartController.InputDTO();
                        dto.userId = userId;
                        dto.productId = pId;
                        dto.quantity = Integer.parseInt(q);
                        addCtrl.execute(dto);
                    } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!"); }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm!");
            }
        });

        btnCart.addActionListener(e -> {
            Object[] deps = {extraDeps[0], extraDeps[1]};
            new GUICart(userId, viewCartCtrl, viewCartVM, deps).setVisible(true);
        });

        btnHistory.addActionListener(e -> 
            new GUIOrderHistory(userId, (GetListController)extraDeps[2], (GetListViewModel)extraDeps[3]).setVisible(true)
        );

        if(btnAdmin != null) {
            btnAdmin.addActionListener(e -> 
                new GUIAdminProduct((CreateProductController)extraDeps[4], (CreateProductViewModel)extraDeps[5]).setVisible(true)
            );
        }
    }

    // Helper tạo nút trên Header nhỏ hơn chút
    private JButton createHeaderButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(StyleUtils.FONT_BOLD);
        btn.setBackground(Color.WHITE);
        btn.setForeground(StyleUtils.PRIMARY_COLOR);
        btn.setFocusPainted(false);
        return btn;
    }

    @Override
    public void update() {
        if (listVM.products != null) {
            model.setRowCount(0);
            for (GetListProductRes.ProductDTO p : listVM.products) {
                model.addRow(new Object[]{
                    p.id, 
                    p.name, 
                    StyleUtils.formatCurrency(p.price), // Format tiền
                    p.quantity, 
                    p.description
                });
            }
        }
        if (addVM.message != null && !addVM.message.isEmpty()) {
            JOptionPane.showMessageDialog(this, addVM.message);
            addVM.message = null;
        }
    }
}