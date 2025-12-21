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
        String description = req.getParameter("description"); // Khai báo biến này
        String image = req.getParameter("imageUrl"); // Lấy từ input name="imageUrl"

        if (idStr == null || idStr.isEmpty()) {
            // THÊM MỚI
            CreateProductViewModel vm = new CreateProductViewModel();
            CreateProductUseCase useCase = new CreateProductUseCase(WebAppContext.getProductRepo(), WebAppContext.getCategoryRepo(), new CreateProductPresenter(vm));
            CreateProductController controller = new CreateProductController(useCase);
            
            CreateProductController.InputDTO input = new CreateProductController.InputDTO();
            input.name = name;
            input.price = java.math.BigDecimal.valueOf(price);
            input.quantity = quantity;
            input.description = description;
            input.image = image; // Gán vào field 'image'
            controller.execute(input);
        } else {
            // CẬP NHẬT
            Long id = Long.parseLong(idStr);
            UpdateProductViewModel vm = new UpdateProductViewModel();
            UpdateProductUseCase useCase = new UpdateProductUseCase(WebAppContext.getProductRepo(), new UpdateProductPresenter(vm));
            UpdateProductController controller = new UpdateProductController(useCase);

            UpdateProductController.InputDTO input = new UpdateProductController.InputDTO();
            input.id = id;
            input.name = name;
            input.price = price;
            input.quantity = quantity;
            input.description = description;
            input.image = image; // Sửa lỗi "image cannot be resolved" tại đây
            controller.execute(input);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }
}