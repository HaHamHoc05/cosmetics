package desktop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import adapters.Subscriber;
import adapters.cart.view.*;
import adapters.order.create.*;
import cosmetic.usecase.cart.view.CartDetailDTO;

public class GUICart extends JFrame implements Subscriber {
    private final Long userId;
    
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal = new JLabel("Tổng tiền: 0 đ");
    private JButton btnCheckout, btnRefresh;

    private final ViewCartController viewCtrl;
    private final ViewCartViewModel viewVM;
    
    private final CreateOrderController orderCtrl;
    private final CreateOrderViewModel orderVM;

    public GUICart(Long userId, ViewCartController viewCtrl, ViewCartViewModel viewVM, Object[] orderDeps) {
        this.userId = userId;
        this.viewCtrl = viewCtrl;
        this.viewVM = viewVM;
        this.orderCtrl = (CreateOrderController) orderDeps[0];
        this.orderVM = (CreateOrderViewModel) orderDeps[1];

        this.viewVM.addSubscriber(this);
        setupUI();
        viewCtrl.execute(userId); 
    }

    private void setupUI() {
        setTitle("Giỏ Hàng Của Bạn");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // Table
        String[] cols = {"Mã SP", "Tên SP", "Đơn giá", "Số lượng", "Thành tiền"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        StyleUtils.styleTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom Panel
        JPanel pnlBot = new JPanel(new BorderLayout());
        pnlBot.setBorder(new javax.swing.border.EmptyBorder(10, 20, 10, 20));
        pnlBot.setBackground(StyleUtils.SECONDARY_COLOR);

        lblTotal.setFont(StyleUtils.FONT_TITLE);
        lblTotal.setForeground(StyleUtils.PRIMARY_COLOR);
        pnlBot.add(lblTotal, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        
        btnRefresh = StyleUtils.createButton("Cập nhật");
        btnRefresh.setBackground(Color.GRAY);
        
        btnCheckout = StyleUtils.createButton("THANH TOÁN");
        
        btnPanel.add(btnRefresh);
        btnPanel.add(btnCheckout);
        pnlBot.add(btnPanel, BorderLayout.EAST);

        add(pnlBot, BorderLayout.SOUTH);

        // Events
        btnRefresh.addActionListener(e -> viewCtrl.execute(userId));
        
        btnCheckout.addActionListener(e -> {
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng trống! Hãy mua thêm sản phẩm.");
                return;
            }
            try {
                // Mở màn hình tạo đơn hàng
                new GUICreateOrder(userId, orderCtrl, orderVM).setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace(); // In lỗi ra console để debug nếu cần
                JOptionPane.showMessageDialog(this, "Chưa cài đặt màn hình thanh toán hoặc lỗi khởi tạo: " + ex.getMessage());
            }
        });
    }

    @Override
    public void update() {
        if (viewVM.isSuccess) {
            model.setRowCount(0);
            if (viewVM.items != null) {
                for (CartDetailDTO item : viewVM.items) {
                    // SỬA LỖI: Chuyển BigDecimal sang double bằng .doubleValue()
                    double price = item.price != null ? item.price.doubleValue() : 0;
                    double totalItemPrice = item.totalPrice != null ? item.totalPrice.doubleValue() : 0;

                    model.addRow(new Object[]{
                        item.productId, 
                        item.productName, 
                        StyleUtils.formatCurrency(price), 
                        item.quantity, 
                        StyleUtils.formatCurrency(totalItemPrice)
                    });
                }
            }
            
            // SỬA LỖI: Chuyển BigDecimal grandTotal sang double
            double grandTotal = viewVM.grandTotal != null ? viewVM.grandTotal.doubleValue() : 0;
            lblTotal.setText("Tổng: " + StyleUtils.formatCurrency(grandTotal));
            
        } else if (viewVM.message != null && !viewVM.message.contains("tìm thấy")) { 
            // Bỏ qua thông báo lỗi nếu chỉ là do giỏ hàng trống (thường backend trả về message 'không tìm thấy' khi chưa có cart)
            JOptionPane.showMessageDialog(this, "Lỗi tải giỏ hàng: " + viewVM.message);
        }
    }
}