package desktop;

import javax.swing.*;
import java.awt.*;
import cosmetic.repository.*;
import adapters.category.getlist.GetListCategoryController;
import adapters.category.getlist.GetListCategoryPresenter;
import adapters.category.getlist.GetListCategoryViewModel;
// Import tất cả adapters và usecases
import adapters.product.create.*;
import cosmetic.usecase.category.getlist.GetListCategoryUseCase;
import cosmetic.usecase.product.create.CreateProductUseCase;
import adapters.product.getlist.*;
import cosmetic.usecase.products.getlist.GetListProductUseCase;
import adapters.product.update.*;
import cosmetic.usecase.product.update.UpdateProductUseCase;
import adapters.product.delete.*;
import cosmetic.usecase.product.delete.DeleteProductUseCase;
// ... (Order imports nếu cần)

public class GUIAdminDashboard extends JFrame {

    public GUIAdminDashboard() {
        setTitle("ADMIN DASHBOARD - COSMETICS");
        setSize(1200, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. KHỞI TẠO REPOSITORIES
        ProductRepository prodRepo = new MySQLProductRepository();
        CategoryRepository catRepo = new MySQLCategoryRepository();
        OrderRepository orderRepo = new MySQLOrderRepository();

        // 2. KHỞI TẠO USE CASES & ADAPTERS (PRODUCT)
        
        // - Create
        var createVM = new CreateProductViewModel();
        var createPres = new CreateProductPresenter(createVM);
        var createUC = new CreateProductUseCase(prodRepo, catRepo, createPres);
        var createCtrl = new CreateProductController(createUC);
        
        var catListVM = new GetListCategoryViewModel();
        var catListPres = new GetListCategoryPresenter(catListVM);
        var catListUC = new GetListCategoryUseCase(catRepo, catListPres);
        var catListCtrl = new GetListCategoryController(catListUC);

        // - Get List
        var listVM = new GetListProductViewModel();
        var listPres = new GetListProductPresenter(listVM);
        var listUC = new GetListProductUseCase(prodRepo, listPres);
        var listCtrl = new GetListProductController(listUC);

        // - Update
        var updateVM = new UpdateProductViewModel();
        var updatePres = new UpdateProductPresenter(updateVM);
        var updateUC = new UpdateProductUseCase(prodRepo, updatePres);
        var updateCtrl = new UpdateProductController(updateUC);

        // - Delete
        var deleteVM = new DeleteProductViewModel();
        var deletePres = new DeleteProductPresenter(deleteVM);
        var deleteUC = new DeleteProductUseCase(prodRepo, deletePres);
        var deleteCtrl = new DeleteProductController(deleteUC);

        // 3. TẠO CÁC TAB
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(StyleUtils.FONT_BOLD);

        // Tab Sản Phẩm: Truyền đủ 4 bộ controller/vm
        GUIAdminProduct prodTab = new GUIAdminProduct(
            createCtrl, createVM,
            listCtrl, listVM,
            updateCtrl, updateVM,
            deleteCtrl, deleteVM,
            catListCtrl, catListVM
        );
        tabs.addTab("QUẢN LÝ SẢN PHẨM", prodTab);

        // Tab Đơn Hàng (Bạn tạo tương tự, dùng GUIAdminOrder)
        // tabs.addTab("QUẢN LÝ ĐƠN HÀNG", new GUIAdminOrder(...));

        add(tabs, BorderLayout.CENTER);

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        header.setBackground(StyleUtils.PRIMARY_COLOR);
        JButton btnLogout = StyleUtils.createButton("Đăng Xuất");
        btnLogout.setBackground(Color.RED);
        btnLogout.addActionListener(e -> {
            this.dispose();
            GUILogin.main(null);
        });
        header.add(btnLogout);
        add(header, BorderLayout.NORTH);
    }
}