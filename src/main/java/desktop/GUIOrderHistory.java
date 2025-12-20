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

    private final GetListController controller;
    private final GetListViewModel viewModel;

    public GUIOrderHistory(Long userId, GetListController controller, GetListViewModel viewModel) {
        this.userId = userId;
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addSubscriber(this);

        setupUI();
        controller.execute(userId);
    }

    private void setupUI() {
        setTitle("Lịch sử đơn hàng");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columns = {"Mã Đơn", "Ngày đặt", "Tổng tiền", "Trạng thái", "Địa chỉ"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlBot = new JPanel();
        pnlBot.add(btnRefresh);
        add(pnlBot, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> controller.execute(userId));
    }

    @Override
    public void update() {
        if (viewModel.isSuccess) {
            model.setRowCount(0);
            List<OrderDTO> list = viewModel.orders;
            if (list != null) {
                for (OrderDTO o : list) {
                    model.addRow(new Object[]{o.id, o.createdAt, o.totalAmount, o.status, o.address});
                }
            }
        }
    }
}