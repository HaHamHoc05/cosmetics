package desktop;

import adapters.Subscriber;
import adapters.order.create.CreateOrderController;
import adapters.order.create.CreateOrderViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUICreateOrder extends JFrame implements Subscriber {

    // Các thành phần giao diện
    private JTextField txtUserId = new JTextField(20);
    private JTextField txtAddress = new JTextField(20);
    private JTextField txtPhone = new JTextField(20);
    private JTextField txtPayment = new JTextField(20);
    private JButton btnOrder = new JButton("Đặt Hàng Ngay");
    private JLabel lblStatus = new JLabel("Trạng thái: Sẵn sàng");

    // "Cầu nối" để giao tiếp
    private final CreateOrderController controller;
    private final CreateOrderViewModel viewModel;

    public GUICreateOrder(CreateOrderController controller, CreateOrderViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        // 1. Đăng ký nhận tin: Khi ViewModel đổi, hãy gọi tôi!
        this.viewModel.addSubscriber(this);

        // 2. Vẽ giao diện (Layout)
        setupUI();

        // 3. Xử lý sự kiện nút bấm
        btnOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOrderButtonClicked();
            }
        });
    }

    private void setupUI() {
        setTitle("Hệ Thống Bán Mỹ Phẩm - Tạo Đơn Hàng");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10)); // Grid 6 dòng, 2 cột

        add(new JLabel("User ID (ví dụ: 1):"));
        add(txtUserId);

        add(new JLabel("Địa chỉ giao hàng:"));
        add(txtAddress);

        add(new JLabel("Số điện thoại:"));
        add(txtPhone);

        add(new JLabel("Thanh toán (COD/BANK):"));
        add(txtPayment);

        add(new JLabel("")); // Placeholder
        add(btnOrder);

        add(lblStatus);
    }

    // --- INPUT: Gửi yêu cầu đi ---
    private void onOrderButtonClicked() {
        // Gom dữ liệu từ ô nhập liệu
        CreateOrderController.InputDTO input = new CreateOrderController.InputDTO();
        input.userId = txtUserId.getText();
        input.address = txtAddress.getText();
        input.phone = txtPhone.getText();
        input.paymentMethod = txtPayment.getText();

        // Gọi Controller xử lý -> GUI xong việc, không chờ đợi
        lblStatus.setText("Đang xử lý...");
        lblStatus.setForeground(Color.BLUE);
        controller.execute(input);
    }

    // --- OUTPUT: Nhận kết quả về (Observer Pattern) ---
    @Override
    public void update() {
        // Hàm này tự động chạy khi ViewModel thay đổi
        if (viewModel.isSuccess) {
            lblStatus.setText("Thành công! Mã đơn: " + viewModel.newOrderId);
            lblStatus.setForeground(Color.GREEN);
            JOptionPane.showMessageDialog(this, viewModel.message);
        } else {
            lblStatus.setText("Lỗi: " + viewModel.message);
            lblStatus.setForeground(Color.RED);
        }
    }
}