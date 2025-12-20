package desktop;

import javax.swing.*;
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
    private final Long userId;
    private final String role; // Thêm role

    // UI
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch = new JTextField(15);
    private JButton btnSearch = new JButton("Tìm");
    private JButton btnAdd = new JButton("Thêm vào giỏ");
    private JButton btnCart = new JButton("Giỏ Hàng");
    private JButton btnHistory = new JButton("Lịch sử đơn"); // Nút mới
    private JButton btnAdmin = new JButton("Quản lý SP");    // Nút cho Admin

    // Dependencies cũ
    private final GetListProductController listCtrl;
    private final GetListProductViewModel listVM;
    private final AddToCartController addCtrl;
    private final AddToCartViewModel addVM;
    private final ViewCartController viewCartCtrl;
    private final ViewCartViewModel viewCartVM;
    
    // Dependencies mới (Lấy từ mảng extraDeps)
    private final Object[] extraDeps;

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
        setTitle("Shop Mỹ Phẩm - " + role + " (ID: " + userId + ")");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top Panel
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTop.add(new JLabel("Tìm kiếm:"));
        pnlTop.add(txtSearch);
        pnlTop.add(btnSearch);
        pnlTop.add(Box.createHorizontalStrut(20));
        pnlTop.add(btnCart);
        pnlTop.add(btnHistory);

        // Chỉ hiện nút Admin nếu role là ADMIN (hoặc ID = 1)
        if ("ADMIN".equalsIgnoreCase(role) || "1".equals(String.valueOf(userId))) {
            btnAdmin.setForeground(Color.RED);
            pnlTop.add(btnAdmin);
        }
        
        add(pnlTop, BorderLayout.NORTH);

        // Center Panel
        String[] cols = {"ID", "Tên SP", "Giá", "Kho", "Mô tả", "Ảnh"};
        model = new DefaultTableModel(cols, 0) {
             @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom Panel
        JPanel pnlBot = new JPanel();
        pnlBot.add(btnAdd);
        add(pnlBot, BorderLayout.SOUTH);

        // --- Events ---
        btnSearch.addActionListener(e -> listCtrl.execute(txtSearch.getText(), null));

        btnAdd.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn sản phẩm trước!"); return;
            }
            Long pId = (Long) model.getValueAt(row, 0);
            String qtyStr = JOptionPane.showInputDialog(this, "Nhập số lượng:", "1");
            if (qtyStr != null) {
                try {
                    AddToCartController.InputDTO dto = new AddToCartController.InputDTO();
                    dto.userId = userId;
                    dto.productId = pId;
                    dto.quantity = Integer.parseInt(qtyStr);
                    addCtrl.execute(dto);
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Số lượng sai!"); }
            }
        });

        // Mở Giỏ hàng (Lấy dependencies order từ extraDeps[0], [1])
        btnCart.addActionListener(e -> {
            Object[] orderDeps = {extraDeps[0], extraDeps[1]};
            new GUICart(userId, viewCartCtrl, viewCartVM, orderDeps).setVisible(true);
        });

        // Mở Lịch sử (Lấy dependencies từ extraDeps[2], [3])
        btnHistory.addActionListener(e -> {
            GetListController histCtrl = (GetListController) extraDeps[2];
            GetListViewModel histVM = (GetListViewModel) extraDeps[3];
            new GUIOrderHistory(userId, histCtrl, histVM).setVisible(true);
        });

        // Mở Admin Product (Lấy dependencies từ extraDeps[4], [5])
        btnAdmin.addActionListener(e -> {
            CreateProductController createCtrl = (CreateProductController) extraDeps[4];
            CreateProductViewModel createVM = (CreateProductViewModel) extraDeps[5];
            new GUIAdminProduct(createCtrl, createVM).setVisible(true);
        });
    }

    @Override
    public void update() {
        if (listVM.products != null) {
            model.setRowCount(0);
            for (GetListProductRes.ProductDTO p : listVM.products) {
                model.addRow(new Object[]{p.id, p.name, p.price, p.quantity, p.description, p.imageUrl});
            }
        }
        if (addVM.message != null && !addVM.message.isEmpty()) {
            JOptionPane.showMessageDialog(this, addVM.message);
            addVM.message = null;
        }
    }
}