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
    private JLabel lblTotal = new JLabel("Tổng tiền: 0");
    private JButton btnCheckout = new JButton("Thanh Toán");
    private JButton btnRefresh = new JButton("Làm mới");

    private final ViewCartController viewCtrl;
    private final ViewCartViewModel viewVM;
    
    // Order dependencies
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
        viewCtrl.execute(userId); // Load cart ngay khi mở
    }

    private void setupUI() {
        setTitle("Giỏ Hàng");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // Center
        String[] cols = {"Mã SP", "Tên SP", "Đơn giá", "Số lượng", "Thành tiền"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom
        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        pnlBot.add(lblTotal);
        pnlBot.add(btnRefresh);
        pnlBot.add(btnCheckout);
        add(pnlBot, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> viewCtrl.execute(userId));
        
        btnCheckout.addActionListener(e -> {
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
                return;
            }
            this.dispose();
            new GUICreateOrder(userId, orderCtrl, orderVM).setVisible(true);
        });
    }

    @Override
    public void update() {
        if (viewVM.isSuccess) {
            model.setRowCount(0);
            if (viewVM.items != null) {
                for (CartDetailDTO item : viewVM.items) {
                    model.addRow(new Object[]{item.productId, item.productName, item.price, item.quantity, item.totalPrice});
                }
            }
            lblTotal.setText("Tổng cộng: " + (viewVM.grandTotal != null ? viewVM.grandTotal : 0) + " VND");
        } else {
            // Chỉ hiện lỗi nếu không phải là lỗi "giỏ hàng chưa tồn tại" (lần đầu vào)
            if(!viewVM.message.contains("tìm thấy"))
                JOptionPane.showMessageDialog(this, "Lỗi: " + viewVM.message);
        }
    }
}