package desktop;

import javax.swing.*;
import java.awt.*;
import adapters.Subscriber;
import adapters.order.create.*;

public class GUICreateOrder extends JFrame implements Subscriber {
    private final Long userId;
    
    private JTextField txtAddress = new JTextField("123 đường ABC");
    private JTextField txtPhone = new JTextField("0901234567");
    private JComboBox<String> cbPayment = new JComboBox<>(new String[]{"COD", "BANKING"});
    private JButton btnOrder = new JButton("Xác nhận đặt hàng");

    private final CreateOrderController controller;
    private final CreateOrderViewModel viewModel;

    public GUICreateOrder(Long userId, CreateOrderController controller, CreateOrderViewModel viewModel) {
        this.userId = userId;
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addSubscriber(this);
        setupUI();
    }

    private void setupUI() {
        setTitle("Thông tin giao hàng");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("User ID:")); add(new JLabel(String.valueOf(userId)));
        add(new JLabel("Địa chỉ:")); add(txtAddress);
        add(new JLabel("Số điện thoại:")); add(txtPhone);
        add(new JLabel("Thanh toán:")); add(cbPayment);
        add(new JLabel("")); add(btnOrder);

        btnOrder.addActionListener(e -> {
            CreateOrderController.InputDTO input = new CreateOrderController.InputDTO();
            // LƯU Ý QUAN TRỌNG: Core của bạn yêu cầu userId là String
            input.userId = String.valueOf(userId);
            input.address = txtAddress.getText();
            input.phone = txtPhone.getText();
            input.paymentMethod = (String) cbPayment.getSelectedItem();
            
            btnOrder.setEnabled(false);
            controller.execute(input);
        });
    }

    @Override
    public void update() {
        btnOrder.setEnabled(true);
        if (viewModel.isSuccess) {
            JOptionPane.showMessageDialog(this, "Đặt hàng thành công! Mã đơn: " + viewModel.newOrderId);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi: " + viewModel.message);
        }
    }
}