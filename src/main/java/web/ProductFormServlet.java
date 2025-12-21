import java.io.IOException;

import adapters.product.create.CreateProductController;
import adapters.product.create.CreateProductPresenter;
import adapters.product.create.CreateProductViewModel;
import adapters.product.getdetail.GetDetailProductController;
import adapters.product.getdetail.GetDetailProductPresenter;
import adapters.product.getdetail.GetDetailProductViewModel;
import adapters.product.update.UpdateProductController;
import adapters.product.update.UpdateProductPresenter;
import adapters.product.update.UpdateProductViewModel;
import cosmetic.usecase.product.create.CreateProductUseCase;
import cosmetic.usecase.product.update.UpdateProductUseCase;
import cosmetic.usecase.products.getdetail.GetDetailProductUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/admin/product/create", "/admin/product/edit", "/product-save"})
public class ProductFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        
        if (idParam != null) { // Chế độ Sửa: Lấy thông tin cũ
            Long id = Long.parseLong(idParam);
            GetDetailProductViewModel vm = new GetDetailProductViewModel();
            GetDetailProductPresenter presenter = new GetDetailProductPresenter(vm);
            GetDetailProductUseCase useCase = new GetDetailProductUseCase(WebAppContext.getProductRepo(), presenter);
            
            GetDetailProductController controller = new GetDetailProductController(useCase);
            controller.execute(id);
            
            req.setAttribute("p", vm.productDetail);
        }
        req.getRequestDispatcher("/product-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Đọc dữ liệu từ Form
        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        double price = Double.parseDouble(req.getParameter("price"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        String description = req.getParameter("description");
        String imageUrl = req.getParameter("imageUrl");

        if (idStr == null || idStr.isEmpty()) {
            // 2. Chế độ THÊM MỚI
            CreateProductViewModel vm = new CreateProductViewModel();
            CreateProductPresenter presenter = new CreateProductPresenter(vm);
            CreateProductUseCase useCase = new CreateProductUseCase(WebAppContext.getProductRepo(), presenter);
            
            // Giả sử Controller của bạn nhận các tham số này
            new CreateProductController(useCase).execute(name, price, quantity, description, imageUrl);
        } else {
            // 3. Chế độ CẬP NHẬT
            Long id = Long.parseLong(idStr);
            UpdateProductViewModel vm = new UpdateProductViewModel();
            UpdateProductPresenter presenter = new UpdateProductPresenter(vm);
            UpdateProductUseCase useCase = new UpdateProductUseCase(WebAppContext.getProductRepo(), presenter);
            
            new UpdateProductController(useCase).execute(id, name, price, quantity, description, imageUrl);
        }

        // Sau khi xong quay về danh sách
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }
}