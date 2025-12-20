package desktop;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

import adapters.Subscriber;
import adapters.product.create.*;

public class GUIAdminProduct extends JFrame implements Subscriber {
    private JTextField txtName = new JTextField(20);
    private JTextField txtPrice = new JTextField(10);
    private JTextField txtQty = new JTextField(5);
    private JTextArea txtDesc = new JTextArea(3, 20);
    private JTextField txtImage = new JTextField(20);
    // Giả sử có Category ID 1=Son, 2=Dưỡng da
    private JComboBox<String> cbCategory = new JComboBox<>(new String[]{"1 - Son môi", "2 - Dưỡng da"});
    
    private JButton btnAdd = new JButton("Thêm sản phẩm");

    private final CreateProductController controller;
    private final CreateProductViewModel viewModel;

    public GUIAdminProduct(CreateProductController controller, CreateProductViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addSubscriber(this);
        setupUI();
    }

    private void setupUI() {
        setTitle("Quản Lý Sản Phẩm (Admin)");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addToGrid(new JLabel("Tên SP:"), txtName, row++, gbc);
        addToGrid(new JLabel("Giá:"), txtPrice, row++, gbc);
        addToGrid(new JLabel("Số lượng:"), txtQty, row++, gbc);
        addToGrid(new JLabel("Danh mục:"), cbCategory, row++, gbc);
        addToGrid(new JLabel("Link ảnh:"), txtImage, row++, gbc);
        
        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; 
        add(new JScrollPane(txtDesc), gbc);
        row++;

        gbc.gridx = 1; gbc.gridy = row;
        add(btnAdd, gbc);

        btnAdd.addActionListener(e -> {
            try {
                CreateProductController.InputDTO input = new CreateProductController.InputDTO();
                input.name = txtName.getText();
                input.price = new BigDecimal(txtPrice.getText());
                input.quantity = Integer.parseInt(txtQty.getText());
                input.description = txtDesc.getText();
                input.image = txtImage.getText();
                
                String catStr = (String) cbCategory.getSelectedItem();
                input.categoryId = Long.parseLong(catStr.split(" - ")[0]);

                controller.execute(input);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu nhập không hợp lệ!");
            }
        });
    }

    private void addToGrid(JLabel lbl, Component comp, int row, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = row;
        add(lbl, gbc);
        gbc.gridx = 1;
        add(comp, gbc);
    }

    @Override
    public void update() {
        if (viewModel.isSuccess) {
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công! ID: " + viewModel.productId);
            txtName.setText(""); txtPrice.setText(""); txtDesc.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi: " + viewModel.message);
        }
    }
}