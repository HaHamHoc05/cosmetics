package desktop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import adapters.Subscriber;
import adapters.order.getlist.*;
import cosmetic.usecase.order.getlist.OrderDTO;

public class GUIOrderHistory extends JFrame implements Subscriber {
    private Long userId;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnRefresh = new JButton("Làm mới");
    private JButton btnBack = new JButton("Quay lại");

    // Dependencies
    private final GetListController controller;
    private final GetListViewModel viewModel;

    public GUIOrderHistory(Long userId, GetListController controller, GetListViewModel viewModel) {
        this.userId = userId;
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addSubscriber(this);

        setupUI();
        loadData();
    }

    private void setupUI() {
        setTitle("Lịch sử đơn hàng - User ID: " + userId);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTop.add(btnBack);
        add(pnlTop, BorderLayout.NORTH);

        // Center: Table
        String[] columns = {"Mã Đơn", "Ngày đặt", "Tổng tiền", "Trạng thái", "Địa chỉ"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom
        JPanel pnlBot = new JPanel();
        pnlBot.add(btnRefresh);
        add(pnlBot, BorderLayout.SOUTH);

        // Events
        btnRefresh.addActionListener(e -> loadData());
        btnBack.addActionListener(e -> this.dispose()); // Chỉ đóng cửa sổ này
    }

    private void loadData() {
        // Gọi controller lấy danh sách đơn của user hiện tại
        controller.execute(userId);
    }

    @Override
    public void update() {
        if (viewModel.isSuccess) {
            model.setRowCount(0);
            List<OrderDTO> list = viewModel.orders;
            if (list != null) {
                for (OrderDTO o : list) {
                    model.addRow(new Object[]{
                        o.id, 
                        o.createdAt, 
                        o.totalAmount, 
                        o.status, 
                        o.address
                    });
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi tải lịch sử: " + viewModel.message);
        }
    }
}