package desktop;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        // Load danh sách sản phẩm ban đầu
        listCtrl.execute("", null);
    }

    private void setupUI() {
        setTitle("Cosmetic Shop - Trang Chủ (" + role + ")");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleUtils.PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblBrand = new JLabel("COSMETIC STORE");
        lblBrand.setFont(StyleUtils.FONT_TITLE);
        lblBrand.setForeground(Color.WHITE);
        headerPanel.add(lblBrand, BorderLayout.WEST);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);
        
        btnCart = createHeaderButton("Giỏ Hàng");
        btnHistory = createHeaderButton("Lịch Sử");
        actionPanel.add(btnCart);
        actionPanel.add(btnHistory);

        // Hiển thị nút Admin nếu role là ADMIN
        if("ADMIN".equalsIgnoreCase(role)) {
            btnAdmin = createHeaderButton("Quản Lý Hệ Thống"); // Đổi tên cho hợp lý hơn
            btnAdmin.setBackground(Color.ORANGE);
            btnAdmin.setForeground(Color.BLACK);
            actionPanel.add(btnAdmin);
        }
        
        JButton btnLogout = createHeaderButton("Đăng Xuất");
        btnLogout.setBackground(new Color(0,0,0)); 
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnLogout.addActionListener(e -> {
            this.dispose();
            GUILogin.main(null);
        });
        actionPanel.add(btnLogout);

        headerPanel.add(actionPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Center ---
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(Color.WHITE);

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        txtSearch = new JTextField(20);
        txtSearch.setFont(StyleUtils.FONT_REGULAR);
        txtSearch.setPreferredSize(new Dimension(300, 35));
        
        btnSearch = StyleUtils.createButton("Tìm kiếm");
        btnSearch.setPreferredSize(new Dimension(100, 35));
        btnSearch.setBackground(new Color(33, 150, 243));
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
        StyleUtils.styleTable(table);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom Actions
        btnAdd = StyleUtils.createButton("THÊM VÀO GIỎ HÀNG");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(10,0,0,0));
        bottomPanel.add(btnAdd);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // --- Events ---
        btnSearch.addActionListener(e -> listCtrl.execute(txtSearch.getText(), null));
        txtSearch.addActionListener(e -> btnSearch.doClick());

        btnAdd.addActionListener(e -> addToCartAction());
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    addToCartAction();
                }
            }
        });

        btnCart.addActionListener(e -> {
            Object[] deps = {extraDeps[0], extraDeps[1]};
            new GUICart(userId, viewCartCtrl, viewCartVM, deps).setVisible(true);
        });

        btnHistory.addActionListener(e -> {
            try {
                new GUIOrderHistory(userId, (GetListController)extraDeps[2], (GetListViewModel)extraDeps[3]).setVisible(true);
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Chưa cài đặt màn hình lịch sử"); }
        });

        // --- CẬP NHẬT ĐOẠN CODE BẠN YÊU CẦU Ở ĐÂY ---
        if(btnAdmin != null) {
            btnAdmin.addActionListener(e -> {
                if ("ADMIN".equalsIgnoreCase(role)) {
                    // Mở Dashboard quản trị
                    new GUIAdminDashboard().setVisible(true);
                    this.dispose(); // Đóng màn hình hiện tại
                }
            });
        }
    }
    
    private void addToCartAction() {
        int row = table.getSelectedRow();
        if(row >= 0) {
            Long pId = (Long) model.getValueAt(row, 0);
            
            Object stockObj = model.getValueAt(row, 3);
            int stock = 0;
            if (stockObj != null) {
                try {
                    stock = Integer.parseInt(stockObj.toString());
                } catch (NumberFormatException e) {
                    stock = 0;
                }
            }
            
            if (stock <= 0) {
                JOptionPane.showMessageDialog(this, "Sản phẩm này đã hết hàng!");
                return;
            }

            String q = JOptionPane.showInputDialog(this, "Nhập số lượng muốn mua:", "1");
            if(q != null && !q.trim().isEmpty()) {
                try {
                    int quantity = Integer.parseInt(q);
                    if(quantity <= 0) throw new NumberFormatException();
                    
                    AddToCartController.InputDTO dto = new AddToCartController.InputDTO();
                    dto.userId = userId;
                    dto.productId = pId;
                    dto.quantity = quantity;
                    addCtrl.execute(dto);
                } catch(NumberFormatException ex) { 
                    JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!"); 
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm!");
        }
    }

    private JButton createHeaderButton(String text) {
    	JButton btn = StyleUtils.createButton(text);
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
                Object quantityDisplay = (p.quantity != null) ? p.quantity : 0;
                
                model.addRow(new Object[]{
                    p.id, 
                    p.name, 
                    StyleUtils.formatCurrency(p.price),
                    quantityDisplay, 
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