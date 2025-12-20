package desktop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

import adapters.Subscriber;
import adapters.product.create.*;
import adapters.product.getlist.*;
import adapters.product.update.*;
import adapters.product.delete.*;
// Import Category
import adapters.category.getlist.*;
import cosmetic.entities.Category;
import cosmetic.usecase.category.getlist.GetListCategoryRes;
import cosmetic.usecase.products.getlist.GetListProductRes;

public class GUIAdminProduct extends JPanel implements Subscriber {
    private JTable table;
    private DefaultTableModel model;
    
    // Inputs Form
    private JTextField txtName, txtPrice, txtQty, txtImg;
    private JTextArea txtDesc;
    
    // Thay JTextField ID bằng JComboBox chọn tên danh mục
    private JComboBox<CategoryItem> cbCategory; 

    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    // Controllers & ViewModels
    private final CreateProductController createCtrl;
    private final CreateProductViewModel createVM;
    private final GetListProductController listCtrl;
    private final GetListProductViewModel listVM;
    private final UpdateProductController updateCtrl;
    private final UpdateProductViewModel updateVM;
    private final DeleteProductController deleteCtrl;
    private final DeleteProductViewModel deleteVM;
    
    // Controller Category
    private final GetListCategoryController catListCtrl;
    private final GetListCategoryViewModel catListVM;

    public GUIAdminProduct(CreateProductController cCtrl, CreateProductViewModel cVM,
                           GetListProductController lCtrl, GetListProductViewModel lVM,
                           UpdateProductController uCtrl, UpdateProductViewModel uVM,
                           DeleteProductController dCtrl, DeleteProductViewModel dVM,
                           GetListCategoryController catCtrl, GetListCategoryViewModel catVM) {
        this.createCtrl = cCtrl; this.createVM = cVM;
        this.listCtrl = lCtrl;   this.listVM = lVM;
        this.updateCtrl = uCtrl; this.updateVM = uVM;
        this.deleteCtrl = dCtrl; this.deleteVM = dVM;
        this.catListCtrl = catCtrl; this.catListVM = catVM;

        // Đăng ký nhận thông báo
        this.createVM.addSubscriber(this);
        this.listVM.addSubscriber(this);
        this.updateVM.addSubscriber(this);
        this.deleteVM.addSubscriber(this);
        this.catListVM.addSubscriber(this);

        setupUI();
        
        // Load dữ liệu ban đầu
        listCtrl.execute("", null);
        catListCtrl.execute(); // Load danh mục ngay khi mở
    }

    // Class phụ để hiển thị tên danh mục trong ComboBox
    private static class CategoryItem {
        Long id;
        String name;
        public CategoryItem(Long id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CategoryItem that = (CategoryItem) o;
            return id.equals(that.id);
        }
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        
        // --- LEFT PANEL: Form nhập liệu ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setPreferredSize(new Dimension(300, 0));
        
        addField(formPanel, "Tên SP:", txtName = new JTextField());
        addField(formPanel, "Giá (VNĐ):", txtPrice = new JTextField());
        addField(formPanel, "Số lượng:", txtQty = new JTextField());
        
        // ComboBox Danh mục
        formPanel.add(new JLabel("Danh mục:"));
        cbCategory = new JComboBox<>();
        formPanel.add(cbCategory);
        formPanel.add(Box.createVerticalStrut(5));
        
        addField(formPanel, "URL Ảnh:", txtImg = new JTextField());
        
        formPanel.add(new JLabel("Mô tả:"));
        txtDesc = new JTextArea(3, 20);
        txtDesc.setLineWrap(true);
        formPanel.add(new JScrollPane(txtDesc));
        formPanel.add(Box.createVerticalStrut(10));

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        btnAdd = StyleUtils.createButton("Thêm");
        btnUpdate = StyleUtils.createButton("Sửa");
        btnDelete = StyleUtils.createButton("Xóa");
        btnDelete.setBackground(Color.RED);
        btnClear = new JButton("Làm mới");
        
        btnPanel.add(btnAdd); btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete); btnPanel.add(btnClear);
        formPanel.add(btnPanel);

        add(formPanel, BorderLayout.WEST);

        // --- CENTER PANEL: Table ---
        String[] cols = {"ID", "Tên SP", "Giá", "Kho", "Mô tả"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        StyleUtils.styleTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- EVENTS ---
        
        // 1. Click Table -> Đổ dữ liệu lên form
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    Long id = (Long) model.getValueAt(row, 0);
                    
                    for (GetListProductRes.ProductDTO p : listVM.products) {
                        if (p.id.equals(id)) {
                            // Đổ dữ liệu text
                            txtName.setText(p.name);
                            txtPrice.setText(String.format("%.0f", p.price));
                            txtQty.setText(p.quantity != null ? p.quantity.toString() : "0");
                            txtDesc.setText(p.description != null ? p.description.toString() : "");
                            txtImg.setText(p.imageUrl);
                            
                            // --- LOGIC CHỌN DANH MỤC TỰ ĐỘNG ---
                            if (p.categoryId != null) {
                                // Duyệt qua các item trong ComboBox để tìm ID trùng khớp
                                for (int i = 0; i < cbCategory.getItemCount(); i++) {
                                    CategoryItem item = cbCategory.getItemAt(i);
                                    if (item.id.equals(p.categoryId)) {
                                        cbCategory.setSelectedIndex(i); // Chọn đúng danh mục
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        });

        // 2. Nút Thêm
        btnAdd.addActionListener(e -> {
            try {
                CreateProductController.InputDTO dto = new CreateProductController.InputDTO();
                dto.name = txtName.getText();
                // Sửa lỗi BigDecimal
                dto.price = new BigDecimal(txtPrice.getText()); 
                dto.quantity = Integer.parseInt(txtQty.getText());
                dto.description = txtDesc.getText();
                dto.image = txtImg.getText(); // Lưu ý tên biến trong controller là 'image' hay 'imageUrl'
                
                // Lấy ID từ ComboBox
                CategoryItem selected = (CategoryItem) cbCategory.getSelectedItem();
                if (selected != null) {
                    dto.categoryId = selected.id;
                } else {
                    throw new Exception("Vui lòng chọn danh mục!");
                }
                
                createCtrl.execute(dto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá hoặc số lượng phải là số!");
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); 
            }
        });

        // 3. Nút Sửa
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row < 0) { JOptionPane.showMessageDialog(this, "Chọn SP cần sửa!"); return; }
            Long id = (Long) model.getValueAt(row, 0);
            
            try {
                UpdateProductController.InputDTO dto = new UpdateProductController.InputDTO();
                dto.id = id;
                dto.name = txtName.getText();
                // Sửa lỗi BigDecimal
                dto.price = Double.parseDouble(txtPrice.getText());
                dto.quantity = Integer.parseInt(txtQty.getText());
                dto.description = txtDesc.getText();
                dto.imageUrl = txtImg.getText();
                
                CategoryItem selected = (CategoryItem) cbCategory.getSelectedItem();
                if (selected != null) dto.categoryId = selected.id;
                
                updateCtrl.execute(dto);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); }
        });

        // 4. Nút Xóa
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row < 0) return;
            Long id = (Long) model.getValueAt(row, 0);
            if(JOptionPane.showConfirmDialog(this, "Xóa sản phẩm này?") == JOptionPane.YES_OPTION) {
                deleteCtrl.execute(id);
            }
        });

        btnClear.addActionListener(e -> {
            txtName.setText(""); txtPrice.setText(""); txtQty.setText("");
            txtDesc.setText(""); txtImg.setText("");
            if(cbCategory.getItemCount() > 0) cbCategory.setSelectedIndex(0);
            table.clearSelection();
        });
    }

    private void addField(JPanel p, String label, JTextField tf) {
        p.add(new JLabel(label));
        p.add(tf);
        p.add(Box.createVerticalStrut(5));
    }

    @Override
    public void update() {
        // Cập nhật bảng sản phẩm
        if (listVM.products != null) {
            model.setRowCount(0);
            for (GetListProductRes.ProductDTO p : listVM.products) {
                model.addRow(new Object[]{
                    p.id, p.name, StyleUtils.formatCurrency(p.price), p.quantity, p.description
                });
            }
        }
        
        // Cập nhật ComboBox danh mục
        if (catListVM.categories != null) {
            // Lưu lại item đang chọn
            Object selected = cbCategory.getSelectedItem();
            cbCategory.removeAllItems();
            for (Category c : catListVM.categories) {
                CategoryItem item = new CategoryItem(c.id, c.name);
                cbCategory.addItem(item);
            }
            // Restore selection nếu có
            if(selected != null) cbCategory.setSelectedItem(selected);
        }

        // Xử lý thông báo
        checkMsg(createVM.message, createVM.isSuccess, createVM);
        checkMsg(updateVM.message, updateVM.isSuccess, updateVM);
        checkMsg(deleteVM.message, deleteVM.isSuccess, deleteVM);
    }

    private void checkMsg(String msg, boolean success, Object vm) {
        if (msg != null && !msg.isEmpty()) {
            // 1. Hiển thị thông báo
            JOptionPane.showMessageDialog(this, msg);
            
            // 2. QUAN TRỌNG: Xóa message trong ViewModel NGAY LẬP TỨC
            // Để lần update sau (do reload list) nó không hiện lại nữa
            if(vm instanceof CreateProductViewModel) ((CreateProductViewModel)vm).message = null;
            if(vm instanceof UpdateProductViewModel) ((UpdateProductViewModel)vm).message = null;
            if(vm instanceof DeleteProductViewModel) ((DeleteProductViewModel)vm).message = null;

            // 3. Sau khi xóa message xong mới được reload list
            if(success) {
                listCtrl.execute("", null);
                clearForm(); // Tiện tay xóa trắng form luôn cho đẹp
            }
        }
    }

	private void clearForm() {
		// TODO Auto-generated method stub
		
	}
}