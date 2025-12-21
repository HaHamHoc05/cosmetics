package web;

import adapters.product.create.*;
import adapters.product.getdetail.*;
import adapters.product.update.*;
import cosmetic.usecase.product.create.CreateProductUseCase;
import cosmetic.usecase.product.update.UpdateProductUseCase;
import cosmetic.usecase.products.getdetail.GetDetailProductUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet({"/admin/product/create", "/admin/product/edit", "/product-save"})
public class ProductFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null) {
            Long id = Long.parseLong(idParam);
            GetDetailProductViewModel vm = new GetDetailProductViewModel();
            GetDetailProductUseCase useCase = new GetDetailProductUseCase(WebAppContext.getProductRepo(), new GetDetailProductPresenter(vm));
            new GetDetailProductController(useCase).execute(id);
            req.setAttribute("p", vm.productDetail);
        }
        req.getRequestDispatcher("/product-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        double price = Double.parseDouble(req.getParameter("price"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        String desc = req.getParameter("description");
        String img = req.getParameter("imageUrl");

        if (idStr == null || idStr.isEmpty()) {
            // THÊM MỚI - Khởi tạo với 3 tham số theo định nghĩa UseCase của bạn
            CreateProductViewModel vm = new CreateProductViewModel();
            CreateProductUseCase useCase = new CreateProductUseCase(WebAppContext.getProductRepo(), WebAppContext.getCategoryRepo(), new CreateProductPresenter(vm));
            CreateProductController controller = new CreateProductController(useCase);
            
            // Đóng gói vào InputDTO
            CreateProductController.InputDTO input = new CreateProductController.InputDTO();
            input.name = name;
            input.description = desc;
            input.price = BigDecimal.valueOf(price);
            input.quantity = quantity;
            input.image = img;
            input.categoryId = 1L; // Mặc định hoặc lấy từ form
            
            controller.execute(input);
        } else {
            // CẬP NHẬT (Làm tương tự nếu UpdateProductController cũng dùng InputDTO)
            Long id = Long.parseLong(idStr);
            UpdateProductViewModel vm = new UpdateProductViewModel();
            UpdateProductUseCase useCase = new UpdateProductUseCase(WebAppContext.getProductRepo(), new UpdateProductPresenter(vm));
            // Cần kiểm tra xem UpdateProductController của bạn nhận gì để truyền cho đúng
            // Ví dụ: new UpdateProductController(useCase).execute(id, name, price, quantity, desc, img);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }
}